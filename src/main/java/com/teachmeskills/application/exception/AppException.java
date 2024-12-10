package com.teachmeskills.application.exception;

import java.io.Serial;
/**
 * Base abstract exception class for the application.

 * Core Responsibilities:
 * - Provide a common base for application-specific exceptions
 * - Standardize exception handling
 * - Support serialization

 * Key Features:
 * - Extends standard Exception class
 * - Supports custom error messages
 * - Allows chaining of underlying causes

 * Design Principles:
 * - Centralized exception management
 * - Consistent error reporting
 * - Extensibility for specific exception types

 * Inheritance Strategy:
 * - Abstract base class for all application-specific exceptions
 * - Provides common constructor implementations

 * Serialization Support:
 * - Implements serialVersionUID for version control
 * - Ensures compatibility across different Java versions

 * Example Usage:
 * <pre>
 * public class CustomAppException extends AppException {
 *     public CustomAppException(String message) {
 *         super(message);
 *     }
 * }
 * </pre>
 *
 * Best Practices:
 * - Use specific exception types
 * - Provide clear, descriptive error messages
 * - Include root cause when possible
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [01.12.2024]
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