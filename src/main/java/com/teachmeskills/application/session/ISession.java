package com.teachmeskills.application.session;

import com.teachmeskills.application.exception.SessionManagerException;

import java.util.Date;
/**
 * The {@code ISession} interface defines the contract for session management,
 * providing comprehensive methods for access token generation, expiration calculation,
 * and validation.
 *
 * <p>This interface is crucial for implementing secure user authentication and
 * session tracking mechanisms in applications.</p>
 *
 * Key Responsibilities:
 * - Generate unique access tokens
 * - Calculate session expiration dates
 * - Validate access tokens

 * Usage Example:
 * <pre>
 * ISession sessionManager = new SessionImpl();
 * String token = sessionManager.createAccessToken(16);
 * Date expirationDate = sessionManager.calculateExpirationDate(30);
 * boolean isValid = sessionManager.isTokenValid(token, expirationDate);
 * </pre>
 *
 * Thread Safety: Implementations should consider thread-safety for multi-threaded environments.
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [21.11.2024]
 */
public interface ISession {

    String createAccessToken(int length);

    Date calculateExpirationDate(int minutesToAdd);

    boolean isTokenValid(String accessToken, Date expDate) throws SessionManagerException;

    String getAccessToken();
}
