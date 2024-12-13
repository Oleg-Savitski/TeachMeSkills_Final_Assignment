package com.teachmeskills.application.model.impl;

import com.teachmeskills.application.model.IDocument;

import java.util.Objects;
/**
 * Represents the base implementation of a financial document with a total monetary amount.

 * This abstract class provides the foundation for various types of financial documents by
 * implementing the core functionality defined in the IDocument interface. The total amount
 * associated with the document is immutable and set during the creation of the derived class
 * instance.

 * Design Characteristics:
 * - Immutable total amount property.
 * - Implements the core method from the IDocument interface.
 * - Serves as a base class for specific financial document types.

 * Subclasses:
 * Classes extending AbstractDocument are expected to represent specific types of financial
 * documents, such as invoices, orders, and checks. These subclasses inherit the functionality
 * provided by AbstractDocument, while adding domain-specific behavior or attributes.

 * Key Features:
 * - Immutable financial data model.
 * - Consistent and reusable base for financial document hierarchy.

 * Methods:
 * - getTotalAmount(): Retrieves the immutable total amount of the document.
 * - equals(): Compares this document with another object for equality based on the totalAmount.
 * - hashCode(): Generates a hashcode based on the totalAmount for consistent hashing.
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