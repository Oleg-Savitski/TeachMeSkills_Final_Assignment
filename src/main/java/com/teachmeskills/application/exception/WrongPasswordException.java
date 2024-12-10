package com.teachmeskills.application.exception;
/**
 * Specialized exception for password-related authentication failures in the application.

 * Core Responsibilities:
 * - Represent password authentication errors
 * - Provide detailed error information about password validation failures
 * - Standardize password-related error handling

 * Key Features:
 * - Extends AppException for consistent error management
 * - Supports custom error messages
 * - Used for tracking and managing password-related issues

 * Common Scenarios:
 * - Incorrect password entry
 * - Password does not meet complexity requirements
 * - Password expired or requires reset

 * Design Patterns:
 * - Custom exception for specific password authentication failures
 * - Extensible error reporting

 * Example Usage:
 * <pre>
 * try {
 *     validatePassword(password);
 * } catch (WrongPasswordException e) {
 *     // Handle password validation failure
 *     logPasswordAttempt(username, false);
 *     displayErrorMessage(e.getMessage());
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [30.12.2024]
 */
public class WrongPasswordException extends AppException {

    public WrongPasswordException(String message) {
        super(message);
    }
}