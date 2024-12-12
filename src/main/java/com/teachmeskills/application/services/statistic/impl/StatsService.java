package com.teachmeskills.application.services.statistic.impl;

import com.teachmeskills.application.exception.StatisticsExportException;
import com.teachmeskills.application.model.impl.Check;
import com.teachmeskills.application.model.impl.Invoice;
import com.teachmeskills.application.model.impl.Order;
import com.teachmeskills.application.services.logger.ILogger;
import com.teachmeskills.application.services.statistic.IStatsService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
/**
 * A service class that implements the IStatsService interface to manage, track, and log statistics
 * for various business entities such as checks, invoices, and orders.

 * The class supports functionalities to:
 * - Record financial data (amount and file count) for specific entity types.
 * - Display collected statistics in a formatted tabular structure and bar chart representation.
 * - Export statistics to a file in a readable format with error handling.

 * Key Features:
 * - Thread-safe management of statistics using `ConcurrentHashMap` and atomic primitives.
 * - Customizable logging mechanism through an `ILogger` implementation.
 * - Graphical and tabular display of data for better insight.

 * Use Cases:
 * - Monitoring financial transactions by category.
 * - Generating reports for accounting purposes.
 * - Exporting statistical data for external processing or backup.

 * Design Principles:
 * - Follows single responsibility principle by maintaining only statistics-related logic.
 * - Encapsulates the logic for formatting and logging to simplify integrations.
 * - Provides detailed feedback (logging warnings, errors) for unsupported or failed operations.

 * Thread Safety:
 * - Uses `ConcurrentHashMap` and atomic classes (`AtomicReference` and `AtomicInteger`) to ensure
 *   thread-safety when updating statistics across multiple threads.

 * Logging:
 * - Utilizes the `ILogger` interface to log information, warnings, and errors.
 * - Logs when unsupported types are accessed or if file export operations fail.

 * Bar Chart Visualization:
 * - Displays a normalized bar graph in console format to visually represent the relative values
 *   of the statistics.

 * File Export:
 * - Writes formatted statistics data to an external file.
 * - Handles I/O errors gracefully by logging the exception and wrapping it in a custom exception.

 * Constructor:
 * - `StatsService(ILogger logger)`: Initializes the service with a logger and pre-defines types
 *   ("Check", "Invoice", "Order") for tracking.

 * Dependencies:
 * - `IStatsService`: The service interface that this class implements.
 * - `ILogger`: Logging interface for managing log messages.
 */
public class StatsService implements IStatsService {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");
    private static final int MAX_BAR_LENGTH = 50;

    private final Map<String, AtomicReference<Double>> statistics;
    private final Map<String, AtomicInteger> fileCounters;
    private final ILogger logger;

    public StatsService(ILogger logger) {
        this.logger = Objects.requireNonNull(logger, "Logger cannot be null");
        this.statistics = new ConcurrentHashMap<>();
        this.fileCounters = new ConcurrentHashMap<>();
        initializeStatistics();
    }

    private void initializeStatistics() {
        for (String type : new String[]{"Check", "Invoice", "Order"}) {
            statistics.put(type, new AtomicReference<>(0.0));
            fileCounters.put(type, new AtomicInteger(0));
        }
    }

    @Override
    public void recordCheck(Check check) {
        record("Check", check.getTotalAmount());
    }

    @Override
    public void recordInvoice(Invoice invoice) {
        record("Invoice", invoice.getTotalAmount());
    }

    @Override
    public void recordOrder(Order order) {
        record("Order", order.getTotalAmount());
    }

    private void record(String type, double amount) {
        if (!statistics.containsKey(type)) {
            logger.logWarning("Unsupported type: " + type);
            return;
        }
        statistics.get(type).updateAndGet(current -> current + amount);
        fileCounters.get(type).incrementAndGet();
    }

    @Override
    public void displayStatistics() {
        StringBuilder report = new StringBuilder();
        report.append("==================== FINANCIAL STATISTICS ====================\n");
        report.append(String.format("%-15s | %-15s | %-20s%n", "Type", "Total Amount", "Number of Files"));
        report.append("---------------------------------------------------------------\n");

        statistics.forEach((key, value) -> {
            String formattedValue = DECIMAL_FORMAT.format(value.get());
            int fileCount = fileCounters.get(key).get();
            report.append(String.format("%-15s | %-15s | %-20d%n", key, formattedValue, fileCount));
        });

        report.append("================================================================\n");
        System.out.println(report);

        drawConsoleBarChart();
    }

    @Override
    public void exportStatisticsToFile(String filePath) throws StatisticsExportException {
        logger.logInfo("Exporting statistics to file: " + filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, AtomicReference<Double>> entry : statistics.entrySet()) {
                writeStatistic(writer, entry.getKey(), entry.getValue().get(), fileCounters.get(entry.getKey()).get());
            }
            logger.logInfo("Statistics exported successfully to " + filePath);
        } catch (IOException e) {
            String errorMessage = "Failed to export statistics to file: " + filePath + ". Error: " + e.getMessage();
            logger.logError(errorMessage);
            throw new StatisticsExportException(StatisticsExportException.Type.IO_ERROR, e);
        }
    }

    private void writeStatistic(BufferedWriter writer, String key, double value, int fileCount) throws IOException {
        String formattedValue = DECIMAL_FORMAT.format(value);
        writer.write(String.format("The total amount for %s: %s%n", key, formattedValue));
        writer.write(String.format("Number of files for %s: %d%n", key, fileCount));
    }

    public void drawConsoleBarChart() {
        System.out.println("\n===== Graphical Representation =====");

        double maxValue = statistics.values().stream()
                .mapToDouble(AtomicReference::get)
                .max()
                .orElse(0);

        if (maxValue == 0) {
            System.out.println("No data to display a bar chart.");
            return;
        }

        for (Map.Entry<String, AtomicReference<Double>> entry : statistics.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue().get();
            int barLength = (int) ((value / maxValue) * MAX_BAR_LENGTH);
            String bar = "█".repeat(barLength);
            String currencySymbol = getCurrencySymbol(key);
            String formattedValue = DECIMAL_FORMAT.format(value);
            int fileCount = fileCounters.get(key).get();

            System.out.printf("%-10s: %s (%8s %s) [%d files]%n",
                    key, bar, formattedValue, currencySymbol, fileCount);
        }

        System.out.println("----------------------------------------------------------------");
    }

    private String getCurrencySymbol(String type) {
        if (type == null) {
            return "€";
        }
        switch (type) {
            case "Invoice":
                return "$";
            default:
                return "€";
        }
    }
}