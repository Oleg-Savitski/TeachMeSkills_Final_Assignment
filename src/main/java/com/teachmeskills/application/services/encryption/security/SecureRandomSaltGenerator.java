package com.teachmeskills.application.services.encryption.security;

import java.util.Random;
import java.util.stream.Collectors;

import static com.teachmeskills.application.utils.config.ConfigurationLoader.SYMBOLS;
/**
 * The SecureRandomSaltGenerator class provides functionality to generate cryptographic salts
 * using a pseudo-random number generator (PRNG). A salt is a sequence of random characters
 * which is typically used to enhance the security of passwords or other sensitive data
 * by making enumeration attacks more difficult.

 * Key Responsibilities:
 * - Generate random salts of a specified length.
 * - Ensure salts are constructed using a secure random process.

 * Design Considerations:
 * - The length of the salt must be greater than zero.
 * - The implementation relies on the Random class, which provides a pseudo-random sequence.

 * Error Handling:
 * - Throws IllegalArgumentException if the provided salt length is less than 1.

 * Usage Context:
 * - Useful for creating secure salts in cryptographic processes or password hashing systems.
 * - Can be integrated into services that require randomized, secure tokens or identifiers.
 */
public class SecureRandomSaltGenerator {

    private final Random random;

    public SecureRandomSaltGenerator() {
        this.random = new Random();
    }

    public String generateSalt(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("The length of the salt must be greater than 0.");
        }
        return random.ints(length, 0, SYMBOLS.length())
                .mapToObj(SYMBOLS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}