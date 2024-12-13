package com.teachmeskills.application.utils.constant;
/**
 * Centralized interface for security-related constants.

 * Provides predefined regular expressions for validating password strength
 * based on the inclusion of specific character types.

 * Key Features:
 * - Regular expression to check for the presence of at least one digit in a password.
 * - Regular expression to check for the presence of at least one special character in a password.

 * Usage Recommendations:
 * - Use these regex constants for password validation in authentication or user management modules.
 * - Combine these patterns with additional validation rules to enforce comprehensive password policies.

 * Potential Improvements:
 * - Extend the constants to include regex for other password criteria, such as length, uppercase letters, or whitespace.
 * - Allow customization or configuration of these patterns to meet specific security requirements.
 */

public interface SecurityConstants {

    String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    String PASSWORD_SPECIAL_CHAR_REGEX = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*";
}