package com.teachmeskills.application.services.encryption;

import com.teachmeskills.application.exception.EncryptionException;
/**
 * This interface defines methods for encrypting and decrypting data.
 * It serves as a contract for implementing encryption and decryption logic.

 * Core responsibilities:
 * - Provide a method for encrypting input strings.
 * - Provide a method for decrypting previously encrypted strings,
 *   with error handling for decryption failures.

 * Key Features:
 * - Abstraction for various encryption implementations.
 * - Simplified API for integrating encryption functionality in applications.
 */
public interface IEncryption {

    String encrypt(String input);

    String decrypt(String input) throws EncryptionException;
}