package com.teachmeskills.application.security.validation;

import com.teachmeskills.application.exception.WrongLoginException;
import com.teachmeskills.application.exception.WrongPasswordException;
import com.teachmeskills.application.utils.config.ConfigurationLoader;

import static com.teachmeskills.application.utils.constant.SecurityConstants.PASSWORD_DIGIT_REGEX;
import static com.teachmeskills.application.utils.constant.SecurityConstants.PASSWORD_SPECIAL_CHAR_REGEX;
/**
 * Class responsible for validating usernames and passwords during login processes.
 * Ensures that logins and passwords conform to predefined constraints set in the application.
 * Throws custom exceptions in cases of validation errors.

 * Responsibilities:
 * - Validate usernames based on character length, presence of spaces, and null checks.
 * - Validate passwords for character length, presence of required characters, and absence of spaces.

 * Exceptions:
 * - Throws WrongLoginException if username does not meet validation requirements.
 * - Throws WrongPasswordException if password violates any validation rule.
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