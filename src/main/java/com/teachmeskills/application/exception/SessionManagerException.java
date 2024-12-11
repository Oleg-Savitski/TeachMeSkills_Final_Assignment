package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Specialized exception for session management-related errors in the application.

 * Core Responsibilities:
 * - Represent different types of session and token handling failures
 * - Provide detailed error information
 * - Standardize session management error handling

 * Key Features:
 * - Enumerated error types
 * - Predefined error messages
 * - Comprehensive token and session-related error categories

 * Error Types:
 * - TOKEN_GENERATION_ERROR: Failure during access token creation
 * - INVALID_TOKEN: Token is not valid or recognized
 * - EXPIRATION_DATE_ERROR: Issues with token expiration settings

 * Design Patterns:
 * - Error type enumeration
 * - Predefined error message mapping
 * - Extensible exception handling

 * Example Usage:
 * <pre>
 * try {
 *     generateAccessToken();
 * } catch (SessionManagerException e) {
 *     switch (e.getType()) {
 *         case TOKEN_GENERATION_ERROR:
 *             // Handle token generation failure
 *             break;
 *         case INVALID_TOKEN:
 *             // Handle invalid token scenario
 *             break;
 *     }
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [03.11.2024]
 */
public class SessionManagerException extends Exception {

  public enum Type {
    TOKEN_GENERATION_ERROR,
    INVALID_TOKEN,
    EXPIRATION_DATE_ERROR,
  }

  private static final Map<Type, String> errorMessages = new HashMap<>();

  static {
    errorMessages.put(Type.TOKEN_GENERATION_ERROR, "Error generating the access token!");
    errorMessages.put(Type.INVALID_TOKEN, "Invalid access token!");
    errorMessages.put(Type.EXPIRATION_DATE_ERROR, "An error occurred when setting the expiration date!");
  }

  public SessionManagerException(Type type) {
    super(errorMessages.get(type));
  }
}