package com.teachmeskills.application.services.analyzer.impl;

import com.teachmeskills.application.exception.FileAnalyzerException;
import com.teachmeskills.application.model.impl.Check;
import com.teachmeskills.application.model.impl.Invoice;
import com.teachmeskills.application.model.impl.Order;
import com.teachmeskills.application.services.analyzer.IFileAnalyzer;
import com.teachmeskills.application.services.logger.ILogger;
import com.teachmeskills.application.services.statistic.impl.StatsService;

import static com.teachmeskills.application.utils.constant.FileIOConstants.BUFFER_SIZE;
import static com.teachmeskills.application.utils.constant.FileIOConstants.MAX_FILE_SIZE_MB;
import static com.teachmeskills.application.utils.constant.ParsingRegexConstants.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Advanced file analysis service for processing financial documents with comprehensive validation and error handling.

 * Key Features:
 * - Robust file content parsing
 * - Multiple document type support (Check, Invoice, Order)
 * - Detailed error logging
 * - Statistical data collection

 * Supported Document Types:
 * - Checks
 * - Invoices
 * - Orders

 * Main Functionalities:
 * - File content validation
 * - Line-by-line document processing
 * - Automatic amount extraction
 * - Statistical record keeping

 * Processing Capabilities:
 * - Regex-based pattern matching
 * - Amount conversion
 * - Error-tolerant parsing

 * Performance Characteristics:
 * - Buffered file reading
 * - UTF-8 encoding support
 * - Configurable file size limits

 * Example Usage:
 * <pre>
 * StatsService statsService = new StatsService();
 * ILogger logger = new LoggerImpl();
 *
 * FileAnalyzer analyzer = new FileAnalyzer(statsService, logger);
 * File documentFile = new File("financial_data.txt");
 *
 * try {
 *     // Validate file content
 *     boolean isValid = analyzer.checkFileContentValidity(documentFile);
 *
 *     // Perform comprehensive analysis
 *     if (isValid) {
 *         analyzer.performFileAnalysis(documentFile);
 *     }
 * } catch (FileAnalyzerException e) {
 *     // Handle analysis errors
 * }
 * </pre>
 *
 * Error Handling:
 * - Comprehensive exception types
 * - Detailed error logging
 * - Graceful error recovery

 * Dependencies:
 * - StatsService for statistical tracking
 * - ILogger for logging operations
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [26.11.2024]
 */
public class FileAnalyzer implements IFileAnalyzer {

    private final StatsService statistic;
    private final ILogger logger;

    private static class PatternCache {
        static final Pattern CHECK_PATTERN = Pattern.compile(CHECK_REGEX);
        static final Pattern INVOICE_PATTERN = Pattern.compile(INVOICE_REGEX);
        static final Pattern ORDER_PATTERN = Pattern.compile(ORDER_REGEX);
    }

    public FileAnalyzer(StatsService statistic, ILogger logger) {
        this.statistic = Objects.requireNonNull(statistic, "StatsService cannot be null!");
        this.logger = Objects.requireNonNull(logger, "Logger cannot be null!");
    }

