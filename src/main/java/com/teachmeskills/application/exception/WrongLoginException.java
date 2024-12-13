package com.teachmeskills.application.exception;
/**
 * Exception indicating errors related to invalid login credentials or login attempts.

 * Core Responsibilities:
 * - Represent specific issues encountered during user login processes.
 * - Provide meaningful error messages to indicate the reason for a failed login attempt.

 * Key Features:
 * - Ability to define custom error messages when a login error occurs.
 * - Extends the base functionality from the {@code AppException} class.

 * Usage Notes:
 * - This exception should be thrown when a login attempt fails due to invalid credentials
 *   or other authentication-related issues.
 * - Helps in identifying and handling authentication failures in a standardized manner within the application.

 * Design Patterns:
 * - Leverages the custom exception hierarchy by extending {@code AppException} to provide
 *   application-specific exceptions.
 */
public class WrongLoginException extends AppException {

    public WrongLoginException(String message) {
        super(message);
    }
}