package com.teachmeskills.application.model.impl;
/**
 * Represents a financial order document with a specific total amount.

 * Document Type Characteristics:
 * - Extends AbstractDocument
 * - Immutable financial transaction record
 * - Standardized order representation

 * Business Domains:
 * - Sales transactions
 * - Purchase management
 * - Inventory tracking

 * Design Principles:
 * - Lightweight document model
 * - Consistent with IDocument interface
 * - Supports comprehensive order tracking

 * Example Usage:
 * <pre>
 * // Creating an order with a specific amount
 * Order purchaseOrder = new Order(3500.50);
 *
 * // Retrieving total order amount
 * double orderTotal = purchaseOrder.getTotalAmount();
 * </pre>
 *
 * Integration Scenarios:
 * - E-commerce platforms
 * - Supply chain management
 * - Financial reporting systems

 * Key Attributes:
 * - Immutable total amount
 * - Simple and extensible structure
 * - Supports various business models
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [12.11.2024]
 */
public class Order extends AbstractDocument {

    public Order(double totalAmount) {
        super(totalAmount);
    }
}