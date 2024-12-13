package com.teachmeskills.application.services.encryption.impl;

import com.teachmeskills.application.exception.EncryptionException;
import com.teachmeskills.application.services.encryption.IEncryption;
import com.teachmeskills.application.services.encryption.security.SecureRandomSaltGenerator;
import com.teachmeskills.application.services.logger.ILogger;

import java.util.Base64;

import static com.teachmeskills.application.utils.config.ConfigurationLoader.SALT_LENGTH;
/**
 * A service class implementing the IEncryption interface to provide encryption and decryption
 * functionalities using Base64 encoding and cryptographic salting.
 * It supports secure data processing by adding salt during encryption and extracting it during decryption.

 * Responsibilities:
 * - Encrypt a given input string by Base64 encoding and appending a randomly generated salt.
 * - Decrypt an encrypted string by removing the salt and decoding the Base64 content.
 * - Handle decryption errors with appropriate logging and exception throwing.

 * Dependencies:
 * - SecureRandomSaltGenerator: Responsible for generating cryptographic salts.
 * - ILogger: Used for logging messages, including errors and warnings.

 * Error Handling:
 * - Throws EncryptionException for invalid inputs or decryption failures.
 * - Logs error messages using the ILogger instance when exceptions occur.

 * Design Notes:
 * - Relies on constants for console log formatting with colors.
 * - Implements the IEncryption interface, ensuring contract compliance for encryption and decryption methods.
 * - Uses secure processes for salt generation to enhance encryption security.

 * Key Methods:
 * - encrypt(String input): Encrypts a given input string and returns the salted, encoded output.
 * - decrypt(String input): Decrypts a previously encrypted string, removing the salt and decoding the material.
 */
public class EncryptionService implements IEncryption {

    private static final String RESET = "\033[0m";
    private static final String BLUE = "\033[0;34m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";

    private final SecureRandomSaltGenerator secureRandomSaltGenerator;
    private final ILogger ILogger;

    public EncryptionService(ILogger ILogger) {
        this.secureRandomSaltGenerator = new SecureRandomSaltGenerator();
        this.ILogger = ILogger;
    }

    @Override
    public String encrypt(String input) {
        System.out.println(BLUE + "\n===== ENCRYPTION =====" + RESET);
        System.out.println(GREEN + "Initial data: " + input + RESET);

        String base64Encoded = Base64.getEncoder().encodeToString(input.getBytes());
        System.out.println(YELLOW + "Base64 encoded: " + base64Encoded + RESET);
        String saltedPassword = addSalt(base64Encoded);

        System.out.println(GREEN + "Encrypted data: " + saltedPassword + RESET);
        System.out.println(BLUE + "=====================\n" + RESET);

        return saltedPassword;
    }

    @Override
    public String decrypt(String input) throws EncryptionException {
        try {
            if (input.length() < SALT_LENGTH) {
                String errorMessage = "The input data is too short to decode.";
                ILogger.logError(errorMessage);
                throw new EncryptionException(EncryptionException.Type.INVALID_KEY);
            }

            return new String(Base64.getDecoder().decode(input.substring(SALT_LENGTH)));

        } catch (IllegalArgumentException e) {
            String errorMessage = "Decoding error: " + e.getMessage();
            ILogger.logError(errorMessage);
            throw new EncryptionException(EncryptionException.Type.DECRYPTION_FAILED, e);

        } catch (Exception e) {
            String errorMessage = "Unknown decryption error: " + e.getMessage();
            ILogger.logError(errorMessage);
            throw new EncryptionException(EncryptionException.Type.DECRYPTION_FAILED, e);
        }
    }

    private String addSalt(String base64Encoded) {
        return secureRandomSaltGenerator.generateSalt(SALT_LENGTH) + base64Encoded;
    }
}