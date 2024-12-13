package com.teachmeskills.application.exception;
/**
 * Exception indicating errors related to incorrect password input or validation failures.

 * Core Responsibilities:
 * - Represent issues encountered during password authentication or validation.
 * - Provide a descriptive error message to indicate the reason for the error.

 * Key Features:
 * - Extends the base functionality from the {@code AppException} class.
 * - Allows for customizable error messages to specify details regarding the incorrect password.

 * Usage Notes:
 * - This exception should be thrown in scenarios where a password does not meet
 *   required validation criteria or fails during authentication.
 * - Facilitates consistent error handling and identification of password-related issues
 *   across the application.

 * Design Patterns:
 * - Leverages the custom exception hierarchy by extending {@code AppException} to ensure
 *   a well-structured and maintainable approach to exception handling.
 */
public class WrongPasswordException extends AppException {

    public WrongPasswordException(String message) {
        super(message);
    }
}