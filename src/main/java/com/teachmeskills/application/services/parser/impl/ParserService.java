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
 * Comprehensive document parsing service for processing files in a specified directory.

 * Key Responsibilities:
 * - Validate and parse documents from a given directory
 * - Authenticate access using session management
 * - Perform file analysis and validation
 * - Track and manage file processing statistics

 * Core Features:
 * - Access token validation
 * - File type and content validation
 * - Automatic invalid file handling
 * - Detailed statistics and reporting

 * Processing Workflow:
 * 1. Validate access token
 * 2. Scan directory for files
 * 3. Process each file individually
 * 4. Move invalid files to a separate directory
 * 5. Generate processing statistics

 * Validation Criteria:
 * - File year matching configuration
 * - File extension (.txt)
 * - Non-empty file
 * - Content validity

 * Error Handling:
 * - Logs processing errors
 * - Moves invalid files to a dedicated folder
 * - Generates comprehensive error reports

 * Dependencies:
 * - Session management
 * - Logging service
 * - Statistics tracking
 * - File analysis

 * Usage Example:
 * <pre>
 * ParserService parser = new ParserService(session, logger, statsService);
 * parser.parseDocumentsInDirectory("/path/to/documents", "access-token");
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [26.11.2024]
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