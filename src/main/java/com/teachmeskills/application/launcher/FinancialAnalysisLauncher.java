package com.teachmeskills.application.launcher;

import com.teachmeskills.application.launcher.core.AuthenticationGateway;
import com.teachmeskills.application.launcher.core.DocumentAnalysisCoordinator;
import com.teachmeskills.application.utils.config.ConfigurationLoader;

import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * Main entry point for the Financial Analysis Application.

 * Application Characteristics:
 * - Centralized application launcher
 * - Manages document analysis workflow
 * - Provides robust error handling

 * Key Responsibilities:
 * - Coordinate document analysis process
 * - Manage application lifecycle
 * - Handle critical exceptions

 * Workflow Overview:
 * 1. Create document analysis coordinator
 * 2. Execute document analysis
 * 3. Handle potential exceptions
 * 4. Perform cleanup operations

 * Error Handling Strategy:
 * - Centralized exception management
 * - Detailed error logging
 * - Graceful application shutdown

 * Resource Management:
 * - Automatic resource closure
 * - Configuration loader shutdown
 * - Scanner resource cleanup

 * Example Execution Flow:
 * <pre>
 * // Application starts
 * FinancialAnalysisLauncher.main(args);
 * // Performs document analysis
 * // Handles potential errors
 * // Closes resources
 * </pre>
 *
 * Design Patterns:
 * - Singleton (Logger)
 * - Facade (DocumentAnalysisCoordinator)
 * - Try-with-resources
 *
 * @author [Oleg Savitski]
 * @version 1.1
 * @since [01.12.2024]
 */
public class FinancialAnalysisLauncher {
    public static void main(String[] args) {
        try {
            DocumentAnalysisCoordinator manager = new DocumentAnalysisCoordinator();
            manager.executeDocumentAnalysis();
        } catch (Exception e) {
            System.out.println("\nAn unexpected malfunction of the application.. Contact the administrator.");
            I_LOGGER.logError("A critical ERROR in the operation of the application : " +
                    e.getClass().getSimpleName() + " - " + e.getMessage());
        } finally {
            ConfigurationLoader.shutdown();
            AuthenticationGateway.scanner.close();
        }
    }
}