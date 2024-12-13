package com.teachmeskills.application.model.impl;
/**
 * Represents a financial order document with a specific total monetary amount.

 * This class extends the AbstractDocument class, inheriting its functionality
 * to manage immutable financial data. It is designed to model orders within
 * the financial document hierarchy.

 * Features:
 * - Immutable Total Amount: The total amount is set during instantiation and cannot be modified.
 * - Core Financial Functionality: Leverages the AbstractDocument base functionality for
 *   managing the document's total amount consistently.
 * - Part of a Financial Document System: Integrates with other financial documents, such as checks
 *   and invoices, for use in broader financial workflows.

 * Use Cases:
 * - Payment Processing: Used in systems handling order payments.
 * - Record Keeping: Maintains a consistent and immutable record of order details.
 * - Reporting and Analytics: Can be included in financial reporting or analytical processes.

 * Related Classes:
 * - AbstractDocument: The base class providing shared financial document functionality.
 * - Check: Models financial checks as part of the same document system.
 * - Invoice: Represents invoices within the financial hierarchy.
 */
public class Order extends AbstractDocument {

    public Order(double totalAmount) {
        super(totalAmount);
    }
}