    @Override
    public boolean checkFileContentValidity(File file) throws FileAnalyzerException {
        logger.logInfo("Checking the contents of the file: " + file.getName());
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), BUFFER_SIZE)) {

            if (file.length() > MAX_FILE_SIZE_MB * 1024 * 1024) {
                logger.logError("The file is too big to process: " + file.getName() + " (size: " + file.length() + " bytes)");
                throw new FileAnalyzerException(FileAnalyzerException.Type.FILE_TOO_LARGE);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (isValidLine(line)) {
                    logger.logInfo("A valid line was found in the file: " + file.getName());
                    return true;
                }
            }
            logger.logWarning("No valid lines found in the file: " + file.getName());
            return false;
        } catch (IOException e) {
            logger.logError("File verification error: " + file.getName() + " - " + e.getMessage());
            throw new FileAnalyzerException(FileAnalyzerException.Type.IO_ERROR);
        }
    }

    @Override
    public void performFileAnalysis(File file) throws FileAnalyzerException {
        logger.logInfo("The beginning of file analysis: " + file.getName());

        if (!file.exists() || !file.canRead()) {
            throw new FileAnalyzerException(FileAnalyzerException.Type.FILE_NOT_READABLE);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), BUFFER_SIZE)) {

            if (file.length() > MAX_FILE_SIZE_MB * 1024 * 1024) {
                throw new FileAnalyzerException(FileAnalyzerException.Type.FILE_TOO_LARGE);
            }

            long validLinesCount = reader.lines()
                    .filter(this::processLine)
                    .count();

            if (validLinesCount == 0) {
                throw new FileAnalyzerException(FileAnalyzerException.Type.NO_VALID_LINES);
            }

            logger .logInfo("File analysis completed: " + file.getName());
        } catch (IOException e) {
            logger.logError("File reading error: " + file.getName() + " - " + e.getMessage());
            throw new FileAnalyzerException(FileAnalyzerException.Type.IO_ERROR);
        }
    }

    private boolean isValidLine(String line) {
        return line != null && !line.trim().isEmpty() &&
                (PatternCache.CHECK_PATTERN.matcher(line).find() ||
                        PatternCache.INVOICE_PATTERN.matcher(line).find() ||
                        PatternCache.ORDER_PATTERN.matcher(line).find());
    }

    private boolean processLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return false;
        }

        try {
            return processCheck(line) || processInvoice(line) || processOrder(line);
        } catch (FileAnalyzerException e) {
            logger.logError("String processing error: " + line + " - " + e.getMessage());
            return false;
        }
    }

    private boolean processCheck(String line) throws FileAnalyzerException {
        Matcher checkMatcher = PatternCache.CHECK_PATTERN.matcher(line);
        if (checkMatcher.find()) {
            try {
                String amountString = checkMatcher.group(1).replace(",", ".");
                double amount = Double.parseDouble(amountString);
                statistic.recordCheck(new Check(amount));
                logger.logInfo("The CHECK has been processed successfully: amount = " + amount + ", line = " + line);
                return true;
            } catch (NumberFormatException e) {
                logger.logError("Incorrect format of the CHECK amount: " + line + " (failed to convert: " + checkMatcher.group(1) + ")");
                throw new FileAnalyzerException(FileAnalyzerException.Type.INVALID_CHECK_AMOUNT);
            }
        }
        logger.logWarning("Invalid string in CHECK for processing: " + line);
        return false;
    }

    private boolean processInvoice(String line) throws FileAnalyzerException {
        Matcher invoiceMatcher = PatternCache.INVOICE_PATTERN.matcher(line);
        if (invoiceMatcher.find()) {
            try {
                String amountString = invoiceMatcher.group(1).replace(",", ".");
                double amount = Double.parseDouble(amountString);
                statistic.recordInvoice(new Invoice(amount));
                logger.logInfo("The INVOICE has been successfully processed: amount = " + amount + ", line = " + line);
                return true;
            } catch (NumberFormatException e) {
                logger.logError("Invalid INVOICE amount format: " + line + " (failed to convert: " + invoiceMatcher.group(1) + ")");
                throw new FileAnalyzerException(FileAnalyzerException.Type.INVALID_INVOICE_AMOUNT);
            }
        }
        logger.logWarning("Invalid string in INVOICE for processing: " + line);
        return false;
    }

    private boolean processOrder(String line) throws FileAnalyzerException {
        Matcher orderMatcher = PatternCache.ORDER_PATTERN.matcher(line);
        if (orderMatcher.find()) {
            try {
                String amountString = orderMatcher.group(1).replace(",", "");
                double amount = Double.parseDouble(amountString);
                statistic.recordOrder(new Order(amount));
                logger.logInfo("The ORDER has been successfully processed: amount = " + amount + ", line = " + line);
                return true;
            } catch (NumberFormatException e) {
                logger.logError("Invalid ORDER amount format: " + line + " (failed to convert: " + orderMatcher.group(1) + ")");
                throw new FileAnalyzerException(FileAnalyzerException.Type.INVALID_ORDER_AMOUNT);
            }
        }
        logger.logWarning("Invalid string in ORDER for processing: " + line);
        return false;
    }
}