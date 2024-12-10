package com.teachmeskills.application.launcher.core;

import com.teachmeskills.application.exception.AuthenticationException;
import com.teachmeskills.application.exception.EncryptionException;
import com.teachmeskills.application.exception.WrongLoginException;
import com.teachmeskills.application.exception.WrongPasswordException;
import com.teachmeskills.application.security.resource.AuthenticatedUserData;
import com.teachmeskills.application.security.validation.LoginPasswordValidator;
import com.teachmeskills.application.services.authentication.AuthenticationService;
import com.teachmeskills.application.services.authentication.twofactor.TwoFactorAuthentication;
import com.teachmeskills.application.utils.config.ConfigurationLoader;

import java.util.Map;
import java.util.Scanner;

import static com.teachmeskills.application.utils.config.ConfigurationLoader.MAX_AUTH_ATTEMPTS;
import static com.teachmeskills.application.utils.config.ConfigurationLoader.VALIDATED_SECRET_KEY;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_ENCRYPTION_SERVICE;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * Manages the authentication process for the application.

 * Core Responsibilities:
 * - Two-factor authentication verification
 * - User credential validation
 * - Secure login process
 * - Error handling and logging

 * Key Features:
 * - Multi-step authentication
 * - Configurable authentication attempts
 * - Secure input handling
 * - Comprehensive error management

 * Authentication Workflow:
 * 1. Two-factor authentication
 * 2. Username and password validation
 * 3. Credential verification
 * 4. Session management

 * Security Measures:
 * - Encrypted credentials
 * - Input validation
 * - Attempt limit
 * - Detailed logging

 * Example Usage:
 * <pre>
 * AuthenticationGateway gateway = new AuthenticationGateway();
 * gateway.verifyTwoFactorAuthenticationCode(secretKey);
 * AuthenticatedUserData userData = gateway.performUserAuthentication();
 * </pre>
 *
 * Design Patterns:
 * - Gateway pattern
 * - Validation strategy
 * - Error handling abstraction
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [01.12.2024]
 */
public class AuthenticationGateway {

    private final LoginPasswordValidator passwordValidator = new LoginPasswordValidator();

    public static Scanner scanner = new Scanner(System.in);

    public void verifyTwoFactorAuthenticationCode(String secretKey) {
        int maxAttempts = 3;
        System.out.println("\nTWO-FACTOR AUTHENTICATION -> ");

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.printf("Attempt %d from %d%n", attempt, maxAttempts);
            String inputCode = requestUserInput("\nENTER the code from Microsoft Authenticator: ").trim(); // Убираем пробелы по краям

            if (inputCode.isEmpty()) {
                System.out.println("The two-factor authentication code cannot be empty!");
                I_LOGGER.logWarning("User entered an empty two-factor authentication code.");
                continue;
            }

            String generatedCode = TwoFactorAuthentication.getTOTPCode(secretKey);

            if (inputCode.equals(generatedCode)) {
                System.out.println("\nThe code has been confirmed successfully!");
                System.out.println("===================================================\n");
                VALIDATED_SECRET_KEY = secretKey;
                return;
            } else {
                System.out.println("Invalid code. Try again.");
                I_LOGGER.logWarning("Invalid two-factor authentication code entered.");
            }
        }

        throw new RuntimeException("The QR code could not be verified.");
    }

    public AuthenticatedUserData performUserAuthentication() {
        int attempt = 0;
        boolean isFirstAttempt = true;

        String encryptedUsername = ConfigurationLoader.ENCRYPTED_USERNAME;
        String encryptedPassword = ConfigurationLoader.ENCRYPTED_PASSWORD;
        System.out.println("\nComplete the authentication process..");
        System.out.println("Follow the INSTRUCTIONS.");
        I_LOGGER.logInfo("Starting user authentication process..");

        while (attempt < MAX_AUTH_ATTEMPTS) {
            try {
                attempt++;

                if (isFirstAttempt) {
                    System.out.print("\nAUTHENTICATION -> ");
                    System.out.printf(" attempt %d from %d%n", attempt, MAX_AUTH_ATTEMPTS);
                    isFirstAttempt = false;
                }
                String username = requestUserInput("\nENTER your username -> ");
                String password = requestUserInput("ENTER the password -> ");
                System.out.println();

                try {
                    passwordValidator.validateUserPassword(password);
                } catch (WrongPasswordException e) {
                    System.out.println("\nThe password cannot be confirmed.");
                    System.out.println("Try again..");

                    I_LOGGER.logWarning("Password validation failed: " + e.getMessage());
                    throw new WrongPasswordException("invalid username or password.");
                }

                AuthenticatedUserData authenticatedUserData = new AuthenticatedUserData(
                        encryptedUsername,
                        encryptedPassword,
                        I_ENCRYPTION_SERVICE
                );

                if (!username.equals(authenticatedUserData.getEncryptedUsername()) ||
                        !password.equals(I_ENCRYPTION_SERVICE.decrypt(encryptedPassword))) {
                    I_LOGGER.logWarning("Invalid username or password for the user: " + username);
                    System.out.println("\nInvalid username or password for the user");
                    throw new WrongLoginException("Invalid USERNAME or PASSWORD.");
                }
                System.out.println("The decryption of the data has begun.. Please wait.");
                I_LOGGER.logInfo("Decryption of user input data..");

                AuthenticationService authService = new AuthenticationService(authenticatedUserData, I_ENCRYPTION_SERVICE, I_LOGGER);

                Map<String, Object> sessionInfo = authService.authenticateUserWithoutOtp(
                        authenticatedUserData.getEncryptedUsername(),
                        password,
                        TwoFactorAuthentication.getTOTPCode(VALIDATED_SECRET_KEY)
                );
                System.out.println("\nYou have successfully logged into your account!\n");
                System.out.println("\nThe expiration date of your session is set to: " + sessionInfo.get("expirationDate"));

                I_LOGGER.logInfo("The user is authenticated: " + authenticatedUserData.getEncryptedUsername() +
                        ". The token is valid until: " + sessionInfo.get("expirationDate"));

                return authenticatedUserData;

            } catch (WrongLoginException | WrongPasswordException | AuthenticationException e) {
                logAndHandleAuthenticationError(attempt, e);
            } catch (EncryptionException e) {
                I_LOGGER.logError("Encryption error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        System.out.println("\nYou have exhausted data entry attempts in one session. Try again!");
        I_LOGGER.logError("Authentication failed after " + MAX_AUTH_ATTEMPTS + " attempts.");
        throw new RuntimeException("Authentication failed.");
    }

    private void logAndHandleAuthenticationError(int attempt, Exception e) {
        System.out.println("\nAUTHENTICATION -> ERROR");
        System.out.println("Incorrectly entered data -> " + e.getMessage());
        System.out.println("================================\n");

        I_LOGGER.logError("Authentication Error (Attempt " + attempt + "): " + e.getMessage());

        if (attempt == MAX_AUTH_ATTEMPTS) {
            System.out.println("The maximum number of login attempts has been exceeded.");
            I_LOGGER.logError("The maximum number of login attempts has been exceeded for user.");
            throw new RuntimeException("Authentication failed.");
        }
    }

    private String requestUserInput(String prompt) {
        System.out.print(prompt != null ? prompt : "Enter the data: ");
        return scanner.nextLine().trim();
    }
}