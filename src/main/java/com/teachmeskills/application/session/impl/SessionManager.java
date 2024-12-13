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
 * The {@code SessionManager} class serves as an implementation of the {@link ISession}
 * interface. It provides functionalities related to session management, including:

 * 1. Generating a unique access token.
 * 2. Calculating the expiration date for the session.
 * 3. Validating the access token and its expiration status.

 * This class is integral to securing application access by verifying token validity
 * and ensuring sessions are correctly managed. It uses the access token and expiration
 * date as the core components of session management.

 * Key Features:
 * - Generates a unique session token with configurable length.
 * - Calculates the expiration time for a session.
 * - Validates session tokens by checking their length and expiration status.
 * - Provides access to the current session token and expiration date.
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