package com.teachmeskills.application.utils.constant;
/**
 * Centralized interface for regular expression patterns used in financial document parsing.

 * Provides standardized regex patterns for extracting financial information from various document types.

 * Key Features:
 * - Case-insensitive matching
 * - Flexible number format support (decimal, comma-separated)
 * - Targeted extraction of total amount values

 * Regex Patterns:
 * - {@link #CHECK_REGEX}: Pattern for parsing check total amounts
 * - {@link #INVOICE_REGEX}: Pattern for extracting invoice total amounts
 * - {@link #ORDER_REGEX}: Pattern for identifying order total amounts

 * Pattern Breakdown:
 * <pre>
 * CHECK_REGEX:
 * - Matches "Bill total amount" (case-insensitive)
 * - Captures numeric value with optional decimal
 *
 * INVOICE_REGEX:
 * - Matches "total amount" with optional currency symbols
 * - Supports various number formats
 *
 * ORDER_REGEX:
 * - Matches "Order Total" with flexible number formatting
 * </pre>
 *
 * Usage Examples:
 * <pre>
 * Pattern checkPattern = Pattern.compile(ParsingRegexConstants.CHECK_REGEX);
 * Matcher matcher = checkPattern.matcher(documentText);
 * if (matcher.find()) {
 *     String totalAmount = matcher.group(1);
 * }
 * </pre>
 *
 * Parsing Considerations:
 * - Regex patterns are designed to be flexible but may need updates
 * - Always validate and sanitize matched values
 * - Consider performance impact of complex regex

 * Potential Improvements:
 * - Support for more currency symbols
 * - Enhanced number format handling
 * - Locale-specific parsing
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [19.11.2024]
 */

public interface ParsingRegexConstants {

    String CHECK_REGEX = "(?i)Bill total amount(?: EURO)?\\s*(\\d+(?:[.,]\\d+)?)"; // 12 по факту, 14 по списку из 50 на сумму 19638.94
    String INVOICE_REGEX = "(?i)total\\s+amount\\s*:?\\s*\\$?\\s*(\\d+(?:\\.\\d{1,2})?)\\$?"; // 24 по факту из 65 по списку на сумму 8912.13
    String ORDER_REGEX = "Order Total\\s*([\\d]{1,4}(?:,\\d{3})*(?:\\.\\d{2})?|[\\d]+(?:\\.\\d{2})?)"; // 8 по факту из 21 по списку на сумму 13837.38
}