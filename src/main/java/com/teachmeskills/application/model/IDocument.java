package com.teachmeskills.application.model;
/**
 * Core interface for financial document representation with amount calculation.

 * Design Principles:
 * - Standardized document amount retrieval
 * - Flexible implementation for various document types
 * - Lightweight and focused contract

 * Supported Document Types:
 * - Invoices
 * - Checks
 * - Orders
 * - Receipts

 * Key Responsibilities:
 * - Provide total document amount
 * - Enable consistent financial calculations

 * Usage Scenarios:
 * - Financial reporting
 * - Transaction tracking
 * - Accounting systems

 * Example Implementation:
 * <pre>
 * public class Invoice implements IDocument {
 *     private double amount;
 *
 *     public Invoice(double amount) {
 *         this.amount = amount;
 *     }
 *
 *     {@code @Override}
 *     public double getTotalAmount() {
 *         return amount;
 *     }
 * }
 * </pre>
 *
 * Integration Benefits:
 * - Polymorphic document handling
 * - Simplified financial calculations
 * - Consistent amount retrieval
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [12.11.2024]
 */
public interface IDocument {

    double getTotalAmount();
}