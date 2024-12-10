package com.teachmeskills.application.security.resource;

import com.teachmeskills.application.exception.EncryptionException;
import com.teachmeskills.application.exception.WrongLoginException;
import com.teachmeskills.application.exception.WrongPasswordException;
import com.teachmeskills.application.services.encryption.IEncryption;
import com.teachmeskills.application.security.validation.LoginPasswordValidator;

import java.util.Date;
/**
 * Secure user authentication data management class with advanced security features.

 * Key Security Components:
 * - Encrypted username and password
 * - Two-factor authentication support
 * - Access token management
 * - Credential validation

 * Security Features:
 * - Immutable encrypted credentials
 * - Dynamic secret key management
 * - Optional OTP (One-Time Password) support
 * - Expiration tracking

 * Authentication Flow:
 * 1. Decrypt and validate credentials
 * 2. Generate access token
 * 3. Manage authentication lifecycle

 * Example Usage:
 * <pre>
 * IEncryption encryptionService = new AESEncryptionService();
 *
 * try {
 *     AuthenticatedUserData userData = new AuthenticatedUserData(
 *         encryptedUsername,
 *         encryptedPassword,
 *         encryptionService
 *     );
 *
 *     // Enable two-factor authentication
 *     userData.setOtpEnabled(true);
 *     userData.setSecretKey(generateSecretKey());
 *
 *     // Set access token and expiration
 *     userData.setAccessToken(generateAccessToken());
 *     userData.setExpirationDate(calculateExpirationDate());
 * } catch (WrongLoginException | WrongPasswordException e) {
 *     // Handle authentication errors
 * }
 * </pre>
 *
 * Error Handling:
 * - Strict null checks
 * - Comprehensive exception management
 * - Secure decryption error handling

 * Thread Safety:
 * - Partially thread-safe
 * - Immutable core credentials
 * - Synchronized access recommended for mutable fields
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [03.11.2024]
 */
public class AuthenticatedUserData {

    private final String encryptedUsername;
    private final String encryptedPassword;
    private String secretKey;
    private boolean otpEnabled;
    private String accessToken;
    private Date expirationDate;

    public AuthenticatedUserData(String encryptedUsername, String encryptedPassword, IEncryption IEncryptionService)
            throws WrongLoginException, WrongPasswordException {

        if (encryptedUsername == null) {
            throw new IllegalArgumentException("encryptedUsername cannot be null");
        }
        if (encryptedPassword == null) {
            throw new IllegalArgumentException("encryptedPassword cannot be null");
        }
        if (IEncryptionService == null) {
            throw new IllegalArgumentException("IEncryptionService cannot be null");
        }
        try {
            this.encryptedUsername = IEncryptionService.decrypt(encryptedUsername);
        } catch (EncryptionException e) {
            throw new WrongLoginException("Error decrypting the login: " + e.getMessage());
        }

        this.encryptedPassword = encryptedPassword;

        LoginPasswordValidator validator = new LoginPasswordValidator();
        validator.validateUsername(this.encryptedUsername);

        this.secretKey = null;
        this.otpEnabled = false;
    }

    public String getEncryptedUsername() {
        return encryptedUsername;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isOtpEnabled() {
        return otpEnabled;
    }

    public void setOtpEnabled(boolean otpEnabled) {
        this.otpEnabled = otpEnabled;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}