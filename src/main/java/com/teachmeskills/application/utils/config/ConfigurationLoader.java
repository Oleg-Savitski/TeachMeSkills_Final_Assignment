package com.teachmeskills.application.utils.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * Comprehensive configuration management utility for the application.

 * Key Responsibilities:
 * - Load and manage application configuration from properties file
 * - Support dynamic configuration reloading
 * - Provide environment-based configuration overrides
 * - Manage secure credentials and settings

 * Features:
 * - File-based configuration with properties file
 * - Real-time configuration monitoring and hot-reloading
 * - Environment variable priority
 * - Secure credential handling
 * - Validation of configuration values

 * Configuration Sources (in priority order):
 * 1. Environment Variables
 * 2. Properties File
 * 3. Default Values

 * Thread Safety:
 * - Uses thread-safe scheduled executor for file monitoring
 * - Provides synchronized configuration reloading

 * Usage Examples:
 * <pre>
 * // Accessing configuration values
 * int tokenLength = ConfigurationLoader.SESSION_TOKEN_LENGTH;
 * String symbols = ConfigurationLoader.SYMBOLS;
 *
 * // Manually reloading configuration
 * ConfigurationLoader.reloadConfiguration();
 *
 * // Saving a new property
 * ConfigurationLoader.saveProperty("KEY", "VALUE");
 * </pre>
 *
 * Configuration Monitoring:
 * - Checks properties file every 5 seconds
 * - Automatically reloads on file changes

 * Error Handling:
 * - Logs configuration loading and modification errors
 * - Falls back to default values if configuration fails
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [26.11.2024]
 */
public class ConfigurationLoader {

    private static PropertiesManager PROPERTIES;

    public static String SYMBOLS;
    public static String FILTER_YEAR;
    public static String VALIDATED_SECRET_KEY;

    public static int SALT_LENGTH;
    public static int MAX_LOGIN_LENGTH;
    public static int MAX_PASSWORD_LENGTH;
    public static int SESSION_TOKEN_LENGTH;
    public static int SESSION_EXPIRATION_MINUTES;
    public static int MAX_AUTH_ATTEMPTS;

    public static String AWS_ACCESS_KEY;
    public static String AWS_SECRET_KEY;
    public static String AWS_S3_BUCKET_NAME;
    public static String AWS_REGION_NAME;

    public static String ENCRYPTED_USERNAME;
    public static String ENCRYPTED_PASSWORD;

    private static File PROPERTIES_FILE;
    private static long lastModified;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    static {
        try {
            initializePropertiesMonitoring();
        } catch (IOException e) {
            I_LOGGER.logError("Critical configuration loading error: " + e.getMessage());
            setDefaultValues();
        }
    }

    private static void initializePropertiesMonitoring() throws IOException {
        URL resourceUrl = ConfigurationLoader.class.getClassLoader()
                .getResource("financial-analyzer.properties");

        if (resourceUrl == null) {
            throw new IllegalStateException("Properties file not found in classpath");
        }

        PROPERTIES_FILE = new File(resourceUrl.getFile());

        PROPERTIES = new PropertiesManager(PROPERTIES_FILE.getName());
        lastModified = PROPERTIES_FILE.lastModified();

        initializeConfiguration();
        initializeAwsConfiguration();
        loadEncryptedCredentials();

        startPropertiesFileMonitoring();
    }

