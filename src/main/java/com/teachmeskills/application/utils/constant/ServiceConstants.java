package com.teachmeskills.application.utils.constant;

import com.teachmeskills.application.services.encryption.IEncryption;
import com.teachmeskills.application.services.encryption.impl.EncryptionService;
import com.teachmeskills.application.services.logger.ILogger;
import com.teachmeskills.application.services.logger.impl.LoggerService;
/**
 * Centralized interface for singleton service instances in the application.

 * Provides standardized, pre-configured service implementations for:
 * - Logging
 * - Encryption

 * Key Features:
 * - Centralized service initialization
 * - Consistent service configuration
 * - Singleton-like service access

 * Services:
 * - {@link #I_LOGGER}: Centralized logging service
 * - {@link #I_ENCRYPTION_SERVICE}: Application-wide encryption service

 * Design Patterns:
 * - Utilizes dependency injection principles
 * - Provides interface-based service definitions

 * Usage Examples:
 * <pre>
 * // Logging
 * ServiceConstants.I_LOGGER.info("Application started");
 *
 * // Encryption
 * String encryptedData = ServiceConstants.I_ENCRYPTION_SERVICE.encrypt(sensitiveData);
 * </pre>
 *
 * Recommendations:
 * - Consider using dependency injection frameworks
 * - Implement lazy initialization for services
 * - Support for service configuration and customization

 * Potential Improvements:
 * - Add more service initializations
 * - Support for dynamic service configuration
 * - Implement service lifecycle management
 *
 * @author [Oleg Savitski]
 * @version 1.0
 * @since [23.11.2024]
 */

public interface ServiceConstants {

    ILogger I_LOGGER = new LoggerService("%s [%s]: %s");
    IEncryption I_ENCRYPTION_SERVICE = new EncryptionService(I_LOGGER);
}