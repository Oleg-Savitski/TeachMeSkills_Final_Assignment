package com.teachmeskills.application.services.cloud.impl;

import com.teachmeskills.application.exception.CloudStorageException;
import com.teachmeskills.application.services.cloud.ICloudStorageService;
import com.teachmeskills.application.utils.config.ConfigurationLoader;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * AWSCloudStorageService is an implementation of the ICloudStorageService interface,
 * providing functionality to interact with Amazon S3 (Simple Storage Service) for
 * file uploads. This service ensures file validation, error handling, and efficient
 * interaction with AWS resources.

 * Key Responsibilities:
 * - Validate configuration settings for AWS, such as credentials and bucket name.
 * - Establish connection to AWS S3 using the provided credentials and region.
 * - Upload a single file or multiple files to the specified S3 bucket.
 * - Handle and classify errors, including configuration issues, file access problems,
 *   network errors, and upload failures.

 * Core Features:
 * - Automatic generation of unique keys for uploaded files to ensure proper organization.
 * - Detailed logging of successful uploads including file name, file size, upload time,
 *   S3 path, bucket, and region.
 * - Thread-safe, reusable S3 client for efficient operations.

 * Exceptions:
 * Throws CloudStorageException in cases such as invalid configuration, file access issues,
 * network failures, or upload errors. The exception type is categorized for precise handling.
 */
public class AWSCloudStorageService implements ICloudStorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final Region region;

    public AWSCloudStorageService() throws CloudStorageException {
        validateConfiguration();

        this.bucketName = ConfigurationLoader.AWS_S3_BUCKET_NAME;
        this.region = Region.of(ConfigurationLoader.AWS_REGION_NAME);

        this.s3Client = createS3Client();
    }

    private void validateConfiguration() throws CloudStorageException {
        if (isBlank(ConfigurationLoader.AWS_ACCESS_KEY) ||
                isBlank(ConfigurationLoader.AWS_SECRET_KEY)) {
            throw new CloudStorageException(
                    CloudStorageException.Type.CONFIGURATION_ERROR,
                    new IllegalArgumentException("AWS credentials cannot be empty!")
            );
        }

        if (isBlank(ConfigurationLoader.AWS_S3_BUCKET_NAME)) {
            throw new CloudStorageException(
                    CloudStorageException.Type.CONFIGURATION_ERROR,
                    new IllegalArgumentException("The S3 bucket name cannot be empty!")
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private S3Client createS3Client() throws CloudStorageException {
        try {
            AwsBasicCredentials credentials = AwsBasicCredentials.create(
                    ConfigurationLoader.AWS_ACCESS_KEY,
                    ConfigurationLoader.AWS_SECRET_KEY
            );

            return S3Client.builder()
                    .region(region)
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
        } catch (Exception e) {
            throw new CloudStorageException(
                    CloudStorageException.Type.CONFIGURATION_ERROR,
                    new RuntimeException("Failed to create S3 client!", e)
            );
        }
    }

    @Override
    public boolean uploadStatisticsFiles(List<File> files) throws CloudStorageException {
        if (files == null || files.isEmpty()) {
            throw new CloudStorageException(
                    CloudStorageException.Type.FILE_ACCESS_ERROR,
                    new IllegalArgumentException("The file list is empty!")
            );
        }

        boolean allUploaded = true;
        for (File file : files) {
            allUploaded &= uploadStatisticsFile(file);
        }
        return allUploaded;
    }

    @ Override
    public boolean uploadStatisticsFile(File file) throws CloudStorageException {
        validateFile(file);

        try {
            String key = generateUniqueKey(file);
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            long startTime = System.currentTimeMillis();

            s3Client.putObject(request, RequestBody.fromFile(file));

            long uploadTime = System.currentTimeMillis() - startTime;
            logUploadSuccess(file, key, uploadTime);

            return true;

        } catch (Exception e) {
            I_LOGGER.logError("Error uploading a file to S3: " + e.getMessage());
            CloudStorageException.Type errorType = determineErrorType(e);
            throw new CloudStorageException(errorType, e);
        }
    }

    private void validateFile(File file) throws CloudStorageException {
        if (file == null) {
            throw new CloudStorageException(
                    CloudStorageException.Type.FILE_ACCESS_ERROR,
                    new NullPointerException("The file cannot be null!")
            );
        }

        if (!file.exists()) {
            throw new CloudStorageException(
                    CloudStorageException.Type.FILE_ACCESS_ERROR,
                    new IllegalArgumentException("The file does not exist: " + file.getPath())
            );
        }

        if (!file.canRead()) {
            throw new CloudStorageException(
                    CloudStorageException.Type.FILE_ACCESS_ERROR,
                    new SecurityException("The file cannot be read: " + file.getPath())
            );
        }

        if (file.length() == 0) {
            throw new CloudStorageException(
                    CloudStorageException.Type.FILE_ACCESS_ERROR,
                    new IllegalStateException("The file is empty: " + file.getPath())
            );
        }
    }

    private CloudStorageException.Type determineErrorType(Exception e) {
        String errorMessage = e.getMessage().toLowerCase();

        if (errorMessage.contains("network") ||
                errorMessage.contains("connection") ||
                errorMessage.contains("timeout")) {
            return CloudStorageException.Type.NETWORK_ERROR;
        }

        return CloudStorageException.Type.UPLOAD_FAILED;
    }

    private String generateUniqueKey(File file) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = now.format(formatter);

        return String.format("statistics/%s_%s", timestamp, file.getName());
    }

    private void logUploadSuccess(File file, String key, long uploadTime) {
        System.out.println("────────────────────────────────────────────");

        String successMessage = String.format(
                "✅ AWS S3 Upload Success%n" +
                        "📄 File: %s%n" +
                        "🌐 S3 Path: %s%n" +
                        "📊 Size: %d bytes%n" +
                        "⏱️ Upload Time: %d ms%n" +
                        "🗃️ Bucket: %s%n" +
                        "🌍 Region: %s",
                file.getName(),
                key,
                file.length(),
                uploadTime,
                bucketName,
                region.id());

        System.out.println(successMessage);

        System.out.println("────────────────────────────────────────────");
    }
}