package com.teachmeskills.application.model.impl;
/**
 * Represents a financial check document with a specific total amount.

 * Document Type Characteristics:
 * - Extends AbstractDocument
 * - Immutable financial instrument
 * - Standardized check representation

 * Use Cases:
 * - Financial transactions
 * - Payment processing
 * - Accounting documentation

 * Design Principles:
 * - Lightweight document model
 * - Consistent with IDocument interface
 * - Supports basic financial tracking

 * Example Usage:
 * <pre>
 * // Creating a check with a specific amount
 * Check paymentCheck = new Check(1500.75);
 *
 * // Retrieving total amount
 * double checkAmount = paymentCheck.getTotalAmount();
 * </pre>
 *
 * Integration Patterns:
 * - Financial reporting systems
 * - Payment management
 * - Transaction tracking

 * Immutability:
 * - Total amount set at creation
 * - Prevents post-creation modifications
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [12.11.2024]
 */
public class Check extends AbstractDocument {

    public Check(double totalAmount) {
        super(totalAmount);
    }
}