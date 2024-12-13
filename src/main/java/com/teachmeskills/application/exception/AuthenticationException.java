package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Specialized exception for authentication-related errors in the application.

 * Core Responsibilities:
 * - Represent various authentication failure scenarios
 * - Provide clear and predefined error messages for different error types
 * - Facilitate error categorization using an enumerated type
 * - Support causal tracking to indicate underlying issues

 * Key Features:
 * - Defines multiple types of authentication errors using an enum `Type`
 * - Maps predefined error messages to each error type for consistent error descriptions
 * - Extends the base `AppException` to ensure standardized exception handling

 * Error Types:
 * - INVALID_CREDENTIALS: Indicates that the provided credentials are incorrect
 * - DECRYPTION_ERROR: Represents issues in decrypting authentication data
 * - AUTHENTICATION_FAILED: A general failure in verifying authentication
 * - SESSION_CREATION_ERROR: Unexpected issues during session creation

 * Design Considerations:
 * - Each error type is associated with a descriptive message to simplify error tracking
 * - Provides constructors to define exceptions with or without a root cause
 */
public class AuthenticationException extends AppException {
  public enum Type {
    INVALID_CREDENTIALS,
    DECRYPTION_ERROR,
    AUTHENTICATION_FAILED,
    SESSION_CREATION_ERROR,

  }

  private static final Map<Type, String> errorMessages = new HashMap<>();

  static {
    errorMessages.put(Type.INVALID_CREDENTIALS, "Invalid credentials!");
    errorMessages.put(Type.DECRYPTION_ERROR, "Decryption error!");
    errorMessages.put(Type.AUTHENTICATION_FAILED, "Authentication failed!");
    errorMessages.put(Type.SESSION_CREATION_ERROR, "Unexpected error in session creation!");
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