package com.teachmeskills.application.services.logger;
/**
 * The ILogger interface provides core methods for logging messages at various levels,
 * including informational, warning, and error messages. Implementations of this interface
 * should handle the logging behavior, such as writing messages to files, consoles, or other
 * logging destinations.

 * Methods:
 * - logInfo(String message): Logs an informational message.
 * - logError(String message): Logs an error message.
 * - logWarning(String message): Logs a warning message.
 */
public interface ILogger {

    void logInfo(String message);

    void logError(String message);

    void logWarning(String message);
}