package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Specialized exception for cloud storage-related errors in the application.

 * Core Responsibilities:
 * - Represent different types of cloud storage failures
 * - Provide detailed error information
 * - Standardize cloud storage error handling

 * Key Features:
 * - Enumerated error types
 * - Predefined error messages
 * - Support for root cause tracking

 * Error Types:
 * - FILE_ACCESS_ERROR: Issues accessing file resources
 * - UPLOAD_FAILED: Problems uploading files to cloud storage
 * - CONFIGURATION_ERROR: Misconfiguration of cloud storage settings
 * - NETWORK_ERROR: Network-related issues during cloud storage operations

 * Design Patterns:
 * - Error type enumeration
 * - Predefined error message mapping
 * - Extensible exception handling

 * Example Usage:
 * <pre>
 * try {
 *     uploadFileToCloudStorage();
 * } catch (CloudStorageException e) {
 *     switch (e.getType()) {
 *         case UPLOAD_FAILED:
 *             // Handle upload failure
 *             break;
 *         case NETWORK_ERROR:
 *             // Handle network issues
 *             break;
 *     }
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [02.12.2024]
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