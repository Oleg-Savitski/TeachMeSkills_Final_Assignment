package com.teachmeskills.application.exception;

import java.util.HashMap;
import java.util.Map;
/**
 * Specialized exception class used to handle session management-related errors
 * within the application. It includes predefined error types to classify and
 * manage specific session-related failures.

 * Core Responsibilities:
 * - Represent specific types of session handling issues.
 * - Provide standardized error messages for different session failure scenarios.
 * - Simplify session error tracking and debugging.

 * Key Features:
 * - Enumerated error types for categorizing session failures.
 * - Predefined error messages associated with each error type for consistency.
 * - Constructor accepting error type to simplify exception handling.

 * Error Types:
 * - TOKEN_GENERATION_ERROR: Indicates an error occurred during token generation.
 * - INVALID_TOKEN: Represents an issue with the validity of the token.
 * - EXPIRATION_DATE_ERROR: Encounters errors when setting or handling expiration dates.

 * Design Patterns:
 * - Utilizes an enum for organizing and managing error types.
 * - Standardizes session error reporting by assigning descriptive, predefined messages.

 * Usage Notes:
 * - Designed to be thrown in session management routines where errors such as
 *   token generation, validation, or expiration date setting occur.
 * - Provides meaningful messages that aid in identifying and resolving session-related issues.
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