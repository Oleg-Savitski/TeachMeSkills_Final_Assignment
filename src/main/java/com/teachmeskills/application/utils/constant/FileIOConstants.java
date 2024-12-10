package com.teachmeskills.application.utils.constant;
/**
 * Constants interface for File Input/Output operations in the application.

 * Provides standardized configuration values for file handling:
 * - Buffer size for efficient I/O operations
 * - Maximum allowed file size

 * Purpose:
 * - Centralize file-related configuration constants
 * - Ensure consistent I/O parameters across the application

 * Key Constants:
 * - {@link #BUFFER_SIZE}: Recommended buffer size for file streams
 * - {@link #MAX_FILE_SIZE_MB}: Maximum file size limit in megabytes

 * Usage Examples:
 * <pre>
 * // Using buffer size in file operations
 * byte[] buffer = new byte[FileIOConstants.BUFFER_SIZE];
 *
 * // Checking file size before processing
 * if (file.length() > FileIOConstants.MAX_FILE_SIZE_MB * 1024 * 1024) {
 *     throw new FileSizeLimitExceededException("File is too large");
 * }
 * </pre>
 *
 * Best Practices:
 * - Use these constants to maintain consistency
 * - Adjust values based on system resources and performance requirements

 * Recommendations:
 * - Buffer size optimized for most standard file operations
 * - File size limit provides a basic security mechanism
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [28.11.2024]
 */
public interface FileIOConstants {

    int BUFFER_SIZE = 8192;
    int MAX_FILE_SIZE_MB = 100;

}