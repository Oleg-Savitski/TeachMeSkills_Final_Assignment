package com.teachmeskills.application.launcher.core;

import com.teachmeskills.application.security.resource.AuthenticatedUserData;
import com.teachmeskills.application.services.authentication.AuthenticationService;
import com.teachmeskills.application.services.parser.impl.ParserService;
import com.teachmeskills.application.services.statistic.impl.StatsService;
import com.teachmeskills.application.session.ISession;
import com.teachmeskills.application.session.impl.SessionManager;

import java.io.File;
import java.util.Date;
import java.util.Map;

import static com.teachmeskills.application.utils.constant.FilePathConstants.CONFIG_DIR;
import static com.teachmeskills.application.utils.constant.FilePathConstants.QR_CODE_DIR;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_ENCRYPTION_SERVICE;
import static com.teachmeskills.application.utils.constant.ServiceConstants.I_LOGGER;
/**
 * The DocumentAnalysisCoordinator class is responsible for orchestrating the document
 * analysis workflow. This includes initializing necessary resources, managing authentication,
 * and analyzing documents from a user-supplied directory.
 */
public class DocumentAnalysisCoordinator {
    public void executeDocumentAnalysis() {
        try {
            createNecessaryDirectories();
            String header = "LAUNCHING the PROGRAM";
            String border = "*".repeat(header.length());

            System.out.println(border);
            System.out.println(header);
            System.out.println(border);

            AuthenticationGateway authManager = new AuthenticationGateway();
            AuthenticatedUserData userData = authManager.performUserAuthentication();

            String secretKey = MultiFactorAuthManager.retrieveOrGenerateQRCode();
            authManager.verifyTwoFactorAuthenticationCode(secretKey);

            String documentsPath = DocumentDirectorySelector.promptForDocumentDirectory();

            analyzeDocuments(userData, documentsPath);
            System.out.println("\nThe document analysis has been completed successfully!");
            I_LOGGER.logInfo("The document analysis has been completed successfully!");
        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred when analyzing files. Try again later.");
            I_LOGGER.logError("Unexpected error when starting the analyzer: " +
                    e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    private void createNecessaryDirectories() {
        File configDir = new File(CONFIG_DIR);
        File qrCodeDir = new File(QR_CODE_DIR);

        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        if (!qrCodeDir.exists()) {
            qrCodeDir.mkdirs();
        }
    }

    private void analyzeDocuments(AuthenticatedUserData authenticatedUserData, String documentsFolderPath) {
        try {
            long startTime = System.currentTimeMillis();

            StatsService statistics = new StatsService(I_LOGGER);
            ISession session = new SessionManager();
            AuthenticationService authService = new AuthenticationService(
                    authenticatedUserData,
                    I_ENCRYPTION_SERVICE,
                    I_LOGGER);

            String accessToken = authenticatedUserData.getAccessToken();
            Date expirationDate = authenticatedUserData.getExpirationDate();

            if (accessToken == null || session.isTokenValid(accessToken, expirationDate)) {
                String decryptedPassword = I_ENCRYPTION_SERVICE.decrypt(
                        authenticatedUserData.getEncryptedPassword());

                Map<String, Object> sessionInfo = authService.authenticateUserWithoutOtp(
                        authenticatedUserData.getEncryptedUsername(),
                        decryptedPassword);

                accessToken = (String) sessionInfo.get("accessToken");
                expirationDate = (Date) sessionInfo.get("expirationDate");
                authenticatedUserData.setAccessToken(accessToken);
                authenticatedUserData.setExpirationDate(expirationDate);
            }

            ParserService parserService = new ParserService(session, I_LOGGER, statistics);
            parserService.parseDocumentsInDirectory(documentsFolderPath, accessToken);
            CloudDataUploader cloudDataUploader = new CloudDataUploader();
            cloudDataUploader.uploadStatisticsToCloud();

            long processingTime = System.currentTimeMillis() - startTime;
            System.out.println("\nThe processing of documents is completed in " + processingTime + " ms.");
            I_LOGGER.logInfo("The processing of documents is completed in " + processingTime + " ms.");

        } catch (Exception e) {
            System.out.println("\nError in processing documents. Try again!");
            I_LOGGER.logError("Document processing error: " +
                            e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}