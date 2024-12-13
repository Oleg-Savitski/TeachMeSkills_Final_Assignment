package com.teachmeskills.application.utils.constant;

import java.io.File;
/**
 * Centralized interface for file path constants used in the application.

 * Provides a collection of predefined file and directory paths for managing various resources,
 * such as configuration files, logs, QR codes, and statistical data.

 * Key Features:
 * - Absolute paths to commonly used files and directories.
 * - Centralized management of file path definitions for consistency.
 * - Derived constants for paths dependent on directory structure.

 * File and Directory Path Constants:
 * - `AMOUNT_STATS_FILE_NAME`: Path to store total amount statistics.
 * - `INVALID_STATS_FILE_NAME`: Path for invalid files report.
 * - `QR_CODE_DIR`: Directory containing QR code files.
 * - `LOG_DIR`: Directory for storing various log files.
 * - `CONFIG_DIR`: Directory for configuration resources.
 * - `CONFIG_FILE`: Path to the main configuration properties file.
 * - `QR_CODE_PATH`: Path to the QR code image file.
 * - `INFO_LOG_PATH`: Path to the info-level log file.
 * - `ERROR_LOG_PATH`: Path to the error-level log file.
 * - `WARNING_LOG_PATH`: Path to the warning-level log file.

 * Recommendations:
 * - Replace hardcoded file paths with configurable paths using external configuration.
 * - Ensure paths are accessible and permissions are correctly set for read/write operations.
 * - Regularly validate file and directory structures during deployment.

 * Potential Improvements:
 * - Dynamically resolve paths relative to the project's root folder to enhance portability.
 * - Use environment variables or configuration files to define paths for better flexibility.
 * - Add constants for additional directories or files as needed.
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