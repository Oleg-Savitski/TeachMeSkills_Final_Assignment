package com.teachmeskills.application.security.resource;

import com.teachmeskills.application.exception.EncryptionException;
import com.teachmeskills.application.exception.WrongLoginException;
import com.teachmeskills.application.exception.WrongPasswordException;
import com.teachmeskills.application.services.encryption.IEncryption;
import com.teachmeskills.application.security.validation.LoginPasswordValidator;

import java.util.Date;
/**
 * Represents authenticated user data containing encrypted login credentials,
 * secret key, access tokens, and additional login-related properties.

 * This class is utilized to store and manage sensitive user information securely
 * while providing controlled access and validation mechanisms. Encrypted username
 * is decrypted during object initialization using a provided encryption service.
 * Additional validations on the username are performed through the
 * {@code LoginPasswordValidator}.

 * Responsibilities:
 * - Store encrypted username and password.
 * - Handle and validate secret key setup.
 * - Provide management for One-Time Password (OTP) settings.
 * - Manage access token and its expiration date.

 * Constructor Details:
 * - Accepts the encrypted username, encrypted password, and an implementation
 *   of {@code IEncryption} for decryption.
 * - Validates the decrypted username using {@code LoginPasswordValidator}.
 * - Throws exceptions for invalid inputs, decryption failures, or validation errors.

 * Exceptions:
 * - {@code IllegalArgumentException}: Thrown if any of the required arguments are null.
 * - {@code WrongLoginException}: Thrown if the username decryption fails or validation fails.
 * - {@code WrongPasswordException}: Reserved for any future password validation logic.

 * Key Methods:
 * - Getters provide access to the encrypted username, password, secret key,
 *   OTP settings, access token, and expiration date.
 * - Setters allow updating the secret key, OTP settings, access token, and expiration date.
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