package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Exception representing file analysis errors in the application.

 * Core Responsibilities:
 * - Encapsulates specific issues encountered during file processing.
 * - Provides descriptive error messages for various file-related issues.
 * - Standardizes error handling for file analysis operations.

 * Key Features:
 * - Enumerated error types to classify file analysis errors.
 * - Predefined error messages associated with specific error conditions.
 * - Facilitates clear and consistent error reporting.

 * Error Types:
 * - FILE_TOO_LARGE: The file size exceeds the permitted limit.
 * - FILE_NOT_READABLE: The file cannot be read due to permissions or other issues.
 * - NO_VALID_LINES: The file does not contain any lines that meet the validity criteria.
 * - IO_ERROR: An input/output operation error while working with the file.
 * - INVALID_CHECK_AMOUNT: The receipt amount format is incorrect.
 * - INVALID_INVOICE_AMOUNT: The invoice amount format is invalid.
 * - INVALID_ORDER_AMOUNT: The order amount format is invalid.

 * Design Patterns:
 * - Utilizes an enum to define and categorize error types.
 * - Simplifies exception handling by associating error types with specific messages.

 * Usage Notes:
 * - This exception is designed for file processing operations where specific errors
 *   need to be identified and handled with clear messages.
 * - Provides detailed error descriptions to help with debugging and error handling.
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