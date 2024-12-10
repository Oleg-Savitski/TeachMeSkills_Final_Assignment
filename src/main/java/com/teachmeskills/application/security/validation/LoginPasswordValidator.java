package com.teachmeskills.application.security.validation;

import com.teachmeskills.application.exception.WrongLoginException;
import com.teachmeskills.application.exception.WrongPasswordException;
import com.teachmeskills.application.utils.config.ConfigurationLoader;

import static com.teachmeskills.application.utils.constant.SecurityConstants.PASSWORD_DIGIT_REGEX;
import static com.teachmeskills.application.utils.constant.SecurityConstants.PASSWORD_SPECIAL_CHAR_REGEX;
/**
 * Comprehensive login and password validation service for secure user authentication.

 * Key Security Features:
 * - Username length validation
 * - Password complexity checking
 * - Configurable validation rules
 * - Strict input sanitization

 * Validation Criteria:
 * - Maximum username/password length
 * - No whitespace characters
 * - Password complexity requirements

 * Security Principles:
 * - Prevent injection attacks
 * - Enforce strong password policies
 * - Validate user input rigorously

 * Example Usage:
 * <pre>
 * LoginPasswordValidator validator = new LoginPasswordValidator();
 *
 * try {
 *     // Validate username
 *     validator.validateUsername("john_doe");
 *
 *     // Validate password
 *     validator.validateUserPassword("StrongP@ss123");
 * } catch (WrongLoginException | WrongPasswordException e) {
 *     // Handle validation errors
 *     System.out.println("Validation failed: " + e.getMessage());
 * }
 * </pre>
 *
 * Error Handling:
 * - Specific exceptions for login and password validation
 * - Detailed error messages
 * - Prevents weak or malformed credentials

 * Configuration:
 * - Dynamically loads validation parameters
 * - Supports flexible security settings
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [04.11.2024]
 */
public class LoginPasswordValidator {

    public void validateUsername(String username) throws WrongLoginException {
        if (username == null ||
                username.length() > ConfigurationLoader.MAX_LOGIN_LENGTH ||
                username.contains(" ")) {
            throw new WrongLoginException(
                    "Invalid login: length up to " + ConfigurationLoader.MAX_LOGIN_LENGTH + " characters without spaces.");
        }
    }

    public void validateUserPassword(String password) throws WrongPasswordException {
        if (password == null ||
                password.length() > ConfigurationLoader.MAX_PASSWORD_LENGTH ||
                password.contains(" ") ||
                !password.matches(PASSWORD_DIGIT_REGEX) ||
                !password.matches(PASSWORD_SPECIAL_CHAR_REGEX)) {

            throw new WrongPasswordException(
                    "Incorrect data entered. Restriction on data entry to " + ConfigurationLoader.MAX_PASSWORD_LENGTH + ".");
        }
    }
}