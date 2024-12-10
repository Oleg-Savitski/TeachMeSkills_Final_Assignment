package com.teachmeskills.application.model.impl;
/**
 * Represents a financial invoice document with a specific total amount.

 * Document Type Characteristics:
 * - Extends AbstractDocument
 * - Immutable financial record
 * - Standardized invoice representation

 * Business Domains:
 * - Commercial transactions
 * - Sales documentation
 * - Financial accounting

 * Design Principles:
 * - Lightweight document model
 * - Consistent with IDocument interface
 * - Supports comprehensive financial tracking

 * Example Usage:
 * <pre>
 * // Creating an invoice with a specific amount
 * Invoice salesInvoice = new Invoice(5000.25);
 *
 * // Retrieving total invoice amount
 * double invoiceTotal = salesInvoice.getTotalAmount();
 * </pre>
 *
 * Integration Scenarios:
 * - Enterprise resource planning (ERP)
 * - Billing systems
 * - Financial reporting

 * Key Attributes:
 * - Immutable total amount
 * - Simple and extensible structure
 * - Supports various business models
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [12.11.2024]
 */
public class Invoice extends AbstractDocument {

    public Invoice(double totalAmount) {
        super(totalAmount);
    }
}