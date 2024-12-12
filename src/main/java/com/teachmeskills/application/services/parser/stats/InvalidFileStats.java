package com.teachmeskills.application.services.parser.stats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
/**
 * The {@code InvalidFileStats} class is responsible for maintaining statistics
 * related to invalid files encountered during processing. It tracks the reasons
 * why files are invalid and supports reporting this data in various formats.

 * Key Features:
 * - Records invalid files along with their associated reasons.
 * - Maintains a count of the total invalid files.
 * - Provides detailed reporting of invalid file statistics, including file details
 *   and percentage breakdown by reason.
 * - Supports exporting the invalid file report to an external file.

 * Thread Safety:
 * - All methods that modify or access shared state are synchronized to ensure
 *   thread safety in multi-threaded environments.

 * Usage Scenarios:
 * - Tracking and analyzing invalid files during batch processing or document parsing.
 * - Providing detailed logs and statistical breakdowns for debugging or auditing purposes.

 * Design Considerations:
 * - Uses {@link EnumMap} to efficiently store and retrieve invalid file reasons.
 * - Relies on the {@link InvalidReason} enumeration to define the possible reasons
 *   for file invalidity.
 * - Exceptions are used to validate input and ensure proper usage.
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

    public synchronized void recordInvalidFile(InvalidReason reason, String fileName) {
        if (reason == null || fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid reason or file name provided");
        }
        invalidFileStats.computeIfAbsent(reason, k -> new ArrayList<>()).add(fileName);
        totalInvalidFiles++;
    }

    public synchronized void generateDetailedReport() {
        System.out.println("\n==== DETAILED REPORT ON INVALID FILES ====");
        System.out.println("The total number of invalid files: " + totalInvalidFiles);

        invalidFileStats.forEach((reason, files) -> {
            System.out.println("\n" + reason + ":");
            System.out.println("Number of files: " + files.size());
            if (!files.isEmpty()) {
                System.out.println("Files:");
                files.forEach(file -> System.out.println(" - " + file));
            }
        });

        generatePercentageBreakdown();
    }

    private synchronized void generatePercentageBreakdown() {
        if (totalInvalidFiles == 0) {
            System.out.println("\nNo invalid files to analyze.");
            return;
        }

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

    public synchronized void exportReportToFile(String filePath) {
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