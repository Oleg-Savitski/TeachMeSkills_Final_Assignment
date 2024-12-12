package com.teachmeskills.application.launcher.core;

import java.io.File;

import static com.teachmeskills.application.launcher.core.AuthenticationGateway.scanner;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * The DocumentDirectorySelector class provides a method to prompt the user
 * to select a valid directory path for document storage. It performs validation
 * and ensures the specified directory meets necessary requirements such as
 * existence, being a directory, and having read permissions.

 * Responsibilities:
 * - Prompts the user for a document directory path through console input.
 * - Validates the provided directory path for existence, type, and permissions.
 * - Limits the number of attempts to select a valid directory path.
 * - Logs the selected directory path upon successful selection.
 * - Throws an exception if the user exceeds the maximum number of attempts.

 * Features:
 * - Console-based interactive directory selection.
 * - Input validation:
 *   - Ensures the specified path exists.
 *   - Ensures the specified path is a directory.
 *   - Ensures the directory has the necessary read permissions.
 * - Configurable number of user attempts.

 * Error Handling:
 * - Provides descriptive error messages for incorrect or invalid input.
 * - Limits the number of retries to prevent indefinite user input.
 * - Throws an IllegalStateException when the maximum number of attempts is exceeded.

 * Logging:
 * - Logs information when a valid directory is successfully selected.
 */

public class DocumentDirectorySelector {
    private static final int MAX_ATTEMPTS = 5;

    public static String promptForDocumentDirectory() {
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("\nThe PATH to the document folder -> ");
            String folderPath = scanner.nextLine().trim();
            File folder = new File(folderPath);

            if (!folder.exists()) {
                System.out.println("Error: The specified path does not exist. Please enter a valid path.");
            } else if (!folder.isDirectory()) {
                System.out.println("Error: The specified path is not a directory. Please enter a valid directory path.");
            } else if (!folder.canRead()) {
                System.out.println("Error: The specified directory cannot be accessed. Check the permissions.");
            } else {
                I_LOGGER.logInfo("A directory has been selected -> " + folderPath);
                System.out.println("\nA directory has been selected -> " + folderPath);
                return folderPath;
            }

            attempts++;
            System.out.println("Remaining attempts: " + (MAX_ATTEMPTS - attempts));
        }

        throw new IllegalStateException("Maximum number of attempts exceeded. Unable to select a valid directory.");
    }
}