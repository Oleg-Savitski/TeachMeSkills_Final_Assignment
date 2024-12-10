package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Specialized exception for authentication-related errors in the application.

 * Core Responsibilities:
 * - Represent different types of authentication failures
 * - Provide detailed error information
 * - Standardize authentication error handling

 * Key Features:
 * - Enumerated error types
 * - Predefined error messages
 * - Support for root cause tracking

 * Error Types:
 * - INVALID_CREDENTIALS: Incorrect login or password
 * - DECRYPTION_ERROR: Issues with credential decryption
 * - AUTHENTICATION_FAILED: General authentication failure

 * Design Patterns:
 * - Error type enumeration
 * - Predefined error message mapping
 * - Extensible exception handling

 * Example Usage:
 * <pre>
 * try {
 *     authenticateUser();
 * } catch (AuthenticationException e) {
 *     switch (e.getType()) {
 *         case INVALID_CREDENTIALS:
 *             // Handle invalid credentials
 *             break;
 *         case DECRYPTION_ERROR:
 *             // Handle decryption issues
 *             break;
 *     }
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [01.12.2024]
 */
public class AuthenticationException extends AppException {
  public enum Type {
    INVALID_CREDENTIALS,
    DECRYPTION_ERROR,
    AUTHENTICATION_FAILED
  }

  private static final Map<Type, String> errorMessages = new HashMap<>();

  static {
    errorMessages.put(Type.INVALID_CREDENTIALS, "Invalid credentials!");
    errorMessages.put(Type.DECRYPTION_ERROR, "Decryption error!");
    errorMessages.put(Type.AUTHENTICATION_FAILED, "Authentication failed!");
  }

  private final Type type;

  public AuthenticationException(Type type) {
    super(errorMessages.get(type));
    this.type = type;
  }

  public AuthenticationException(Type type, Throwable cause) {
    super(errorMessages.get(type), cause);
    this.type = type;
  }

  public Type getType() {
    return type;
  }
}