package com.teachmeskills.application.utils.constant;
/**
 * Centralized interface for parsing-related regular expression patterns.

 * Provides standardized regex patterns for extracting specific data fields
 * from text, such as bill amounts, invoice totals, and order totals.

 * Key Features:
 * - Case-insensitive matching for enhanced flexibility.
 * - Handles various number formats such as decimal points, commas, and optional currency symbols.
 * - Tailored patterns to handle different document structures.

 * Regex Constants:
 * - {@code CHECK_REGEX}: Matches and extracts the total amount from bill text,
 *   supporting optional "EURO" and formatting like commas or periods in numbers.
 * - {@code INVOICE_REGEX}: Extracts the total amount from invoice text, capable of handling
 *   optional currency symbols and different formats for decimals.
 * - {@code ORDER_REGEX}: Extracts order total, accommodating optional commas for thousands and
 *   different decimal point formats.

 * Recommendations:
 * - Use these patterns within appropriate parsing methods to standardize
 *   the data extraction logic.
 * - Ensure the input text is preprocessed (e.g., removing unnecessary white spaces)
 *   for optimal regex matching.

 * Potential Improvements:
 * - Add patterns for parsing additional document fields.
 * - Enhance number extraction to support more regional variations.
 * - Build a utility class to validate or preview regex matches during runtime.
 */

public interface ParsingRegexConstants {

    String CHECK_REGEX = "(?i)Bill total amount(?: EURO)?\\s*(\\d+(?:[.,]\\d+)?)"; // 12 по факту, 14 по списку из 50 на сумму 19638.94
    String INVOICE_REGEX = "(?i)total\\s+amount\\s*:?\\s*\\$?\\s*(\\d+(?:\\.\\d{1,2})?)\\$?"; // 24 по факту из 65 по списку на сумму 8912.13
    String ORDER_REGEX = "Order Total\\s*([\\d]{1,4}(?:,\\d{3})*(?:\\.\\d{2})?|[\\d]+(?:\\.\\d{2})?)"; // 8 по факту из 21 по списку на сумму 13837.38
}