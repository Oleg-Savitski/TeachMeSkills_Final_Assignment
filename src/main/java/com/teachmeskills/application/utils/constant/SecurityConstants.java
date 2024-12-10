package com.teachmeskills.application.utils.constant;
/**
 * Centralized interface for security-related constants and regex patterns.

 * Provides standardized regular expression patterns for password validation
 * and other security-related checks.

 * Key Features:
 * - Regex patterns for password complexity validation
 * - Supports common password strength requirements

 * Password Validation Patterns:
 * - {@link #PASSWORD_DIGIT_REGEX}: Checks for presence of at least one digit
 * - {@link #PASSWORD_SPECIAL_CHAR_REGEX}: Validates special character inclusion

 * Usage Examples:
 * <pre>
 * // Checking password complexity
 * boolean hasDigit = password.matches(SecurityConstants.PASSWORD_DIGIT_REGEX);
 * boolean hasSpecialChar = password.matches(SecurityConstants.PASSWORD_SPECIAL_CHAR_REGEX);
 *
 * if (!hasDigit || !hasSpecialChar) {
 *     throw new WeakPasswordException("Password does not meet complexity requirements");
 * }
 * </pre>
 *
 * Password Complexity Recommendations:
 * - Combine multiple validation checks
 * - Implement comprehensive password strength validation
 * - Consider using more advanced password validation libraries

 * Potential Improvements:
 * - Add regex for uppercase and lowercase letters
 * - Implement minimum length requirement
 * - Support for custom password policies

 * Security Considerations:
 * - Regex patterns are basic checks
 * - Supplement with additional security measures
 * - Regularly update security requirements
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [28.11.2024]
 */

public interface SecurityConstants {

    String PASSWORD_DIGIT_REGEX = ".*\\d.*";
    String PASSWORD_SPECIAL_CHAR_REGEX = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*";
}