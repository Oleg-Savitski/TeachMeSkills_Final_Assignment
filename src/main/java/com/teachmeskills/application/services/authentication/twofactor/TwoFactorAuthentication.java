package com.teachmeskills.application.services.authentication.twofactor;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
/**
 * Utility class for implementing Two-Factor Authentication (2FA) functionality.

 * Key Features:
 * - Secret key generation
 * - Time-based One-Time Password (TOTP) code generation
 * - QR code creation for Google Authenticator

 * Supported Authentication Methods:
 * - TOTP algorithm implementation
 * - Google Authenticator compatibility

 * Main Functionalities:
 * - Generate secure secret keys
 * - Create TOTP codes
 * - Generate QR codes for easy 2FA setup

 * Security Characteristics:
 * - Cryptographically secure random key generation
 * - Base32 encoding for secret keys
 * - URL-safe encoding for authentication URIs

 * Use Cases:
 * - Two-factor authentication implementation
 * - Secure user verification
 * - Multi-factor authentication setup

 * Example Usage:
 * <pre>
 * // Generate a secret key
 * String secretKey = TwoFactorAuthentication.generateSecretKey();
 *
 * // Get current TOTP code
 * String totpCode = TwoFactorAuthentication.getTOTPCode(secretKey);
 *
 * // Create QR code for Google Authenticator
 * String barCode = TwoFactorAuthentication.getGoogleAuthenticatorBarCode(
 *     secretKey, "user@example.com", "MyApp"
 * );
 * TwoFactorAuthentication.createQRCode(barCode, "qrcode.png", 300, 300);
 * </pre>
 *
 * Dependencies:
 * - Google ZXing for QR code generation
 * - Apache Commons Codec
 * - TOTP library

 * Error Handling:
 * - Throws IllegalStateException for encoding errors
 * - Throws WriterException for QR code generation issues
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [28.11.2024]
 */
public class TwoFactorAuthentication {

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, StandardCharsets.UTF_8).replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+", "%20");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void createQRCode(String barCodeData, String filePath, int height, int width)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }
}