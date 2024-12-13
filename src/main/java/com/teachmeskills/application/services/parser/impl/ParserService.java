package com.teachmeskills.application.services.parser.impl;

import com.teachmeskills.application.exception.ParserServiceException;
import com.teachmeskills.application.exception.SessionManagerException;
import com.teachmeskills.application.exception.StatisticsExportException;
import com.teachmeskills.application.services.analyzer.impl.FileAnalyzer;
import com.teachmeskills.application.services.logger.ILogger;
import com.teachmeskills.application.services.parser.IParser;
import com.teachmeskills.application.services.parser.stats.InvalidFileStats;
import com.teachmeskills.application.session.ISession;
import com.teachmeskills.application.services.statistic.impl.StatsService;
import com.teachmeskills.application.utils.config.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

import static com.teachmeskills.application.utils.constant.FilePathConstants.AMOUNT_STATS_FILE_NAME;
import static com.teachmeskills.application.utils.constant.FilePathConstants.INVALID_STATS_FILE_NAME;
/**
 * The {@code ParserService} class provides a comprehensive implementation of the {@code IParser} interface.
 * It is responsible for parsing files within a specified directory and categorizing them
 * as valid or invalid based on specific criteria. It also records and reports statistics about
 * the file processing operation.

 * Key Features:
 * - Processes files within a target directory
 * - Categorizes files as valid or invalid based on content, extension, and naming conventions
 * - Records and exports file processing statistics
 * - Logs information, warnings, and errors during parsing operations
 * - Handles the creation of an invalid files directory to store rejected files

 * Core Functionalities:
 * - Access token validation before file processing
 * - File and directory validation
 * - Detailed logging of significant events and errors
 * - File analysis for content validity
 * - Handling of invalid files via specified reasons and relocation to the invalid folder
 * - Reporting and exporting statistics at the end of operations

 * Exception Handling:
 * - Catches and handles exceptions related to directory access, file operations, and statistics export
 * - Logs detailed error messages for troubleshooting and traceability

 * Dependencies:
 * - {@link ISession}: For access token management and validation
 * - {@link ILogger}: For logging operations
 * - {@link StatsService}: For collecting and exporting statistics
 * - {@link InvalidFileStats}: For recording information about invalid files
 * - {@code FileAnalyzer}: For performing detailed file content analysis

 * Usage Scenarios:
 * - Large-scale text file parsing and analysis
 * - Invalid file handling and detailed error reporting
 * - Generating and exporting file processing statistics

 * Implementation Details:
 * - Relocates invalid files into a subdirectory ("invalid") with assigned reasons
 * - Supports identification of invalid files by checking for empty files,
 *   incorrect file extensions, non-compliance with year filters, or parsing errors
 * - Exports processing results into output files

 * Limitations:
 * - Only textual files ("*.txt") are processed
 * - Requires a pre-defined year filter in file names for validation

 * Thread Safety:
 * - This implementation assumes single-threaded operation and may require adjustments for multi-threaded scenarios
 */
public class ParserService implements IParser {

    private final ISession session;
    private final ILogger logger;
    private final StatsService statistics;
    private final InvalidFileStats invalidFileStats;

    private int totalProcessedFiles = 0;
    private int validFiles = 0;
    private int invalidFiles = 0;

    public ParserService(ISession session, ILogger logger, StatsService statistics) {
        this.session = session;
        this.logger = logger;
        this.statistics = statistics;
        this.invalidFileStats = new InvalidFileStats();
    }

