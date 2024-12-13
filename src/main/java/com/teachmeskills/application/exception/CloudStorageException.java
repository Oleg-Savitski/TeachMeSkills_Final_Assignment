package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Exception indicating errors related to cloud storage operations.

 * Core Responsibilities:
 * - Represent specific types of issues encountered while interacting
 *   with cloud storage services.
 * - Provide detailed error information for debugging and error handling.
 * - Support classification of error types to ensure standardized handling
 *   across the application.

 * Key Features:
 * - Enumerated error types for common cloud storage issues.
 * - Predefined error messages associated with each error type.
 * - Support for root cause chaining via the throwable cause.

 * Error Types:
 * - FILE_ACCESS_ERROR: Error accessing a file in the local or cloud file system.
 * - UPLOAD_FAILED: File upload to cloud storage has failed.
 * - CONFIGURATION_ERROR: Issues related to misconfiguration of cloud storage settings.
 * - NETWORK_ERROR: Network connectivity issues while interacting with cloud storage.

 * Design Patterns:
 * - Utilizes an enum to organize and categorize error types.
 * - Supports extensibility of error reporting by standardizing exception structure.

 * Usage Notes:
 * - Intended for use in cloud storage-related operations where specific
 *   error conditions need to be flagged.
 * - Facilitates debugging by providing meaningful error types and messages.
 */
public class CloudStorageException extends AppException {

    public enum Type {
        FILE_ACCESS_ERROR,
        UPLOAD_FAILED,
        CONFIGURATION_ERROR,
        NETWORK_ERROR
    }

    private static final Map<Type, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(Type.FILE_ACCESS_ERROR, "File access error!");
        errorMessages.put(Type.UPLOAD_FAILED, "The file could not be uploaded to the cloud storage!");
        errorMessages.put(Type.CONFIGURATION_ERROR, "Cloud Storage Configuration Error!");
        errorMessages.put(Type.NETWORK_ERROR, "Network error when working with cloud storage!");
    }

    private final Type type;

    public CloudStorageException(Type type) {
        super(errorMessages.get(type));
        this.type = type;
    }

    public CloudStorageException(Type type, Throwable cause) {
        super(errorMessages.get(type), cause);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}