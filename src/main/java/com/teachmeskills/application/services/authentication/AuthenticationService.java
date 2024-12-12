package com.teachmeskills.application.services.authentication;

import com.teachmeskills.application.exception.AuthenticationException;
import com.teachmeskills.application.exception.EncryptionException;
import com.teachmeskills.application.security.resource.AuthenticatedUserData;
import com.teachmeskills.application.services.authentication.twofactor.TwoFactorAuthentication;
import com.teachmeskills.application.services.encryption.IEncryption;
import com.teachmeskills.application.services.logger.ILogger;
import com.teachmeskills.application.session.impl.SessionManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * Service class responsible for handling user authentication logic,
 * including credential validation and session management.

 * Core Responsibilities:
 * - Verify user credentials (username and password).
 * - Handle two-factor authentication (OTP) validation when enabled.
 * - Manage creation of user authentication sessions.
 * - Log all authentication-related actions.
 * - Decrypt sensitive user data as part of the authentication process.

 * Dependencies:
 * - {@code AuthenticatedUserData}: Contains encrypted user credentials and session state.
 * - {@code IEncryption}: Utility for encrypting and decrypting sensitive data.
 * - {@code ILogger}: Logging interface for tracking authentication operations.
 * - {@code SessionManager}: Utility for handling session tokens and expiration details.

 * Features:
 * - Supports authentication with and without OTP depending on user settings.
 * - Validates username and password by comparing against securely stored values.
 * - Supports logging for both successful and failed login attempts.
 * - Detects and handles invalid input parameters such as empty or null values.
 * - Generates and returns authentication session details upon successful login.

 * Exception Handling:
 * - Throws {@code AuthenticationException} for various failure scenarios, including:
 *   - Invalid credentials (username or password mismatch).
 *   - Decryption errors during password validation.
 *   - Invalid OTP for two-factor authentication.
 *   - Issues with session token generation.

 * Thread Safety:
 * - Not inherently thread-safe. Instances of this class should be used with caution in a multithreaded environment to avoid inconsistent state.

 * Authentication Flow:
 * - Validate input parameters (username and password).
 * - Decrypt stored credentials and verify against the provided input.
 * - If OTP is enabled, validate the provided OTP using the user's secret key.
 * - Generate a new session token and expiration date on successful authentication.
 */
public class AuthenticationService {

    private final AuthenticatedUserData authenticatedUserData;
    private final IEncryption encryptionService;
    private final ILogger logger;
    private final SessionManager sessionManager;

    public AuthenticationService(AuthenticatedUserData authenticatedUserData, IEncryption encryptionService, ILogger logger) {
        this.authenticatedUserData = Objects.requireNonNull(authenticatedUserData, "User Storage cannot be null!");
        this.encryptionService = Objects.requireNonNull(encryptionService, "EncryptionService cannot be null!");
        this.logger = Objects.requireNonNull(logger, "Logger cannot be null!");
        this.sessionManager = new SessionManager();
    }

    public Map<String, Object> authenticateUserWithoutOtp(String username, String passcode) throws AuthenticationException {
        return authenticateUserWithoutOtp(username, passcode, null);
    }

    public Map<String, Object> authenticateUserWithoutOtp(String username, String passcode, String otpCode) throws AuthenticationException {
        logger.logInfo("Processing the authentication request for user: " + username);

        validateInputs(username, passcode);

        String storedUsername = authenticatedUserData.getEncryptedUsername();
        String storedEncryptedPassword = authenticatedUserData.getEncryptedPassword();

        logger.logInfo("Verification of credentials for user: " + username);

        if (!isValidUsername(username, storedUsername)) {
            logAndThrowInvalidCredentials("Invalid username attempt for user: " + username);
        }

        String decryptedPassword = decryptPassword(storedEncryptedPassword);

        if (!isValidPassword(passcode, decryptedPassword)) {
            logAndThrowAuthenticationFailed("Invalid password attempt for user: " + username);
        }

        if (authenticatedUserData.isOtpEnabled()) {
            validateOtp(otpCode, username);
        }

        return createSuccessfulAuthenticationSession(username);
    }

    private void validateInputs(String username, String passcode) throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            logAndThrowInvalidCredentials("Username is empty or null");
        }

        if (passcode == null || passcode.isEmpty()) {
            logAndThrowInvalidCredentials("Passcode is empty or null");
        }
    }

    private void validateOtp(String otpCode, String username) throws AuthenticationException {
        if (otpCode == null || otpCode.trim().isEmpty()) {
            logger.logWarning("OTP code is null or empty for user: " + username);
            throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS,
                    new IllegalArgumentException("The two-factor authentication code cannot be empty!"));
        }

        try {
            String expectedOtp = TwoFactorAuthentication.getTOTPCode(authenticatedUserData.getSecretKey());
            if (!expectedOtp.equals(otpCode)) {
                logger.logWarning("Invalid OTP code for user: " + username);
                throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS);
            }
        } catch (Exception e) {
            logger.logError("Error validating OTP for user: " + username + " - " + e.getMessage());
            throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS, e);
        }
    }

    private boolean isValidUsername(String inputUsername, String storedUsername) {
        return inputUsername.trim().equalsIgnoreCase(storedUsername);
    }

    private String decryptPassword(String encryptedPassword) throws AuthenticationException {
        try {
            return encryptionService.decrypt(encryptedPassword);
        } catch (EncryptionException e) {
            logger.logError("Error decrypting the data for user: " + authenticatedUserData.getEncryptedUsername() + " - " + e.getMessage());
            throw new AuthenticationException(AuthenticationException.Type.DECRYPTION_ERROR, e);
        }
    }

    private boolean isValidPassword(String inputPassword, String decryptedPassword) {
        return inputPassword.equals(decryptedPassword);
    }

    private Map<String, Object> createSuccessfulAuthenticationSession(String username) throws AuthenticationException {
        logger.logInfo("Authentication is successful for the user: " + username);
        Map<String, Object> sessionInfo = new HashMap<>();

        try {
            String token = sessionManager.getAccessToken();
            Date expDate = sessionManager.getExpirationDate();

            authenticatedUserData.setAccessToken(token);
            authenticatedUserData.setExpirationDate(expDate);

            sessionInfo.put("accessToken", token);
            sessionInfo.put("expirationDate", expDate);
        } catch (Exception e) {
            logger.logError("Error creating session for user: " + username + " - " + e.getMessage());
            throw new AuthenticationException(AuthenticationException.Type.SESSION_CREATION_ERROR, e);
        }

        return sessionInfo;
    }

    private void logAndThrowInvalidCredentials(String message) throws AuthenticationException {
        logger.logWarning(message);
        throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS);
    }

    private void logAndThrowAuthenticationFailed(String message) throws AuthenticationException {
        logger.logWarning(message);
        throw new AuthenticationException(AuthenticationException.Type.AUTHENTICATION_FAILED);
    }
}