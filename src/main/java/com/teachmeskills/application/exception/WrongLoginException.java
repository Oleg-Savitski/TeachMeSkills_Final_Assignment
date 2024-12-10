package com.teachmeskills.application.exception;
/**
 * Specialized exception for login-related authentication failures in the application.

 * Core Responsibilities:
 * - Represent login authentication errors
 * - Provide detailed error information about login failures
 * - Standardize login error handling

 * Key Features:
 * - Extends AppException for consistent error handling
 * - Supports custom error messages
 * - Used for tracking and managing login-related issues

 * Common Scenarios:
 * - Incorrect username
 * - Invalid password
 * - Account locked or disabled

 * Design Patterns:
 * - Custom exception for specific authentication failures
 * - Extensible error reporting

 * Example Usage:
 * <pre>
 * try {
 *     authenticateUser(username, password);
 * } catch (WrongLoginException e) {
 *     // Handle login failure
 *     logLoginAttempt(username, false);
 *     displayErrorMessage(e.getMessage());
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [30.11.2024]
 */
public class WrongLoginException extends AppException {

    public WrongLoginException(String message) {
        super(message);
    }
}