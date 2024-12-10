package com.teachmeskills.application.services.encryption.impl;

import com.teachmeskills.application.exception.EncryptionException;
import com.teachmeskills.application.services.encryption.IEncryption;
import com.teachmeskills.application.services.encryption.security.SecureRandomSaltGenerator;
import com.teachmeskills.application.services.logger.ILogger;

import java.util.Base64;

import static com.teachmeskills.application.utils.config.ConfigurationLoader.SALT_LENGTH;
/**
 * Implementation of encryption and decryption service with Base64 encoding and salt generation.

 * Key Characteristics:
 * - Base64-based encoding/decoding mechanism
 * - Salt-enhanced encryption process
 * - Secure data transformation
 * - Detailed logging support

 * Core Capabilities:
 * - Encrypt input strings
 * - Decrypt salted Base64-encoded strings
 * - Generate cryptographic salts
 * - Provide detailed encryption/decryption logs

 * Usage Scenarios:
 * - Secure data storage
 * - Confidential information protection
 * - Logging encryption processes

 * Security Considerations:
 * - Uses Base64 encoding
 * - Adds cryptographic salt
 * - Handles potential decryption errors
 * - Prevents decoding of invalid inputs

 * Usage Examples:
 * <pre>
 * ILogger logger = new LoggerImpl();
 * EncryptionService encryptor = new EncryptionService(logger);
 * String encrypted = encryptor.encrypt("sensitive_data");
 * String decrypted = encryptor.decrypt(encrypted);
 * </pre>
 *
 * Error Handling:
 * - Throws EncryptionException for decryption failures
 * - Logs detailed error information

 * Implementation Details:
 * - Utilizes SecureRandomSaltGenerator
 * - Supports configurable salt length
 * - Provides colorful console logging
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [17.11.2024]
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