package com.teachmeskills.application.utils.constant;

import java.io.File;
/**
 * Centralized constants interface for file paths in the application.

 * Provides standardized file and directory paths for:
 * - Statistical reports
 * - QR Code generation
 * - Logging
 * - Configuration files

 * Key Features:
 * - Absolute file paths for different application resources
 * - Consistent path management across the application
 * - Uses platform-independent {@link File#separator} for log files

 * Path Categories:
 * - {@link #AMOUNT_STATS_FILE_NAME}: Total amount statistics file
 * - {@link #INVALID_STATS_FILE_NAME}: Invalid files report
 * - {@link #QR_CODE_DIR}: QR Code generation directory
 * - {@link #LOG_DIR}: Logging directory
 * - {@link #CONFIG_DIR}: Configuration directory

 * Usage Examples:
 * <pre>
 * // Writing to statistics file
 * try (FileWriter writer = new FileWriter(FilePathConstants.AMOUNT_STATS_FILE_NAME)) {
 *     writer.write(statisticsData);
 * }
 *
 * // Logging
 * Logger.log(FilePathConstants.INFO_LOG_PATH, "Application started");
 * </pre>
 *
 * Recommendations:
 * - Consider using relative paths or configuration-driven paths
 * - Implement path resolution mechanism for different environments

 * Potential Improvements:
 * - Support for environment-specific path configuration
 * - Dynamic path generation

 * Warning:
 * - Hardcoded absolute paths may cause issues in different deployment environments
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [19.11.2024]
 */
public interface FilePathConstants {

    String AMOUNT_STATS_FILE_NAME = "C:\\Java-job\\Project 2024\\TeachMeSkills_Final_Assignment\\source\\data\\information_statistic\\oleg_total_amount.txt";
    String INVALID_STATS_FILE_NAME = "C:\\Java-job\\Project 2024\\TeachMeSkills_Final_Assignment\\source\\data\\information_statistic\\oleg_invalid_files_report.txt";
    String QR_CODE_DIR = "C:\\Java-job\\Project 2024\\TeachMeSkills_Final_Assignment\\source\\data\\qr_codes";
    String LOG_DIR = "C:\\Java-job\\Project 2024\\TeachMeSkills_Final_Assignment\\source\\data\\logs_report";
    String CONFIG_DIR = "C:\\Java-job\\Project 2024\\TeachMeSkills_Final_Assignment\\src\\main\\resources";
    String CONFIG_FILE = CONFIG_DIR + "\\financial-analyzer.properties";
    String QR_CODE_PATH = QR_CODE_DIR + "\\finance_app_qr.png";
    String INFO_LOG_PATH = LOG_DIR + File.separator + "info_log.txt";
    String ERROR_LOG_PATH = LOG_DIR + File.separator + "error_log.txt";
    String WARNING_LOG_PATH = LOG_DIR + File.separator + "warning_log.txt";

}