package com.teachmeskills.application.launcher;

import com.teachmeskills.application.launcher.core.AuthenticationGateway;
import com.teachmeskills.application.launcher.core.DocumentAnalysisCoordinator;
import com.teachmeskills.application.utils.config.ConfigurationLoader;

import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * The FinancialAnalysisLauncher class serves as the main entry point for the Financial Analysis application.
 * It initializes the document analysis coordinator, manages the execution of the document analysis process,
 * and ensures proper handling of resource shutdown and exceptions.

 * Core Responsibilities:
 * - Launch the document analysis process.
 * - Ensure proper resource management and environment cleanup.
 * - Handle unexpected exceptions during execution.

 * Workflow:
 * 1. Instantiate the DocumentAnalysisCoordinator.
 * 2. Execute the document analysis via the coordinator.
 * 3. Handle exceptions by logging critical errors and notifying the user.
 * 4. Perform cleanup operations with resource shutdown in the finally block.

 * Exception Handling:
 * - Catches generic exceptions to ensure smooth application handling.
 * - Logs error messages for unexpected issues using the global I_LOGGER instance.

 * Cleanup:
 * - Gracefully shuts down system resources such as configuration loaders and authentication gateways.
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