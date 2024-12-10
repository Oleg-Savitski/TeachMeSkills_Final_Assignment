package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Specialized exception for file analysis-related errors in the application.

 * Core Responsibilities:
 * - Represent different types of file analysis failures
 * - Provide detailed error information
 * - Standardize file processing error handling

 * Key Features:
 * - Enumerated error types
 * - Predefined error messages
 * - Comprehensive file-related error categories

 * Error Types:
 * - FILE_TOO_LARGE: File size exceeds maximum limit
 * - FILE_NOT_READABLE: Unable to read file contents
 * - NO_VALID_LINES: No processable data in the file
 * - IO_ERROR: Input/Output related issues
 * - INVALID_CHECK_AMOUNT: Incorrect receipt amount format
 * - INVALID_INVOICE_AMOUNT: Incorrect invoice amount format
 * - INVALID_ORDER_AMOUNT: Incorrect order amount format

 * Design Patterns:
 * - Error type enumeration
 * - Predefined error message mapping
 * - Extensible exception handling

 * Example Usage:
 * <pre>
 * try {
 *     analyzeFile(file);
 * } catch (FileAnalyzerException e) {
 *     switch (e.getType()) {
 *         case FILE_TOO_LARGE:
 *             // Handle file size exceeded
 *             break;
 *         case NO_VALID_LINES:
 *             // Handle empty or invalid file
 *             break;
 *     }
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [03.12.2024]
 */
public class FileAnalyzerException extends Exception {

    public enum Type {
        FILE_TOO_LARGE,
        FILE_NOT_READABLE,
        NO_VALID_LINES,
        IO_ERROR,
        INVALID_CHECK_AMOUNT,
        INVALID_INVOICE_AMOUNT,
        INVALID_ORDER_AMOUNT
    }

    private static final Map<Type, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(Type.FILE_TOO_LARGE, "The file exceeds the maximum allowed size!");
        errorMessages.put(Type.FILE_NOT_READABLE, "The file cannot be read!");
        errorMessages.put(Type.NO_VALID_LINES, "There are no valid strings in the file!");
        errorMessages.put(Type.IO_ERROR, "I/O error when working with the file!");
        errorMessages.put(Type.INVALID_CHECK_AMOUNT, "Incorrect format of the receipt amount!");
        errorMessages.put(Type.INVALID_INVOICE_AMOUNT, "The invoice amount format is incorrect!");
        errorMessages.put(Type.INVALID_ORDER_AMOUNT, "The order amount format is incorrect!");
    }

    public FileAnalyzerException(Type type) {
        super(errorMessages.get(type));
    }
}