    @Override
    public void parseDocumentsInDirectory(String directoryPath, String accessToken) {
        resetCounters();

        logger.logInfo("Checking the access token...");
        try {
            if (session.isTokenValid(accessToken, session.calculateExpirationDate(0))) {
                logger.logError("Invalid access token.");
                System.out.println("\nYou have an invalid access token!");
                return;
            }
        } catch (SessionManagerException e) {
            throw new RuntimeException(e);
        }

        File directory = new File(directoryPath);
        File invalidDirectory = new File(directoryPath, "invalid");

        createInvalidDirectory(invalidDirectory);
        if (!isValidDirectory(directory)) {
            logger.logError("The specified directory does not exist or is not a directory: " + directoryPath);
            System.out.println("\nTry to specify the parsing directory again..");
            return;
        }

        logger.logInfo("The beginning of parsing files in a directory: " + directoryPath);
        System.out.println("\nThe beginning of parsing files in a directory: " + directoryPath);
        try (Stream<Path> paths = Files.walk(directory.toPath())) {
            List<Path> allFiles = paths.filter(Files::isRegularFile).toList();

            for (Path path : allFiles) {
                handleFileProcessing(path.toFile(), invalidDirectory);
            }

            logProcessingResults();
            statistics.displayStatistics();
            statistics.exportStatisticsToFile(AMOUNT_STATS_FILE_NAME);
            System.out.println("\nStatistics have been saved successfully.");
            logger.logInfo("Statistics have been saved successfully.");

        } catch (IOException e) {
            logger.logError("Error processing files: " + e.getMessage());
        } catch (StatisticsExportException e) {
            logger.logError("Error saving statistics: " + e.getMessage());
        }
    }

    private void resetCounters() {
        totalProcessedFiles = 0;
        validFiles = 0;
        invalidFiles = 0;
    }

    private void createInvalidDirectory(File invalidDirectory) {
        if (!invalidDirectory.exists()) {
            invalidDirectory.mkdirs();
        }
    }

    private boolean isValidDirectory(File directory) {
        return directory.exists() && directory.isDirectory();
    }

    private void handleFileProcessing(File file, File invalidDirectory) {
        totalProcessedFiles++;

        if (isValidFileToProcess(file)) {
            processValidFile(file, invalidDirectory);
        } else {
            moveToInvalidFolder(file, invalidDirectory, determineInvalidReason(file));
        }
    }

    private InvalidFileStats.InvalidReason determineInvalidReason(File file) {
        if (file.length() == 0) {
            return InvalidFileStats.InvalidReason.EMPTY_FILE;
        }
        if (!file.getName().contains(ConfigurationLoader.FILTER_YEAR)) {
            return InvalidFileStats.InvalidReason.WRONG_YEAR;
        }
        if (!file.getName().endsWith(".txt")) {
            return InvalidFileStats.InvalidReason.INCORRECT_EXTENSION;
        }
        return InvalidFileStats.InvalidReason.INCORRECT_CONTENT;
    }

    private boolean isValidFileToProcess(File file) {
        return file.getName().contains(ConfigurationLoader.FILTER_YEAR) &&
                file.getName().endsWith(".txt") &&
                file.length() > 0;
    }

    private void processValidFile(File file, File invalidDirectory) {
        try {
            FileAnalyzer fileAnalyzer = new FileAnalyzer(statistics , logger);

            if (!fileAnalyzer.checkFileContentValidity(file)) {
                moveToInvalidFolder(file, invalidDirectory, InvalidFileStats.InvalidReason.INCORRECT_CONTENT);
                return;
            }

            fileAnalyzer.performFileAnalysis(file);
            validFiles++;
            logger.logInfo("The file has been processed successfully: " + file.getName());

        } catch (Exception e) {
            moveToInvalidFolder(file, invalidDirectory, InvalidFileStats.InvalidReason.PARSING_ERROR);
            logger.logError("Error processing the file " + file.getName() + ": " + e.getMessage());
        }
    }

    private void moveToInvalidFolder(File file, File invalidDirectory, InvalidFileStats.InvalidReason reason) {
        try {
            Path source = file.toPath();
            Path destination = new File(invalidDirectory, file.getName()).toPath();
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);

            invalidFileStats.recordInvalidFile(reason, file.getName());
            invalidFiles++;
            logger.logInfo("The file has been moved to the invalid folder: " + file.getName());
        } catch (IOException e) {
            logger.logError("The file could not be moved " + file.getName() + ": " + e.getMessage());
            try {
                throw new ParserServiceException(ParserServiceException.Type.FILE_MOVE_ERROR, e);
            } catch (ParserServiceException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void logProcessingResults() {
        System.out.println("\n======== FILE PROCESSING RESULTS ========");
        System.out.println("Total files: " + totalProcessedFiles);
        System.out.println("Valid files: " + validFiles);
        System.out.println("Invalid files: " + invalidFiles);

        invalidFileStats.generateDetailedReport();
        invalidFileStats.exportReportToFile(INVALID_STATS_FILE_NAME);
    }
}