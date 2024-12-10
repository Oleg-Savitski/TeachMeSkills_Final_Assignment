package com.teachmeskills.application.services.analyzer;

import com.teachmeskills.application.exception.FileAnalyzerException;

import java.io.File;
/**
 * Interface for file analysis operations with robust error handling and validation.

 * Key Features:
 * - File content analysis
 * - File validity checking
 * - Flexible file processing

 * Main Functionalities:
 * - Perform comprehensive file analysis
 * - Validate file content integrity

 * Use Cases:
 * - Document processing
 * - Security file scanning
 * - Content verification
 * - File integrity checks

 * Design Principles:
 * - Separation of concerns
 * - Error-driven design
 * - Flexible implementation

 * Example Usage:
 * <pre>
 * IFileAnalyzer analyzer = new ConcreteFileAnalyzer();
 *
 * try {
 *     File file = new File("example.txt");
 *
 *     // Check file validity
 *     boolean isValid = analyzer.checkFileContentValidity(file);
 *
 *     // Perform analysis if valid
 *     if (isValid) {
 *         analyzer.performFileAnalysis(file);
 *     }
 * } catch (FileAnalyzerException e) {
 *     // Handle analysis errors
 * }
 * </pre>
 *
 * Error Handling:
 * - Custom FileAnalyzerException for analysis errors
 * - Comprehensive error information
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [23.11.2024]
 */
public interface IFileAnalyzer {

    void performFileAnalysis(File file) throws FileAnalyzerException;

    boolean checkFileContentValidity(File file) throws FileAnalyzerException;
}