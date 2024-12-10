package com.teachmeskills.application.services.statistic;

import com.teachmeskills.application.exception.StatisticsExportException;
import com.teachmeskills.application.model.impl.Check;
import com.teachmeskills.application.model.impl.Invoice;
import com.teachmeskills.application.model.impl.Order;
/**
 * Service interface for recording and managing business statistics.

 * Provides functionality to:
 * - Record various business entities
 * - Display current statistics
 * - Export statistics to external files

 * Supports tracking of:
 * - Checks
 * - Invoices
 * - Orders

 * Usage example:
 * <pre>
 * IStatsService statsService = new StatsServiceImpl();
 * statsService.recordCheck(check);
 * statsService.recordInvoice(invoice);
 * statsService.displayStatistics();
 * statsService.exportStatisticsToFile("stats.txt");
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [20.11.2024]
 */
public interface IStatsService {

    void recordCheck(Check check);

    void recordInvoice(Invoice invoice);

    void recordOrder(Order order);

    void displayStatistics();

    void exportStatisticsToFile(String filePath) throws StatisticsExportException;
}