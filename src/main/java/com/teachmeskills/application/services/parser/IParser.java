package com.teachmeskills.application.services.parser;
/**
 * Represents an interface for parsing documents in a specified directory.

 * This interface is intended to define a contract for processing and
 * handling document parsing functionality. The primary operation is to
 * parse all documents located in a given directory using the provided access
 * credentials (access token).

 * Responsibilities:
 * - Process all documents found in the specified directory.
 * - Ensure proper handling of valid and invalid files.
 * - Utilize access tokens for any necessary authentication while parsing.

 * Method:
 * - `parseDocumentsInDirectory(String directoryPath, String accessToken)`:
 *   Parses all files in the given directory using the accessToken for authorization.
 */
public interface IParser {

    void parseDocumentsInDirectory(String directoryPath, String accessToken);
}