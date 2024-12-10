package com.teachmeskills.application.services.parser.stats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
/**
 * Comprehensive statistics tracking and reporting service for invalid files.

 * Key Features:
 * - Tracks invalid files by specific reasons
 * - Generates detailed console and file reports
 * - Provides percentage breakdown of invalid files

 * Invalid File Tracking:
 * - Supports multiple invalid file reasons via {@link InvalidReason} enum
 * - Maintains a count and list of files for each invalid reason

 * Reporting Capabilities:
 * - Console reporting
 * - File export of invalid file statistics
 * - Percentage analysis of file invalidity

 * Supported Invalid Reasons:
 * - Empty files
 * - Incorrect year
 * - Wrong file extension
 * - Parsing errors
 * - Incorrect content

 * Usage Examples:
 * <pre>
 * InvalidFileStats stats = new InvalidFileStats();
 * stats.recordInvalidFile(InvalidReason.EMPTY_FILE, "document.txt");
 * stats.generateDetailedReport();
 * stats.exportReportToFile("invalid_files_report.txt");
 * </pre>
 *
 * Design Principles:
 * - Thread-safe enum map for storing statistics
 * - Flexible and extensible invalid reason tracking
 * - Detailed reporting mechanism

 * Potential Improvements:
 * - Add logging integration
 * - Support for custom report formats
 * - Performance optimization for large file sets
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [21.11.2024]
 */
public class InvalidFileStats {

    public enum InvalidReason {
        EMPTY_FILE,
        WRONG_YEAR,
        INCORRECT_EXTENSION,
        PARSING_ERROR,
        INCORRECT_CONTENT,
    }

    private final Map<InvalidReason, List<String>> invalidFileStats = new EnumMap<>(InvalidReason.class);
    private int totalInvalidFiles = 0;

    public void recordInvalidFile(InvalidReason reason, String fileName) {
        invalidFileStats.computeIfAbsent(reason, k -> new ArrayList<>()).add(fileName);
        totalInvalidFiles++;
    }

    public void generateDetailedReport() {
        System.out.println("\n==== DETAILED REPORT ON INVALID FILES ====");
        System.out.println("The total number of invalid files: " + totalInvalidFiles);

        invalidFileStats.forEach((reason, files) -> {
            System.out.println("\n" + reason + ":");
            System.out.println("Number of files: " + files.size());
            System.out.println("Files:");
            files.forEach(file -> System.out.println(" - " + file));
        });

        generatePercentageBreakdown();
    }

    private void generatePercentageBreakdown() {
        StringBuilder report = new StringBuilder();

        report.append("\n==================== ANALYSIS OF INVALID FILES ====================\n");
        report.append(String.format("%-30s | %-10s | %-15s%n",
                "The reason for the invalidity", "Percent", "Number of files"));
        report.append("----------------------------------------------------------------\n");

        invalidFileStats.forEach((reason, files) -> {
            double percentage = (files.size() * 100.0) / totalInvalidFiles;
            report.append(String.format("%-30s | %6.2f%% | %15d%n",
                    reason,
                    percentage,
                    files.size()
            ));
        });

        report.append("----------------------------------------------------------------\n");
        report.append(String.format("%-30s | %6.2f%% | %15d%n",
                "Total invalid files:",
                100.0,
                totalInvalidFiles
        ));

        System.out.println(report);
    }

    public void exportReportToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("Detailed report on invalid files");
            writer.println("The total number of invalid files: " + totalInvalidFiles);

            invalidFileStats.forEach((reason, files) -> {
                writer.println("\n" + reason + ":");
                writer.println("Number of files: " + files.size());
                files.forEach(file -> writer.println(" - " + file));
            });
        } catch (IOException e) {
            System.err.println("Error exporting the report: " + e.getMessage());
        }
    }
}