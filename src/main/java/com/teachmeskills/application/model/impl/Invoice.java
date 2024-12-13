package com.teachmeskills.application.model.impl;
/**
 * Represents a financial invoice document with a specific total monetary amount.

 * This class extends AbstractDocument, inheriting its core functionality to manage
 * immutable financial data. It is designed specifically to model invoices within
 * the financial document hierarchy.

 * Features:
 * - Immutable Total Amount: The total amount is set during instantiation and cannot be modified.
 * - Reuse of AbstractDocument Core Functionality: Utilizes the base functionality for managing
 *   financial document properties consistently.
 * - Integration into Financial Workflows: Compatible with systems that manage and analyze invoice transactions.

 * Design and Usability:
 * - Ensures lightweight object modeling for streamlined integration.
 * - Serves as part of a consistent financial document system.
 * - Facilitates use in scenarios like invoice processing, tracking, and financial reporting.

 * Related Classes:
 * - AbstractDocument: The base class providing core financial document functionality.
 * - Check: Represents checks as financial documents in the same system.
 * - Order: Represents payment orders with similar characteristics.
 */
public class Invoice extends AbstractDocument {

    public Invoice(double totalAmount) {
        super(totalAmount);
    }
}