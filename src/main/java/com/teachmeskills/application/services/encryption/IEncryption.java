package com.teachmeskills.application.services.encryption;

import com.teachmeskills.application.exception.EncryptionException;
/**
 * Interface for cryptographic encryption and decryption operations.

 * Key Characteristics:
 * - Abstraction of encryption mechanisms
 * - Support for reversible encryption
 * - Secure data transformation

 * Core Capabilities:
 * - Encryption of input string
 * - Decryption of encrypted string

 * Usage Scenarios:
 * - Protection of confidential data
 * - Secure information storage
 * - Secure data transmission

 * Security Requirements:
 * - Encryption irreversibility
 * - Resistance to cryptanalysis
 * - Minimization of information leaks

 * Usage Examples:
 * <pre>
 * IEncryption encryptor = new AESEncryption();
 * String encrypted = encryptor.encrypt("sensitive_data");
 * String decrypted = encryptor.decrypt(encrypted);
 * </pre>
 *
 * Potential Implementations:
 * - Symmetric encryption (AES)
 * - Asymmetric encryption (RSA)
 * - Salted hashing
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [29.11.2024]
 */
public interface IEncryption {

    String encrypt(String input);

    String decrypt(String input) throws EncryptionException;
}