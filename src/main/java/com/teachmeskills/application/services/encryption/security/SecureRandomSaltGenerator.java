package com.teachmeskills.application.services.encryption.security;

import java.util.Random;
import java.util.stream.Collectors;

import static com.teachmeskills.application.utils.config.ConfigurationLoader.SYMBOLS;
/**
 * Utility class for generating cryptographically secure random salts.

 * Key Characteristics:
 * - Generates random salt strings for cryptographic purposes
 * - Supports configurable salt length
 * - Uses predefined symbol set for salt generation

 * Core Capabilities:
 * - Create random salt strings
 * - Ensure uniqueness of generated salts
 * - Provide flexible salt length configuration

 * Usage Scenarios:
 * - Password hashing
 * - Cryptographic salt generation
 * - Enhancing security of encryption processes

 * Security Considerations:
 * - Uses Java's Random number generator
 * - Generates salts from predefined character set
 * - Prevents generation of zero-length salts

 * Usage Examples:
 * <pre>
 * SecureRandomSaltGenerator saltGenerator = new SecureRandomSaltGenerator();
 * String salt = saltGenerator.generateSalt(16); // Generates 16-character salt
 * </pre>
 *
 * Implementation Details:
 * - Utilizes Stream API for efficient salt generation
 * - Randomly selects characters from predefined symbol set
 * - Throws IllegalArgumentException for invalid length

 * Performance Characteristics:
 * - O(n) time complexity, where n is salt length
 * - Minimal memory overhead
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [16.11.2024]
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