package com.teachmeskills.application.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * Manages the loading, retrieval, and manipulation of properties
 * from a specified properties file.
 * Provides thread-safe access to property values and
 * supports reloading and updating properties.
 */
public class PropertiesManager {
    private final String fileName;
    private final Properties properties = new Properties();

    public PropertiesManager(String fileName) throws IOException {
        this.fileName = fileName;
        loadProperties();
    }

    private void loadProperties() throws IOException {
        try (InputStream input = Optional.ofNullable(
                        getClass().getClassLoader().getResourceAsStream(fileName))
                .orElseThrow(() -> new IOException("Properties file not found: " + fileName))) {

            synchronized (properties) {
                properties.clear();
                properties.load(input);
            }
        } catch (IOException e) {
            I_LOGGER.logError("Error loading properties file: " + fileName);
            throw e;
        }
    }

    public void reloadProperties() throws IOException {
        loadProperties();
    }

    public String getString(String key, String defaultValue) {
        synchronized (properties) {
            return Optional.ofNullable(properties.getProperty(key))
                    .orElseGet(() -> {
                        I_LOGGER.logWarning("Key not found: " + key + ". Using default value.");
                        return defaultValue;
                    });
        }
    }

    public int getInt(String key, int defaultValue) {
        synchronized (properties) {
            String value = properties.getProperty(key);
            try {
                return value != null ? Integer.parseInt(value.trim()) : defaultValue;
            } catch (NumberFormatException e) {
                I_LOGGER.logWarning("Invalid integer for key: " + key + ". Using default value.");
                return defaultValue;
            }
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        synchronized (properties) {
            String value = properties.getProperty(key);
            return value != null ? Boolean.parseBoolean(value.trim()) : defaultValue;
        }
    }

    public void setProperty(String key, String value) {
        synchronized (properties) {
            properties.setProperty(key, value);
        }
    }
}