    private static void startPropertiesFileMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                long currentModified = PROPERTIES_FILE.lastModified();
                if (currentModified > lastModified) {
                    System.out.println("Properties file changed. Reloading configuration...");
                    I_LOGGER.logInfo("Properties file changed. Reloading configuration...");

                    PROPERTIES.reloadProperties();

                    initializeConfiguration();
                    initializeAwsConfiguration();
                    loadEncryptedCredentials();

                    lastModified = currentModified;
                }
            } catch (Exception e) {
                I_LOGGER.logError("Error monitoring properties file: " + e.getMessage());
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    public static void reloadConfiguration() {
        try {
            PROPERTIES.reloadProperties();
            initializeConfiguration();
            initializeAwsConfiguration();
            loadEncryptedCredentials();
            I_LOGGER.logInfo("Configuration manually reloaded.");
        } catch (IOException e) {
            I_LOGGER.logError("Failed to reload configuration: " + e.getMessage());
            setDefaultValues();
        }
    }

    public static void saveProperty(String key, String value) {
        try {
            PROPERTIES.setProperty(key, value);

            Properties props = new Properties();
            props.load(Files.newInputStream(Paths.get(PROPERTIES_FILE.getPath())));

            props.setProperty(key, value);

            try (OutputStream output = new FileOutputStream(PROPERTIES_FILE)) {
                props.store(output, "Updated property");
            }

            I_LOGGER.logInfo("Property saved: " + key + " = " + value);

            reloadConfiguration();
        } catch (IOException e) {
            I_LOGGER.logError("Error saving property: " + e.getMessage());
        }
    }

    public static void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    private static void loadEncryptedCredentials() {
        ENCRYPTED_USERNAME = PROPERTIES.getString("ENCRYPTED_USERNAME", "");
        ENCRYPTED_PASSWORD = PROPERTIES.getString("ENCRYPTED_PASSWORD", "");
    }

    private static void initializeConfiguration() {
        SYMBOLS = getEnvOrDefault("SYMBOLS",
                PROPERTIES.getString("SYMBOLS", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
        FILTER_YEAR = getEnvOrDefault("FILTER_YEAR",
                PROPERTIES.getString("FILTER_YEAR", "2024"));
        VALIDATED_SECRET_KEY = getEnvOrDefault("VALIDATED_SECRET_KEY",
                PROPERTIES.getString("VALIDATED_SECRET_KEY", "default_secret_key"));
        SALT_LENGTH = getValidatedInt("SALT_LENGTH", 16, 8, 32);
        MAX_LOGIN_LENGTH = getValidatedInt("MAX_LOGIN_LENGTH", 30, 5, 50);
        MAX_PASSWORD_LENGTH = getValidatedInt("MAX_PASSWORD_LENGTH", 50, 8, 255);
        SESSION_TOKEN_LENGTH = getValidatedInt("SESSION_TOKEN_LENGTH", 64, 32, 128);
        SESSION_EXPIRATION_MINUTES = getValidatedInt("SESSION_EXPIRATION_MINUTES", 30, 5, 120);
        MAX_AUTH_ATTEMPTS = getValidatedInt("MAX_AUTH_ATTEMPTS", 3, 1, 5);
    }

    private static void initializeAwsConfiguration() {
        AWS_ACCESS_KEY = getSecureConfigValue("AWS_ACCESS_KEY",
                PROPERTIES.getString("aws.s3.accessKey", ""));
        AWS_SECRET_KEY = getSecureConfigValue("AWS_SECRET_KEY",
                PROPERTIES.getString("aws.s3.secretKey", ""));
        AWS_S3_BUCKET_NAME = getEnvOrDefault("AWS_S3_BUCKET_NAME",
                PROPERTIES.getString("aws.s3.bucketName", "default-bucket"));
        AWS_REGION_NAME = getEnvOrDefault("AWS_REGION_NAME",
                PROPERTIES.getString("aws.s3.regionName", "eu-north-1"));
    }

    private static String getSecureConfigValue(String envKey, String propertiesValue) {
        return Optional.ofNullable(System.getenv(envKey))
                .filter(value -> !value.isEmpty())
                .orElseGet(() -> {
                    if (propertiesValue.isEmpty()) {
                        I_LOGGER.logWarning("No credentials found for " + envKey);
                    }
                    return propertiesValue;
                });
    }

    private static void setDefaultValues() {
        SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        FILTER_YEAR = "2024";
        VALIDATED_SECRET_KEY = "default_secret_key";
        SALT_LENGTH = 16;
        MAX_LOGIN_LENGTH = 30;
        MAX_PASSWORD_LENGTH = 50;
        SESSION_TOKEN_LENGTH = 64;
        SESSION_EXPIRATION_MINUTES = 30;
        MAX_AUTH_ATTEMPTS = 3;
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        return Optional.ofNullable(System.getenv(key)).orElse(defaultValue);
    }

    private static int getValidatedInt(String key, int defaultValue, int minValue, int maxValue) {
        try {
            return Optional.ofNullable(System.getenv(key))
                    .map(Integer::parseInt)
                    .filter(value -> value >= minValue && value <= maxValue)
                    .orElseGet(() -> {
                        int valueFromProperties = PROPERTIES.getInt(key, defaultValue);
                        if (valueFromProperties < minValue || valueFromProperties > maxValue) {
                            I_LOGGER.logWarning("The value is out of the acceptable range for " + key + ". The default value is used: " + defaultValue);
                            return defaultValue;
                        }
                        return valueFromProperties;
                    });
        } catch (Exception e) {
            I_LOGGER.logWarning("Error while getting the configuration for " + key);
            return defaultValue;
        }
    }
}