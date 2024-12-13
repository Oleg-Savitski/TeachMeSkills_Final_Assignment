package com.teachmeskills.application.services.statistic;

import com.teachmeskills.application.exception.StatisticsExportException;
import com.teachmeskills.application.model.impl.Check;
import com.teachmeskills.application.model.impl.Invoice;
import com.teachmeskills.application.model.impl.Order;
/**
 * The IStatsService interface provides functionality for managing and processing
 * a variety of statistical data related to checks, invoices, and orders. It also
 * supports exporting statistics to external files and displaying them.

 * Methods:
 * - recordCheck(Check check): Records statistical data associated with a check object.
 * - recordInvoice(Invoice invoice): Records statistical data associated with an invoice object.
 * - recordOrder(Order order): Records statistical data associated with an order object.
 * - displayStatistics(): Displays collected statistics on the console or relevant output medium.
 * - exportStatisticsToFile(String filePath): Exports the collected statistics to a file,
 *   throwing a StatisticsExportException in case of failure.
 */
public interface IStatsService {

    void recordCheck(Check check);

    void recordInvoice(Invoice invoice);

    void recordOrder(Order order);

    void displayStatistics();

    void exportStatisticsToFile(String filePath) throws StatisticsExportException;
}