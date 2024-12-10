package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Specialized exception for parser service-related errors in the application.

 * Core Responsibilities:
 * - Represent different types of parsing and file handling failures
 * - Provide detailed error information
 * - Standardize parser service error handling

 * Key Features:
 * - Enumerated error types
 * - Predefined error messages
 * - Support for root cause tracking

 * Error Types:
 * - INVALID_DIRECTORY: Directory does not exist or is invalid
 * - FILE_NOT_FOUND: Specified file cannot be located
 * - FILE_MOVE_ERROR: Issues during file transfer
 * - FILE_PROCESSING_ERROR: General file processing failures
 * - INVALID_FILE_CONTENT: File contains invalid or unexpected content

 * Design Patterns:
 * - Error type enumeration
 * - Predefined error message mapping
 * - Extensible exception handling

 * Example Usage:
 * <pre>
 * try {
 *     parseFile(filePath);
 * } catch (ParserServiceException e) {
 *     switch (e.getType()) {
 *         case FILE_NOT_FOUND:
 *             // Handle missing file
 *             break;
 *         case INVALID_FILE_CONTENT:
 *             // Handle content validation error
 *             break;
 *     }
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [04.11.2024]
 */
public class ParserServiceException extends Exception {

    public enum Type {
        INVALID_DIRECTORY,
        FILE_NOT_FOUND,
        FILE_MOVE_ERROR,
        FILE_PROCESSING_ERROR,
        INVALID_FILE_CONTENT
    }

    private static final Map<Type, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(Type.INVALID_DIRECTORY, "The specified directory does not exist or is not a directory!");
        errorMessages.put(Type.FILE_NOT_FOUND, "The file was not found!");
        errorMessages.put(Type.FILE_MOVE_ERROR, "File transfer error!");
        errorMessages.put(Type.FILE_PROCESSING_ERROR, "Error processing the file!");
        errorMessages.put(Type.INVALID_FILE_CONTENT, "Invalid file contents!");
    }

    private final Type type;

    public ParserServiceException(Type type, Throwable cause) {
        super(errorMessages.get(type), cause);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}