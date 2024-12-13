package com.teachmeskills.application.session;

import com.teachmeskills.application.exception.SessionManagerException;

import java.util.Date;
/**
 * Represents a session management interface for handling access tokens and their validity.
 * This interface defines methods for creating access tokens, calculating expiration dates,
 * validating tokens, and retrieving the current access token.

 * Responsibilities:
 * - Generate unique access tokens with a specified length.
 * - Calculate expiration dates based on the provided time duration.
 * - Validate access tokens against their expiration dates.
 * - Access the generated access token.

 * Methods:
 * - `createAccessToken(int length)`: Generates a new access token with the given length.
 * - `calculateExpirationDate(int minutesToAdd)`: Computes an expiration date by adding
 *   the specified number of minutes to the current time.
 * - `isTokenValid(String accessToken, Date expDate)`: Validates an access token based on
 *   its expiration date and throws an exception for invalid scenarios.
 * - `getAccessToken()`: Retrieves the current access token for the session.
 */
public interface ISession {

    String createAccessToken(int length);

    Date calculateExpirationDate(int minutesToAdd);

    boolean isTokenValid(String accessToken, Date expDate) throws SessionManagerException;

    String getAccessToken();
}
