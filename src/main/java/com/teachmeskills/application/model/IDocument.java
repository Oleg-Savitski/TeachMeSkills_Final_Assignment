package com.teachmeskills.application.model;
/**
 * Represents a contract for financial document entities.

 * This interface defines the essential behavior of a financial document,
 * which includes retrieving the total monetary amount associated with
 * the document. It serves as a core abstraction for implementing various
 * financial document types such as invoices, orders, and checks.
 *
 * Design Goals:
 * - Standardize the behavior for financial documents
 * - Enable consistent handling of document amounts
 * - Promote extensibility for specific document implementations

 * Key Responsibilities:
 * - Define a method to retrieve the total amount of a document

 * Implementations:
 * Classes implementing this interface must specify the behavior for
 * calculating or storing the total amount of the document. These classes
 * are expected to ensure the immutability of the total amount and may
 * include additional attributes relevant to their*/
public interface IDocument {

    double getTotalAmount();
}