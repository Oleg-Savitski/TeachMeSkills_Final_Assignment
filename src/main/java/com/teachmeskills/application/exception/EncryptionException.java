package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Exception indicating errors related to encryption and decryption operations.

 * Core Responsibilities:
 * - Represent specific types of encryption-related issues.
 * - Provide standardized error messages for different encryption failure reasons.
 * - Offer detailed error information to help diagnose encryption errors effectively.

 * Key Features:
 * - Enumerated error types to classify encryption issues.
 * - Predefined error messages associated with specific error types.
 * - Support for root cause chaining via the throwable cause for improved traceability.

 * Error Types:
 * - ENCRYPTION_FAILED: A general failure in the encryption process.
 * - DECRYPTION_FAILED: A failure that occurs during the decryption process.
 * - INVALID_KEY: Indicates the use of an invalid or incompatible encryption key.

 * Usage Notes:
 * - Designed to be used in scenarios where encryption or decryption operations might fail.
 * - Helps in categorizing encryption-related issues, enabling better error handling and logging.

 * Design Patterns:
 * - Utilizes an enum to organize and manage different error types.
 * - Standardizes the handling of encryption-related exceptions within the application.
 */
public class EncryptionException extends AppException {
    public enum Type {
        ENCRYPTION_FAILED,
        DECRYPTION_FAILED,
        INVALID_KEY
    }

    private static final Map<Type, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(Type.ENCRYPTION_FAILED, "Encryption error!");
        errorMessages.put(Type.DECRYPTION_FAILED, "Decryption error!");
        errorMessages.put(Type.INVALID_KEY, "Invalid encryption key!");
    }

    private final Type type;

    public EncryptionException(Type type) {
        super(errorMessages.get(type));
        this.type = type;
    }

    public EncryptionException(Type type, Throwable cause) {
        super(errorMessages.get(type), cause);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}