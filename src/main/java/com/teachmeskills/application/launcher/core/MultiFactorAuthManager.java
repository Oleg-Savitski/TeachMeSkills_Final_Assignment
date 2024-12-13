package com.teachmeskills.application.launcher.core;

import com.google.zxing.WriterException;
import com.teachmeskills.application.services.authentication.twofactor.TwoFactorAuthentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static com.teachmeskills.application.utils.constant.FilePathConstants.CONFIG_FILE;
import static com.teachmeskills.application.utils.constant.FilePathConstants.QR_CODE_PATH;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * The MultiFactorAuthManager class is responsible for managing multi-factor authentication
 * for applications. It provides functionality to retrieve or generate QR codes
 * for enabling two-factor authentication using tools like Google Authenticator or Microsoft Authenticator.

 * Responsibilities:
 * - Verify whether a pre-existing secret key exists and retrieve it.
 * - Generate a new QR code for two-factor authentication and save a corresponding secret key, if needed.
 * - Handle errors or exceptions during the QR code generation and configuration process.

 * Methods:
 * - retrieveOrGenerateQRCode: This method retrieves an existing two-factor authentication secret key
 *   or generates a new QR code if the key does not exist. It also ensures the new secret key is stored
 *   in the configuration file.

 * Error Handling:
 * - Logs errors using the I_LOGGER implementation in cases where QR code creation or retrieval fails.
 * - Wraps and rethrows exceptions as runtime exceptions to indicate failures.

 * Dependencies:
 * - TwoFactorAuthentication: Used for generating the secret key, creating barcodes, and saving QR codes.
 * - I_LOGGER: Used for logging error messages.
 * - Properties and file I/O operations to store and retrieve secret keys and configurations.

 * Constraints:
 * - The QR code is saved in a predefined file path.
 * - The email and app-specific label for the QR code generation are hardcoded.
 */
public class MultiFactorAuthManager {
    public static String retrieveOrGenerateQRCode() {
        Properties props = new Properties();
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    props.load(fis);
                    String existingSecretKey = props.getProperty("secret_key");
                    if (existingSecretKey != null) {
                        return existingSecretKey;
                    }
                }
            }
            return createNewQRCode(props);
        } catch (Exception e) {
            I_LOGGER.logError("Error creating/uploading a QR code: " + e.getMessage());
            throw new RuntimeException("Failed to create/upload a QR code.");
        }
    }

    private static String createNewQRCode(Properties props) throws WriterException, IOException {
        System.out.println("===== SETTING UP TWO-FACTOR AUTHENTICATION =====");
        System.out.println("1. Open Microsoft Authenticator.");
        System.out.println("2. Scan the QR code in the app.");

        String secretKey = TwoFactorAuthentication.generateSecretKey();
        String email = "www.legendog@mail.ru";
        String barCodeUrl = TwoFactorAuthentication.getGoogleAuthenticatorBarCode(
                secretKey, email, "TeachMeSkills_C32_app_pro"
        );

        TwoFactorAuthentication.createQRCode(barCodeUrl, QR_CODE_PATH, 400, 400);
        props.setProperty("secret_key", secretKey);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Two-Factor Authentication Secret Key");
        }

        System.out.println("The QR code is saved along the way: " + QR_CODE_PATH);
        System.out.println("================================================");

        return secretKey;
    }
}