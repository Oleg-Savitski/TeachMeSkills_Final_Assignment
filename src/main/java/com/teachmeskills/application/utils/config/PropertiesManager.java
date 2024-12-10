package com.teachmeskills.application.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * Utility class for managing application properties with flexible loading and retrieval.

 * Key Responsibilities:
 * - Load properties from configuration files
 * - Provide type-safe property retrieval
 * - Support default value mechanisms
 * - Handle property reloading

 * Features:
 * - Classpath-based properties file loading
 * - Supports multiple property types (String, Integer, Boolean)
 * - Graceful handling of missing or invalid properties
 * - Logging of configuration warnings

 * Supported Property Retrieval Methods:
 * - {@link #getString(String, String)} for string values
 * - {@link #getInt(String, int)} for integer values
 * - {@link #getBoolean(String, boolean)} for boolean values

 * Usage Examples:
 * <pre>
 * PropertiesManager manager = new PropertiesManager("config.properties");
 *
 * // Retrieve properties with default values
 * String dbUrl = manager.getString("database.url", "default-url");
 * int maxConnections = manager.getInt("database.max.connections", 10);
 * boolean debugMode = manager.getBoolean("debug.enabled", false);
 *
 * // Modify properties
 * manager.setProperty("new.key", "new value");
 * </pre>
 *
 * Error Handling:
 * - Logs warnings for missing or invalid properties
 * - Provides default values to prevent null/invalid configurations

 * Thread Safety Considerations:
 * - Not inherently thread-safe
 * - Concurrent access should be externally synchronized
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [26.11.2024]
 */
public class PropertiesManager {
    private final String fileName;
    private Properties properties;

    public PropertiesManager(String fileName) throws IOException {
        this.fileName = fileName;
        loadProperties();
    }

    private void loadProperties() throws IOException {
        properties = new Properties();
        try (InputStream input = Optional.ofNullable(
                getClass().getClassLoader().getResourceAsStream(fileName)
        ).orElseThrow(() -> new IOException("Properties file not found: " + fileName))) {
            assert properties != null;
            properties.load(input);
        } catch (IOException e) {
            I_LOGGER.logError("Error loading properties file: " + fileName);
            throw e;
        }
    }

    public void reloadProperties() throws IOException {
        loadProperties();
    }

    public String getString(String key, String defaultValue) {
        return Optional.ofNullable(properties.getProperty(key))
                .orElseGet(() -> {
                    I_LOGGER.logWarning("Key not found: " + key + ". Using default value.");
                    return defaultValue;
                });
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        try {
            return value != null ? Integer.parseInt(value.trim()) : defaultValue;
        } catch (NumberFormatException e) {
            I_LOGGER.logWarning("Invalid integer for key: " + key + ". Using default value.");
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value.trim()) : defaultValue;
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}