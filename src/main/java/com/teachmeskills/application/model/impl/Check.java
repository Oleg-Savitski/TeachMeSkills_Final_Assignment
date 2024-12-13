package com.teachmeskills.application.model.impl;
/**
 * Represents a financial check document with a specific total amount.

 * This class extends the AbstractDocument class, inheriting its functionality for managing
 * immutable financial data models. It provides a specialized implementation specific to
 * checks while maintaining consistency with the base document structure.

 * Features:
 * - Financial Check Representation: Standardized modeling of a check with a defined monetary value.
 * - Extensibility: Forms part of a hierarchical document system for diverse types of financial records.
 * - Integration: Used in financial systems for processing, tracking, and reporting check-related transactions.

 * Design Characteristics:
 * - Immutable total monetary amount set during instantiation.
 * - Lightweight object model for easy integration into financial workflows.
 * - Implements core financial functionality via inheritance from AbstractDocument.

 * Business Scenarios:
 * - Payment Tracking: Used for documenting and managing check payments.
 * - Financial Analysis: Allows for seamless inclusion in financial reporting and analytic systems.
 * - System Integration: Can be incorporated with services that process or analyze financial document transactions.

 * Related Classes:
 * - AbstractDocument: Base class providing core document functionalities.
 * - Order: Models financial orders with characteristics similar to a check.
 * - Invoice: Represents financial invoices in the same document hierarchy.
 */
public class Check extends AbstractDocument {

    public Check(double totalAmount) {
        super(totalAmount);
    }
}