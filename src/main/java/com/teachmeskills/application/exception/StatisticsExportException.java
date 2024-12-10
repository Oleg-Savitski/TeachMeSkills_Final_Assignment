package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;/**
 * Specialized exception for statistics export-related errors in the application.

 * Core Responsibilities:
 * - Represent different types of statistics export failures
 * - Provide detailed error information
 * - Standardize statistics export error handling

 * Key Features:
 * - Enumerated error types
 * - Predefined error messages
 * - Comprehensive statistics export error categories

 * Error Types:
 * - FILE_NOT_FOUND: Required export file cannot be located
 * - IO_ERROR: Input/Output issues during export process
 * - INVALID_DATA: Export data is invalid or cannot be processed

 * Design Patterns:
 * - Error type enumeration
 * - Predefined error message mapping
 * - Extensible exception handling

 * Example Usage:
 * <pre>
 * try {
 *     exportStatistics();
 * } catch (StatisticsExportException e) {
 *     switch (e.getType()) {
 *         case FILE_NOT_FOUND:
 *             // Handle missing export file
 *             break;
 *         case INVALID_DATA:
 *             // Handle data validation error
 *             break;
 *     }
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [01.12.2024]
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