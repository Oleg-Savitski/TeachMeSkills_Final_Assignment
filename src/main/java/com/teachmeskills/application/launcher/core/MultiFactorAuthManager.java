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
 * Manages multi-factor authentication process, specifically handling QR code generation
 * and secret key management for two-factor authentication.

 * Key Responsibilities:
 * - Generate and manage two-factor authentication secret keys
 * - Create QR codes for authentication
 * - Persist authentication configuration

 * Authentication Workflow:
 * 1. Check for existing secret key
 * 2. Generate new secret key if not exists
 * 3. Create QR code for authentication app
 * 4. Store secret key in configuration

 * Security Considerations:
 * - Secure secret key generation
 * - Configuration file protection
 * - Centralized authentication management

 * Supported Authentication Platforms:
 * - Microsoft Authenticator
 * - Google Authenticator

 * Configuration Management:
 * - Uses Properties for key storage
 * - Persistent configuration file
 * - Secure key generation

 * Example Usage:
 * <pre>
 * // Retrieve or generate QR code for authentication
 * String secretKey = MultiFactorAuthManager.retrieveOrGenerateQRCode();
 * </pre>
 *
 * Design Patterns:
 * - Singleton-like static utility class
 * - Configuration management
 * - Secure key generation
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [01.12.2024]
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