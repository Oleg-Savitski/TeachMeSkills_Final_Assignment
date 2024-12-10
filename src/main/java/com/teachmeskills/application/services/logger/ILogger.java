package com.teachmeskills.application.services.logger;
/**
 * Logging interface for application-wide logging functionality.

 * Core Logging Levels:
 * - INFO: Informational messages about application flow
 * - ERROR: Critical error and exception tracking
 * - WARNING: Potential issue or unexpected condition alerts

 * Key Design Principles:
 * - Abstraction of logging mechanism
 * - Simple and clear logging methods
 * - Supports multiple logging implementations

 * Supported Logging Scenarios:
 * - Application startup and shutdown
 * - Method entry/exit tracking
 * - Error and exception logging
 * - Configuration and runtime information

 * Usage Flexibility:
 * - Can be implemented with various logging frameworks
 * - Supports console, file, and remote logging

 * Usage Examples:
 * <pre>
 * ILogger logger = new ConsoleLogger();
 * logger.logInfo("Application started successfully");
 * logger.logError("Critical error occurred: " + exception.getMessage());
 * </pre>
 *
 * Potential Implementations:
 * - Console logging
 * - File-based logging
 * - Remote logging services
 * - Database logging

 * Best Practices:
 * - Keep log messages concise and meaningful
 * - Include contextual information
 * - Avoid logging sensitive data
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [03.12.2024]
 */
public interface ILogger {

    void logInfo(String message);

    void logError(String message);

    void logWarning(String message);
}