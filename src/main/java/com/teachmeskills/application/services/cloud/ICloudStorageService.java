package com.teachmeskills.application.services.cloud;

import com.teachmeskills.application.exception.CloudStorageException;

import java.io.File;
import java.util.List;
/**
 * Interface for cloud storage operations focused on statistics file management.

 * Key Characteristics:
 * - Abstraction of cloud storage upload mechanisms
 * - Support for single and multiple file uploads
 * - Robust error handling for cloud storage interactions

 * Core Capabilities:
 * - Upload individual statistics files
 * - Upload collections of statistics files
 * - Provide upload status feedback

 * Usage Scenarios:
 * - Archiving analytical data
 * - Backup of statistical reports
 * - Remote storage of performance metrics

 * Error Handling:
 * - Throws CloudStorageException for upload failures
 * - Provides boolean status of upload operations

 * Design Considerations:
 * - Platform-independent file upload interface
 * - Supports various cloud storage providers
 * - Minimal coupling with specific storage implementations

 * Usage Examples:
 * <pre>
 * ICloudStorageService cloudService = new AWSCloudStorageService();
 *
 * // Upload single file
 * File statisticsReport = new File("report.csv");
 * boolean singleUploadStatus = cloudService.uploadStatisticsFile(statisticsReport);
 *
 * // Upload multiple files
 * List<File> reports = Arrays.asList(report1, report2, report3);
 * boolean multiUploadStatus = cloudService.uploadStatisticsFiles(reports);
 * </pre>
 *
 * Potential Implementations:
 * - Amazon S3 Cloud Storage
 * - Google Cloud Storage
 * - Azure Blob Storage
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [30.11.2024]
 */
public interface ICloudStorageService {

    boolean uploadStatisticsFile(File file) throws CloudStorageException;

    boolean uploadStatisticsFiles(List<File> files) throws CloudStorageException;
}