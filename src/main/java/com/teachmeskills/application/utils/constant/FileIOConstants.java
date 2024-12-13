package com.teachmeskills.application.utils.constant;
/**
 * Centralized interface for file input/output constants in the application.

 * Provides standardized constants for managing file reading and writing operations.

 * Key Features:
 * - Predefined buffer size for file I/O operations to optimize performance.
 * - Maximum file size limit to ensure efficient resource management and to prevent issues
 *   with excessively large files.

 * Constants:
 * - BUFFER_SIZE: The size of the buffer (in bytes) used for file reading and writing.
 * - MAX_FILE_SIZE_MB: Maximum allowed file size in megabytes for processing within the application.

 * Recommendations:
 * - Use BUFFER_SIZE in I/O streams to maintain consistent performance across file operations.
 * - Validate file size against MAX_FILE_SIZE_MB before processing to avoid memory issues.

 * Potential Improvements:
 * - Make these constants configurable via an external configuration file.
 * - Add more constants related to file encoding or I/O timeout settings.
 */
public interface FileIOConstants {

    int BUFFER_SIZE = 8192;
    int MAX_FILE_SIZE_MB = 100;

}