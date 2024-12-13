package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Exception indicating errors related to file parsing operations within the application.

 * Core Responsibilities:
 * - Represent specific types of parsing-related issues.
 * - Provide detailed and standardized error messages for various parsing failure scenarios.
 * - Allow identification of the type of parsing error that occurred through an enumerated type.
 * - Support root cause analysis via the throwable cause.

 * Key Features:
 * - Enumerated error types for classifying common parsing issues.
 * - Predefined error messages mapped to each error type for consistency in error reporting.
 * - Detailed information about the error type through the {@code getType()} method.

 * Error Types:
 * - INVALID_DIRECTORY: Indicates the specified directory is invalid or does not exist.
 * - FILE_NOT_FOUND: Signals that the expected file could not be located.
 * - FILE_MOVE_ERROR: Represents an error during the file transfer or movement process.
 * - FILE_PROCESSING_ERROR: Denotes an error encountered during file processing tasks.
 * - INVALID_FILE_CONTENT: Indicates that the content of the file is not valid or cannot be processed.

 * Design Patterns:
 * - Utilizes an enum to categorize and manage different error scenarios.
 * - Facilitates extensible and structured handling of parsing-related exceptions.

 * Usage Notes:
 * - This class is specifically intended for scenarios where parsing operations fail, such as file reading or processing errors.
 * - Enables cleaner exception handling by providing a clear type and message for each error scenario.

 * Constructor Details:
 * - Supports initialization with both an error type and an optional throwable cause for tracing the root cause of errors.
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