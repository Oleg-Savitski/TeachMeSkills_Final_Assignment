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
 * The TwoFactorAuthentication class provides methods to implement
 * two-factor authentication using TOTP (Time-based One-Time Passwords).
 * It allows generating secret keys, creating TOTP codes, generating
 * Google Authenticator-compatible barcodes, and creating QR codes.

 * Core Responsibilities:
 * - Generate secret keys for TOTP-based authentication.
 * - Generate time-based OTP codes based on a secret key.
 * - Create barcodes compatible with Google Authenticator for user account integration.
 * - Generate QR codes for visual representation of the barcode data.

 * Key Features:
 * - Secure random generation of secret keys.
 * - TOTP-based authentication mechanism.
 * - Barcode compatibility with Google Authenticator.
 * - QR code generation for enhanced user experience.

 * Methods:
 * - generateSecretKey: Creates a random secret key for use in TOTP.
 * - getTOTPCode: Generates a TOTP code using a given secret key.
 * - getGoogleAuthenticatorBarCode: Builds a Google Authenticator-compatible barcode URL.
 * - createQRCode: Generates a QR code from barcode data and saves it as an image file.

 * Use Cases:
 * - Enabling two-factor authentication for user accounts.
 * - Integrating with Google Authenticator or other TOTP-compatible apps.
 * - Providing secure and visually accessible QR codes for authentication setup.
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