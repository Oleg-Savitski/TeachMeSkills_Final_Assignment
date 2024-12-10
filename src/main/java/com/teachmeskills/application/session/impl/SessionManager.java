package com.teachmeskills.application.session.impl;

import com.teachmeskills.application.exception.SessionManagerException;
import com.teachmeskills.application.session.ISession;
import com.teachmeskills.application.utils.config.ConfigurationLoader;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

import static com.teachmeskills.application.utils.config.ConfigurationLoader.SESSION_TOKEN_LENGTH;
import static com.teachmeskills.application.utils.config.ConfigurationLoader.SYMBOLS;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * Implementation of the ISession interface for managing user session tokens.

 * Provides functionality for:
 * - Generating secure access tokens
 * - Calculating session expiration dates
 * - Validating access tokens

 * Key Features:
 * - Generates random tokens of configurable length
 * - Uses a predefined set of symbols for token creation
 * - Supports token expiration management
 * - Logs token generation and validation events

 * Thread Safety Considerations:
 * - Uses static fields for access token and expiration date
 * - Recommended for single-threaded or carefully synchronized environments

 * Configuration:
 * - Token length and expiration time are loaded from configuration
 * - Utilizes a logging mechanism for tracking session events

 * Usage Example:
 * <pre>
 * SessionManager sessionManager = new SessionManager();
 * String token = sessionManager.getAccessToken();
 * Date expirationDate = sessionManager.getExpirationDate();
 *
 * try {
 *     boolean isValid = sessionManager.isTokenValid(token, expirationDate);
 * } catch (SessionManagerException e) {
 *     // Handle token validation errors
 * }
 * </pre>
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [21.11.2024]
 * @see ISession
 * @see SessionManagerException
 */
public class SessionManager implements ISession {

    private static final String ANSI_RAD = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static String accessToken;
    private static Date expDate;

    public SessionManager() {
        if (accessToken == null || expDate == null) {
            accessToken = createAccessToken(SESSION_TOKEN_LENGTH);
            expDate = calculateExpirationDate(ConfigurationLoader.SESSION_EXPIRATION_MINUTES);
            System.out.println("\nThe token has been successfully generated for access to the program.");
            I_LOGGER.logInfo("An access token has been generated: " + ANSI_RAD + accessToken + ANSI_RESET);
            I_LOGGER.logInfo("The expiration date is set to: " + ANSI_RAD + expDate + ANSI_RESET);
        }
    }

    @Override
    public String createAccessToken(int length) {
        return new Random()
                .ints(length, 0, SYMBOLS.length())
                .mapToObj(SYMBOLS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    @Override
    public Date calculateExpirationDate(int minutesToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutesToAdd);
        calendar.add(Calendar.SECOND, 1);
        return calendar.getTime();
    }

    @Override
    public boolean isTokenValid(String accessToken, Date expDate) throws SessionManagerException {
        if (accessToken == null || expDate == null) {
            I_LOGGER.logWarning("The access token or expiration date is null.");
            throw new SessionManagerException(SessionManagerException.Type.INVALID_TOKEN);
        }

        Date currentDate = new Date();
        boolean isTokenLengthValid = accessToken.length() == SESSION_TOKEN_LENGTH;
        boolean isTokenNotExpired = expDate.after(currentDate);

        if (!isTokenLengthValid) {
            I_LOGGER.logError("Invalid access token length.");
            throw new SessionManagerException(SessionManagerException.Type.INVALID_TOKEN);
        }

        if (!isTokenNotExpired) {
            System.out.println("\nTry restarting the program.");
            I_LOGGER.logWarning("The access token has expired.");
            throw new SessionManagerException(SessionManagerException.Type.INVALID_TOKEN);
        }

        return false;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Date getExpirationDate() {
        return expDate;
    }
}