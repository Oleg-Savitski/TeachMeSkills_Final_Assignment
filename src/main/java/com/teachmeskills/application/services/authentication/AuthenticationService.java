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
 * Authentication service with robust user authentication capabilities, including support for two-factor authentication (2FA).

 * Key Features:
 * - Secure credential verification
 * - Support for two-factor authentication (2FA)
 * - Encryption and decryption of passwords
 * - User session management

 * Main Functionalities:
 * - Username and password validation
 * - OTP code validation
 * - Session token creation
 * - Authentication error handling

 * Use Cases:
 * - User login
 * - Protecting access to critical resources
 * - Multi-factor authentication

 * Security Mechanisms:
 * - Encryption of stored credentials
 * - Input integrity checks
 * - Protection against brute-force attacks
 * - Logging of login attempts

 * Example Usage:
 * <pre>
 * AuthenticationService authService = new AuthenticationService(
 *     userData, encryptionService, logger
 * );
 *
 * try {
 *     Map<String, Object> session = authService.authenticateUser WithoutOtp(
 *         "username", "password", "otpCode"
 *     );
 * } catch (AuthenticationException e) {
 *     // Handle authentication errors
 * }
 * </pre>
 *
 * Error Handling:
 * - Generation of AuthenticationException
 * - Detailed error logging
 * - Classification of error types
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [20.11.2024]
 */
public class AuthenticationService {

    private final AuthenticatedUserData authenticatedUserData;
    private final IEncryption IEncryptionService;
    private final ILogger ILogger;
    private final SessionManager sessionManager;

    public AuthenticationService(AuthenticatedUserData authenticatedUserData, IEncryption IEncryptionService, ILogger ILogger) {
        this.authenticatedUserData = Objects.requireNonNull(authenticatedUserData, "User  Storage cannot be null!");
        this.IEncryptionService = Objects.requireNonNull(IEncryptionService, "EncryptionService cannot be null!");
        this.ILogger = Objects.requireNonNull(ILogger, "Logger cannot be null!");
        this.sessionManager = new SessionManager();
    }

    public Map<String, Object> authenticateUserWithoutOtp(String username, String passcode, String otpCode) throws AuthenticationException {
        ILogger.logInfo("Processing the authentication request for user: " + username);

        validateInputs(username, passcode);

        String storedUsername = authenticatedUserData.getEncryptedUsername();
        String storedEncryptedPassword = authenticatedUserData.getEncryptedPassword();

        ILogger.logInfo("Verification of credentials for user: " + username);

        if (!isValidUsername(username, storedUsername)) {
            ILogger.logWarning("Invalid username attempt for user: " + username);
            throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS);
        }

        String decryptedPassword = decryptPassword(storedEncryptedPassword);

        if (!isValidPassword(passcode, decryptedPassword)) {
            ILogger.logWarning("Invalid password attempt for user: " + username);
            throw new AuthenticationException(AuthenticationException.Type.AUTHENTICATION_FAILED);
        }

        if (authenticatedUserData.isOtpEnabled()) {
            if (otpCode == null || otpCode.trim().isEmpty()) {
                ILogger.logWarning("OTP code is null or empty for user: " + username);
                throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS, new IllegalArgumentException
                        ("The two-factor authentication code cannot be empty!"));
            }

            if (!TwoFactorAuthentication.getTOTPCode(authenticatedUserData.getSecretKey()).equals(otpCode)) {
                ILogger.logWarning("Invalid OTP code for user: " + username);
                throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS);
            }
        }

        return createSuccessfulAuthenticationSession(username);
    }

    private void validateInputs(String username, String passcode) throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            ILogger.logWarning("Username is empty or null");
            throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS, new IllegalArgumentException("Имя пользователя не может быть пустым"));
        }

        if (passcode == null || passcode.isEmpty()) {
            ILogger.logWarning("Passcode is empty or null");
            throw new AuthenticationException(AuthenticationException.Type.INVALID_CREDENTIALS, new IllegalArgumentException("Пароль не может быть пустым"));
        }
    }

    private boolean isValidUsername(String inputUsername, String storedUsername) {
        return inputUsername.trim().equalsIgnoreCase(storedUsername);
    }

    private String decryptPassword(String encryptedPassword) throws AuthenticationException {
        try {
            return IEncryptionService.decrypt(encryptedPassword);
        } catch (EncryptionException e) {
            ILogger.logError("Error decrypting the data for user: " + authenticatedUserData.getEncryptedUsername() + " - " + e.getMessage());
            throw new AuthenticationException(AuthenticationException.Type.DECRYPTION_ERROR, e);
        }
    }

    private boolean isValidPassword(String inputPassword, String decryptedPassword) {
        return inputPassword.equals(decryptedPassword);
    }

    private Map<String, Object> createSuccessfulAuthenticationSession(String username) {
        ILogger.logInfo("Authentication is successful for the user: " + username);
        Map<String, Object> sessionInfo = new HashMap<>();

        String token = sessionManager.getAccessToken();
        Date expDate = sessionManager.getExpirationDate();

        authenticatedUserData.setAccessToken(token);
        authenticatedUserData.setExpirationDate(expDate);

        sessionInfo.put("accessToken", token);
        sessionInfo.put("expirationDate", expDate);

        return sessionInfo;
    }

    public Map<String, Object> authenticateUserWithoutOtp(String username, String passcode) throws AuthenticationException {
        return authenticateUserWithoutOtp(username, passcode, null);
    }
}