package com.teachmeskills.application.services.cloud;

import com.teachmeskills.application.exception.CloudStorageException;

import java.io.File;
import java.util.List;
/**
 * Interface for cloud storage operations, providing methods to upload files
 * to a cloud storage service. Implementations of this interface are expected
 * to handle the specifics of interacting with different cloud storage platforms
 * and managing data transfer.

 * Responsibilities:
 * - Upload a single statistics file to cloud storage.
 * - Upload multiple statistics files to cloud storage.
 * - Handle exceptions related to cloud storage operations, such as configuration
 *   issues, file access problems, or upload failures.
 */
public interface ICloudStorageService {

    boolean uploadStatisticsFile(File file) throws CloudStorageException;

    boolean uploadStatisticsFiles(List<File> files) throws CloudStorageException;
}