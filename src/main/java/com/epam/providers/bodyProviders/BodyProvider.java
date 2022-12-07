package com.epam.providers.bodyProviders;

import com.epam.providers.dataProviders.UserDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BodyProvider {
    private final static Logger logger = LoggerFactory.getLogger(BodyProvider.class);
    private static final Map<String, Object> bodyParameters = new HashMap<>();

    public static String getBody(String ftlFileName, Map<String, Object> params) {
        TemplateManager templateManager = new TemplateManager();
        logger.info("Get body with provided parameters -> {}", params);
        return templateManager.processTemplate(ftlFileName, params);
    }

    public static Map<String, Object> createBodyForNewUser() {
        bodyParameters.put("name", UserDataProvider.generateName());
        bodyParameters.put("email", UserDataProvider.generateEmail());
        bodyParameters.put("password", UserDataProvider.generateStrongPassword());
        return bodyParameters;
    }

    public static Map<String, Object> createInvalidUserBody(String name, String email, String password) {
        bodyParameters.put("name", name);
        bodyParameters.put("email", email);
        bodyParameters.put("password", password);
        return bodyParameters;
    }

    public static Map<String, Object> createBodyForResettingPassword(String newPassword) {
        bodyParameters.put("currentPassword", UserDataProvider.getExistedPassword());
        bodyParameters.put("newPassword", newPassword);
        bodyParameters.put("conformNewPassword", newPassword);
        return bodyParameters;
    }

}
