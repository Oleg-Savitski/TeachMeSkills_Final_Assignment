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
 * LoggerService is an implementation of the ILogger interface providing
 * functionality for logging messages at different levels, such as Info,
 * Warning, and Error. It supports asynchronous logging with file output.

 * Key Features:
 * - Supports asynchronous logging using a dedicated single-threaded executor.
 * - Writes log messages to specific files based on log levels.
 * - Allows custom log formatting using optional constructor.
 * - Formats log messages to include timestamps, levels, and caller details.

 * Usage Scenarios:
 * - Application runtime information logging.
 * - Warning and error tracking with stack trace capture when applicable.
 * - Asynchronous background writing to log files to avoid blocking.

 * Implementation Details:
 * - Uses a BlockingQueue to manage log entries internally.
 * - Supports ANSI color-coding for console logs (Info, Warning, Error).
 * - Creates and maintains a designated directory for log files.
 * - Supports proper shutdown and resource cleanup via the AutoCloseable interface.

 * Thread Safety:
 * - The logging mechanism is designed to support multi-threaded applications
 *   via asynchronous queue-based logging.
 * - Ensures proper cleanup by shutting down the executor during close.

 * Logging Levels:
 * - Info: General information about application flow.
 * - Warning: Indications of potential issues or unexpected conditions.
 * - Error: Critical errors and exceptions, allows stack trace inclusion.

 * Example Internal Workflow:
 * 1. A logging message is queued with relevant details such as level,
 *    formatted message, and optional stack trace for errors.
 * 2. A background thread processes log entries from the queue.
 * 3. Each log message is written to the appropriate file and optionally decorated
 *    with stack trace if relevant.
 */
public class LoggerService implements ILogger, AutoCloseable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_INFO = "\u001B[34m";
    private static final String ANSI_ERROR = "\u001B[31m";
    private static final String ANSI_WARNING = "\u001B[33m";

    private static final int CALLER_DEPTH = 3;

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
        this.logExecutor = Executors.newSingleThreadExecutor(this::createLoggerThread);
        this.formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                .withZone(ZoneId.of("Europe/Moscow"));

        createLogDirectory();
        startAsyncLogger();
    }

    private Thread createLoggerThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.setName("async-logger-thread");
        return thread;
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
                }
            }
        });
    }

    private void writeLogToFile(LogEntry entry) {
        try {
            Path logPath = Paths.get(entry.logPath());
            writeMessageToFile(logPath, entry.formattedMessage());

            if (entry.isError() && entry.stackTrace() != null) {
                writeMessageToFile(logPath, entry.stackTrace());
            }
        } catch (IOException e) {
            System.err.println("Log writing error: " + e.getMessage());
        }
    }

    private void writeMessageToFile(Path logPath, String message) throws IOException {
        Files.writeString(
                logPath,
                message + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    @Override
    public void logInfo(String message) {
        queueLog(ANSI_INFO + "Info" + ANSI_RESET, message, INFO_LOG_PATH, false, null);
    }

    @Override
    public void logWarning(String message) {
        queueLog(ANSI_WARNING + "Warning" + ANSI_RESET, message, WARNING_LOG_PATH, false, null);
    }

    @Override
    public void logError(String message) {
        logError(message, null);
    }

    public void logError(String message, Throwable throwable) {
        StackTraceElement[] stackTrace = throwable != null ? throwable.getStackTrace() : Thread.currentThread().getStackTrace();

        String callerDetails = extractCallerDetails(stackTrace);
        String fullStackTrace = throwable != null ? getStackTraceAsString(throwable) : null;

        String errorMessage = String.format(
                "%s | Location: %s",
                message,
                callerDetails
        );

        queueLog(ANSI_ERROR + "Error" + ANSI_RESET, errorMessage, ERROR_LOG_PATH, true, fullStackTrace);
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private String extractCallerDetails(StackTraceElement[] stackTrace) {
        if (stackTrace == null || stackTrace.length < CALLER_DEPTH) {
            return "Unknown location";
        }

        StackTraceElement caller = stackTrace[CALLER_DEPTH];
        return String.format(
                "%s.%s (Line: %d)",
                caller.getClassName(),
                caller.getMethodName(),
                caller.getLineNumber()
        );
    }

    private void queueLog(String level, String message, String logPath, boolean isError, String stackTrace) {
        String formattedMessage;
        try {
            String callerLocation = extractCallerDetails(Thread.currentThread().getStackTrace());
            formattedMessage = String.format(
                    logFormat,
                    LocalDateTime.now().format(formatter),
                    level.replace(ANSI_INFO, "").replace(ANSI_ERROR, "").replace(ANSI_WARNING, "").replace(ANSI_RESET, ""),
                    String.format("%s | Location: %s", message, callerLocation)
            );
        } catch (Exception e) {
            System.err.println("Failed to format log message: " + e.getMessage());
            return;
        }

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
        } catch (InterruptedException e) {
            logExecutor.shutdownNow();
        }
    }
}