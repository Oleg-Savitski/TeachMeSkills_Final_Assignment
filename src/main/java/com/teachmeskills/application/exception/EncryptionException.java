package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Specialized exception for encryption-related errors in the application.

 * Core Responsibilities:
 * - Represent different types of encryption failures
 * - Provide detailed error information
 * - Standardize encryption error handling

 * Key Features:
 * - Enumerated error types
 * - Predefined error messages
 * - Support for root cause tracking

 * Error Types:
 * - ENCRYPTION_FAILED: Issues during encryption process
 * - DECRYPTION_FAILED: Problems during decryption process
 * - INVALID_KEY: Incorrect or invalid encryption key

 * Design Patterns:
 * - Error type enumeration
 * - Predefined error message mapping
 * - Extensible exception handling

 * Example Usage:
 * <pre>
 * try {
 *     encryptSensitiveData();
 * } catch (EncryptionException e) {
 *     switch (e.getType()) {
 *         case ENCRYPTION_FAILED:
 *             // Handle encryption failure
 *             break;
 *         case INVALID_KEY:
 *             // Handle key-related issues
 *             break;
 *     }
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [03.12.2024]
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