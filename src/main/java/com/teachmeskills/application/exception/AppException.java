package com.teachmeskills.application.exception;

import java.io.Serial;
/**
 * Represents a generic application-level exception that serves as a base
 * for more specific exception types within the application.

 * Core Responsibilities:
 * - Provide a common foundation for custom exceptions in the application
 * - Simplify exception hierarchy by extending from the Java Exception class
 * - Support custom error messages and causal tracking

 * Key Features:
 * - Constructor for initializing exceptions with a message
 * - Constructor for initializing exceptions with a message and a cause

 * Usage Notes:
 * This class is intended to be extended by more specific exception types
 * that represent domain or application-specific errors.

 * Design Patterns:
 * - Serves as a base class in a custom exception hierarchy, enabling a
 *   consistent structure for exception handling throughout the application
 */
public abstract class AppException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}