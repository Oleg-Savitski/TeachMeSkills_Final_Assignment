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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
/**
 * Implementation of the IStatsService for tracking and managing financial statistics.

 * Key Features:
 * - Thread-safe statistical tracking using ConcurrentHashMap
 * - Records statistics for Checks, Invoices, and Orders
 * - Supports displaying statistics in console
 * - Provides graphical console bar chart representation
 * - Exports statistics to external files

 * Concurrency: Fully thread-safe with atomic operations

 * Usage example:
 * <pre>
 * ILogger logger = new LoggerService();
 * IStatsService statsService = new StatsService(logger);
 *
 * statsService.recordCheck(check);
 * statsService.recordInvoice(invoice);
 * statsService.displayStatistics();
 * statsService.exportStatisticsToFile("financial_stats.txt");
 * </pre>
 *
 * The service tracks:
 * - Total amount for each entity type
 * - Number of files processed for each entity type
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [20.11.2024]
 * @see IStatsService
 * @see ILogger
 */
public class StatsService implements IStatsService {

    private final Map<String, AtomicReference<Double>> statistics;
    private final Map<String, AtomicInteger> fileCounters;
    private final ILogger logger;

    public StatsService(ILogger logger) {
        this.logger = logger;

        statistics = new ConcurrentHashMap<>();
        fileCounters = new ConcurrentHashMap<>();

        initializeStatistics();
    }

    private void initializeStatistics() {
        statistics.put("Check", new AtomicReference<>(0.0));
        statistics.put("Invoice", new AtomicReference<>(0.0));
        statistics.put("Order", new AtomicReference<>(0.0));

        fileCounters.put("Check", new AtomicInteger(0));
        fileCounters.put("Invoice", new AtomicInteger(0));
        fileCounters.put("Order", new AtomicInteger(0));
    }

    @Override
    public void recordCheck(Check check) {
        updateTotal("Check", check.getTotalAmount());
    }

    @Override
    public void recordInvoice(Invoice invoice) {
        updateTotal("Invoice", invoice.getTotalAmount());
    }

    @Override
    public void recordOrder(Order order) {
        updateTotal("Order", order.getTotalAmount());
    }

    private void updateTotal(String type, double amount) {
        statistics.get(type).updateAndGet(current -> current + amount);
        fileCounters.get(type).incrementAndGet();
    }

    @Override
    public void displayStatistics() {
        StringBuilder report = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.00");
        report.append("==================== FINANCIAL STATISTICS ====================\n");
        report.append(String.format("%-15s | %-15s | %-20s%n", "Type", "The total amount", "Number of files"));
        report.append("---------------------------------------------------------------\n");

        statistics.forEach((key, value) -> {
            String formattedValue = df.format(value.get());
            int fileCount = fileCounters.get(key).get();
            report.append(String.format("%-15s | %-15s | %-20d%n", key, formattedValue, fileCount));
        });

        report.append("----------------------------------------------------------------\n");
        report.append("================================================================\n");
        System.out.println(report);
        drawConsoleBarChart();
    }

    @Override
    public void exportStatisticsToFile(String filePath) throws StatisticsExportException {
        logger.logInfo("Exporting statistics to a file -> " + filePath);
        System.out.println("\nThe path for exporting statistics -> " +filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, AtomicReference<Double>> entry : statistics.entrySet()) {
                String key = entry.getKey();
                Double value = entry.getValue().get();
                writeStatistic(writer, key, value, fileCounters.get(key).get());
            }
            logger.logInfo("Statistics have been successfully exported.");
            System.out.println("\nStatistics have been successfully exported!");
        } catch (IOException e) {
            String errorMessage = "Failed to save statistics to a file: " + e.getMessage();
            logger.logError(errorMessage);
            throw new StatisticsExportException(StatisticsExportException.Type.IO_ERROR, e);
        }
    }

    private void writeStatistic(BufferedWriter writer, String key, double value, int fileCount) {
        try {
            String formattedValue = new DecimalFormat("#.00").format(value);
            writer.write(String.format("The total amount %s: %s%n", key, formattedValue));
            writer.write(String.format("Number of files %s: %d%n", key, fileCount));
        } catch (IOException e) {
            logger.logError("Error recording statistics for " + key + ": " + e.getMessage());
        }
    }

    public void drawConsoleBarChart() {
        System.out.println("\n===== Graphical representation =====");

        final int MAX_BAR_LENGTH = 50;
        double maxValue = statistics.values().stream()
                .mapToDouble(AtomicReference::get)
                .max()
                .orElse(0);

        DecimalFormat df = new DecimalFormat("#.00");

        for (Map.Entry<String, AtomicReference<Double>> entry : statistics.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue().get();

            int barLength = (int) ((value / maxValue) * MAX_BAR_LENGTH);
            String bar = "█".repeat(barLength);
            String formattedValue = df.format(value);
            int fileCount = fileCounters.get(key).get();

            String currencySymbol = key.equals("Invoice") ? "$" : "€";

            System.out.printf("%-10s: %s (%8s %s) [%d files]%n",
                    key, bar, formattedValue, currencySymbol, fileCount);
        }

        System.out.println("----------------------------------------------------------------\n");
    }
}