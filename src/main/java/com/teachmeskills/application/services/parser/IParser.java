package com.teachmeskills.application.services.parser;
/**
 * Interface for parsing documents within a specified directory.

 * Defines a contract for document parsing services with the following key characteristics:
 * - Processes multiple documents in a given directory
 * - Supports authentication via access token
 * - Designed for flexible document processing strategies

 * Key Features:
 * - Directory-based document parsing
 * - Secure access with authentication token
 * - Flexible implementation for different document types

 * Usage Scenarios:
 * - Bulk document processing
 * - Automated document analysis
 * - File system document extraction

 * Method Specification:
 * - {@link #parseDocumentsInDirectory(String, String)}
 *   Parses all documents in the specified directory

 * Usage Examples:
 * <pre>
 * IParser parser = new DocumentParser();
 * parser.parseDocumentsInDirectory("/path/to/documents", "user-access-token");
 * </pre>
 *
 * Design Considerations:
 * - Implement robust error handling
 * - Support various document formats
 * - Provide logging and traceability

 * Potential Improvements:
 * - Add support for recursive directory parsing
 * - Implement filtering mechanisms
 * - Support for different parsing strategies
 *
 * @author [Oleg savitski]
 * @version 1.0
 * @since [18.11.2024]
 */
public interface IParser {

    void parseDocumentsInDirectory(String directoryPath, String accessToken);
}