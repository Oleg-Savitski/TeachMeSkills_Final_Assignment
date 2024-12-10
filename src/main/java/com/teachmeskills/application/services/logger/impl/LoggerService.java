package com.teachmeskills.application.services.logger.impl;

import com.teachmeskills.application.services.logger.ILogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.*;

import static com.teachmeskills.application.utils.constant.FilePathConstants.*;

/**
 * Asynchronous logging service with file-based logging.

 * Features:
 * - Asynchronous log writing to files
 * - Flexible log formatting
 * - Error stack trace logging
 * - Thread-safe log queue
 * - Supports different log levels (Info, Warning, Error)
 * - No console output

 * Usage examples:
 * <pre>
 * LoggerService logger = new LoggerService();
 * logger.logInfo("Application started");
 * logger.logWarning("Potential issue detected");
 * logger.logError("Critical error", exception);
 * </pre>
 *
 * Thread safety: Fully thread-safe with concurrent log processing
 * Log storage: Writes to separate files for each log level
 *
 * @author [Oleg Savitski]
 * @version 1.1
 * @since [07.12.2024]
 */
public class LoggerService implements ILogger, AutoCloseable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_INFO = "\u001B[34m";
    private static final String ANSI_ERROR = "\u001B[31m";
    private static final String ANSI_WARNING = "\u001B[33m";

    private final String logFormat;
    private final BlockingQueue<LogEntry> logQueue;
    private final ExecutorService logExecutor;
    private final DateTimeFormatter formatter;

    private record LogEntry(String formattedMessage, String logPath, boolean isError, String stackTrace) {}

    public LoggerService() {
        this("%s | %s | %s");
    }

    public LoggerService(String logFormat) {
        this.logFormat = logFormat;
        this.logQueue = new LinkedBlockingQueue<>(1000);
        this.logExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("async-logger-thread");
            return thread;
        });
        this.formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                .withZone(ZoneId.of("Europe/Moscow"));

        createLogDirectory();
        startAsyncLogger();
    }

    private void createLogDirectory() {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create log directory: " + e.getMessage());
        }
    }

    private void startAsyncLogger() {
        logExecutor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    LogEntry logEntry = logQueue.take();
                    writeLogToFile(logEntry);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void writeLogToFile(LogEntry entry) {
        try {
            Path logPath = Paths.get(entry.logPath());

            Files.writeString(
                    logPath,
                    entry.formattedMessage() + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            if (entry.isError() && entry.stackTrace() != null) {
                Files.writeString(
                        logPath,
                        entry.stackTrace() + System.lineSeparator(),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.APPEND
                );
            }
        } catch (IOException e) {
            System.err.println("Log writing error: " + e.getMessage());
        }
    }

    @Override
    public void logInfo(String message) {
        queueLog(ANSI_INFO + "Info" + ANSI_RESET, message, INFO_LOG_PATH, false, null);
    }

    private String extractErrorLocation(StackTraceElement[] stackTrace) {
        if (stackTrace == null || stackTrace.length == 0) {
            return "Unknown location";
        }

        StackTraceElement caller = stackTrace.length > 1
                ? stackTrace[1]
                : stackTrace[0];

        return String.format(
                "%s.%s (Line: %d)",
                caller.getClassName(),
                caller.getMethodName(),
                caller.getLineNumber()
        );
    }

    @Override
    public void logError(String message) {
        String errorLocation = extractErrorLocation(Thread.currentThread().getStackTrace());

        String detailedMessage = String.format(
                "%s | Location: %s",
                message,
                errorLocation
        );

        queueLog(
                ANSI_ERROR + "Error" + ANSI_RESET,
                detailedMessage,
                ERROR_LOG_PATH,
                true,
                null
        );
    }

    public void logError(String message, Throwable throwable) {
        StackTraceElement[] stackTrace = throwable != null
                ? throwable.getStackTrace()
                : Thread.currentThread().getStackTrace();

        String errorLocation = extractErrorLocation(stackTrace);

        String fullStackTrace = null;
        String errorMessage = message;

        if (throwable != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            fullStackTrace = stringWriter.toString();

            errorMessage = String.format(
                    "%s - %s",
                    message,
                    throwable.getMessage() != null ? throwable.getMessage() : "No additional details"
            );
        }

        String detailedMessage = String.format(
                "%s | Location: %s",
                errorMessage,
                errorLocation
        );

        queueLog(
                ANSI_ERROR + "Error" + ANSI_RESET,
                detailedMessage,
                ERROR_LOG_PATH,
                true,
                fullStackTrace
        );
    }

    @Override
    public void logWarning(String message) {
        queueLog(ANSI_WARNING + "Warning" + ANSI_RESET, message, WARNING_LOG_PATH, false, null);
    }

    private void queueLog(String level, String message, String logPath, boolean isError, String stackTrace) {
        String formattedMessage = String.format(
                logFormat,
                LocalDateTime.now().format(formatter),
                level.replace(ANSI_INFO, "").replace(ANSI_ERROR, "").replace(ANSI_WARNING, "").replace(ANSI_RESET, ""),
                message
        );

        try {
            boolean offered = logQueue.offer(new LogEntry(formattedMessage, logPath, isError, stackTrace));
            if (!offered) {
                System.err.println("Log queue is full. Message dropped: " + formattedMessage);
            }
        } catch (Exception e) {
            System.err.println("Failed to queue log message: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        logExecutor.shutdown();
        try {
            if (!logExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                logExecutor.shutdownNow();
            }
        } catch ( InterruptedException e) {
            logExecutor.shutdownNow();
        }
    }
}