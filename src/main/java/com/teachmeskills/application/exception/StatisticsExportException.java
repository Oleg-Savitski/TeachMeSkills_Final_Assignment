package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception class for handling errors encountered during the export of statistical data.

 * Core Responsibilities:
 * - Represent specific types of errors that occur while exporting statistics.
 * - Provide meaningful error messages corresponding to each error type.
 * - Enable improved error traceability by linking the root cause of the exception.

 * Key Features:
 * - Enumerates predefined error types to classify common export issues.
 * - Associates each error type with a specific error message for consistency.
 * - Supports the inclusion of a nested throwable cause for error chaining.

 * Error Types:
 * - FILE_NOT_FOUND: Indicates that the specified file for exporting was not found.
 * - IO_ERROR: Signals an input/output operation failure during the export process.
 * - INVALID_DATA: Denotes that the data provided for export is invalid or incompatible.

 * Design Patterns:
 * - Utilizes an enum to organize and structure error types.
 * - Extends the AppException class to ensure consistent handling across related exceptions.

 * Usage Notes:
 * - Designed for use in scenarios where statistics-related export operations fail.
 * - Facilitates debugging and error resolution by offering targeted error classification.
 */
public class StatisticsExportException extends AppException {
    public enum Type {
        FILE_NOT_FOUND,
        IO_ERROR,
        INVALID_DATA
    }

    private static final Map<Type, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(Type.FILE_NOT_FOUND, "The file was not found!");
        errorMessages.put(Type.IO_ERROR, "Data input/output error!");
        errorMessages.put(Type.INVALID_DATA, "Invalid data for export!");
    }

    private final Type type;

    public StatisticsExportException(Type type, Throwable cause) {
        super(errorMessages.get(type), cause);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}