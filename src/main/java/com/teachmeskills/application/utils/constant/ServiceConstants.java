package com.teachmeskills.application.utils.constant;

import com.teachmeskills.application.services.encryption.IEncryption;
import com.teachmeskills.application.services.encryption.impl.EncryptionService;
import com.teachmeskills.application.services.logger.ILogger;
import com.teachmeskills.application.services.logger.impl.LoggerService;
/**
 * Interface containing constants for core service components.

 * ServiceConstants acts as a container for commonly used service instances,
 * ensuring that all application modules use the same global configurations
 * and instances of services. This avoids redundant instantiations and promotes
 * consistent behavior across the application.

 * Key Components:
 * - ILogger: Provides logging functionality for application runtime information.
 * - IEncryption: Handles encryption-related operations for security and data protection.

 * ILogger:
 * - Responsible for logging application information, warnings, and error messages.
 * - Enhances debugging and traceability through consistent and structured logging.

 * LoggerService:
 * - Implements ILogger to provide logging capabilities with formatted messages.
 * - Supports asynchronous and multi-threaded environments for efficient logging.

 * IEncryption:
 * - Provides functionality for encryption and decryption of data.
 * - Enables secure operations such as handling sensitive user information.

 * EncryptionService:
 * - Implements IEncryption and uses ILogger for logging purposes.
 * - Ensures secure data processing with error logging support.
 */

public interface ServiceConstants {

    ILogger I_LOGGER = new LoggerService("%s [%s]: %s");
    IEncryption I_ENCRYPTION_SERVICE = new EncryptionService(I_LOGGER);
}