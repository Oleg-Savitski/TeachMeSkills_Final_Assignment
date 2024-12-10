package com.teachmeskills.application.launcher.core;

import java.io.File;

import static com.teachmeskills.application.launcher.core.AuthenticationGateway.scanner;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * Manages the process of selecting a valid document directory for analysis.

 * Core Responsibilities:
 * - Prompt user for document directory path
 * - Validate directory accessibility
 * - Ensure secure and correct directory selection

 * User Interaction Workflow:
 * 1. Request directory path from user
 * 2. Validate directory existence
 * 3. Check directory readability
 * 4. Log successful selection

 * Validation Criteria:
 * - Directory must exist
 * - Must be a valid directory
 * - Must have read permissions

 * Error Handling:
 * - Continuous prompting until valid directory selected
 * - User-friendly error messages

 * Design Characteristics:
 * - Interactive console-based selection
 * - Robust input validation
 * - Secure directory access check

 * Example Usage:
 * <pre>
 * // Interactively select document directory
 * String documentPath = DocumentDirectorySelector.promptForDocumentDirectory();
 * </pre>
 *
 * Integration Scenarios:
 * - Document management systems
 * - File processing applications
 * - Batch document analysis
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [01.12.2024]
 */

public class DocumentDirectorySelector {
    public static String promptForDocumentDirectory() {
        while (true) {
            System.out.print("\nThe PATH to the document folder -> ");
            String folderPath = scanner.nextLine().trim();
            File folder = new File(folderPath);

            if (folder.exists() && folder.isDirectory() && folder.canRead()) {
                I_LOGGER.logInfo("A directory has been selected -> " + folderPath);
                System.out.println("\nA directory has been selected -> " + folderPath);
                return folderPath;
            } else {
                System.out.println("The specified path is not a directory.. Try again.");
            }
        }
    }
}