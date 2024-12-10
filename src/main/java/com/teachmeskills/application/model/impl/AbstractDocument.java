package com.teachmeskills.application.model.impl;

import com.teachmeskills.application.model.IDocument;

import java.util.Objects;
/**
 * Abstract base implementation for financial documents with standard amount management.

 * Design Characteristics:
 * - Implements core IDocument interface
 * - Provides common document functionality
 * - Supports inheritance for specialized document types

 * Key Features:
 * - Immutable total amount
 * - Standard equality and hash code implementation
 * - Simplified document creation

 * Inheritance Strategy:
 * - Base class for specific document implementations
 * - Enforces consistent amount handling

 * Example Inheritance:
 * <pre>
 * public class Invoice extends AbstractDocument {
 *     private String invoiceNumber;
 *
 *     public Invoice(double amount, String invoiceNumber) {
 *         super(amount);
 *         this.invoiceNumber = invoiceNumber;
 *     }
 * }
 * </pre>
 *
 * Design Patterns:
 * - Template Method
 * - Immutable Object

 * Best Practices:
 * - Enforce document amount validation
 * - Provide consistent object comparison
 * - Support efficient collection storage
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [12.11.2024]
 */
public abstract class AbstractDocument implements IDocument {

    private final double totalAmount;

    protected AbstractDocument(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractDocument document)) return false;
        return Double.compare(document.totalAmount, totalAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalAmount);
    }
}