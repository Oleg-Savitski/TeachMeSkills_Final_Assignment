package com.teachmeskills.application.launcher.core;

import com.teachmeskills.application.exception.CloudStorageException;
import com.teachmeskills.application.services.cloud.impl.AWSCloudStorageService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.teachmeskills.application.launcher.core.AuthenticationGateway.scanner;
import static com.teachmeskills.application.utils.constant.FilePathConstants.AMOUNT_STATS_FILE_NAME;
import static com.teachmeskills.application.utils.constant.FilePathConstants.INVALID_STATS_FILE_NAME;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * The CloudDataUploader class is responsible for handling the process of uploading
 * statistical data files to cloud storage. It ensures that the files are prepared
 * and validated before upload and handles various scenarios, including user input
 * prompts and errors that might occur during the upload process.

 * This class interacts with an AWS cloud storage service to manage file uploads.
 * It also logs appropriate messages for success, failure, or any unexpected errors
 * using a logging interface.
 */
public class CloudDataUploader {
    public void uploadStatisticsToCloud() {

        if (!promptUserForUpload()) {
            System.out.println("\nYou have postponed uploading statistics to the cloud storage.");
            I_LOGGER.logInfo("Statistics upload to cloud storage was skipped.");
            return;
        }

        try {
            long startTime = System.currentTimeMillis();
            AWSCloudStorageService cloudStorageService = new AWSCloudStorageService();

            List<File> filesToUpload = prepareFilesForUpload();

            if (!validateFiles(filesToUpload)) {
                System.out.println("\nThe files do not exist. Check their presence in the specified directory.");
                I_LOGGER.logError("One or more files do not exist. Upload cannot proceed.");
                return;
            }

            boolean uploadSuccess = cloudStorageService.uploadStatisticsFiles(filesToUpload);
            long uploadTime = System.currentTimeMillis() - startTime;

            logUploadResults(uploadSuccess, uploadTime);

        } catch (CloudStorageException e) {
            handleCloudStorageException(e);
        } catch (Exception e) {
            System.out.println("\nUnexpected error when working with cloud storage. Try again later..");
            I_LOGGER.logError("Unexpected error when working with cloud storage: " +
                    e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    private boolean promptUserForUpload() {
        System.out.print("\nDo you want to upload statistics to cloud storage? (1 -> Yes, 2 -> No): ");
        String choice = scanner.nextLine().trim();

        while (!choice.equals("1") && !choice.equals("2")) {
            System.out.print("\nInvalid input. Please enter 1 -> Yes or 2 -> No: ");
            choice = scanner.nextLine().trim();
        }

        return choice.equals("1");
    }

    private List<File> prepareFilesForUpload() {
        List<File> filesToUpload = new ArrayList<>();
        filesToUpload.add(new File(AMOUNT_STATS_FILE_NAME));
        filesToUpload.add(new File(INVALID_STATS_FILE_NAME));
        return filesToUpload;
    }

    private boolean validateFiles(List<File> files) {
        return files.stream().allMatch(File::exists);
    }

    private void logUploadResults(boolean uploadSuccess, long uploadTime) {
        if (uploadSuccess) {
            System.out.println("All files have been successfully uploaded to the cloud storage in " + uploadTime + " ms.");
            I_LOGGER.logInfo("All files have been successfully uploaded to the cloud storage in " + uploadTime + " ms.");
        } else {
            System.out.println("Some files were not uploaded.");
            I_LOGGER.logInfo("Some files were not uploaded.");
        }
    }

    private void handleCloudStorageException(CloudStorageException e) {
        switch (e.getType()) {
            case FILE_ACCESS_ERROR:
                I_LOGGER.logError("The problem with the statistics file: " + e.getMessage());
                break;
            case CONFIGURATION_ERROR:
                I_LOGGER.logError("Incorrect AWS configuration: " + e.getMessage());
                break;
            case NETWORK_ERROR:
                I_LOGGER.logError("Network error during upload: " + e.getMessage());
                break;
            case UPLOAD_FAILED:
                I_LOGGER.logError("Failed to upload statistics: " + e.getMessage());
                break;
        }
    }
}