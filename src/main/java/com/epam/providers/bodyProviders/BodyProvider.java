package com.epam.providers.bodyProviders;

import com.epam.providers.dataProviders.SharedTestData;
import com.epam.providers.dataProviders.UserDataProvider;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BodyProvider {
    private final static Logger logger = LoggerFactory.getLogger(BodyProvider.class);

    public static String getBody(String ftlFileName, Map<String, Object> params) {
        TemplateManager templateManager = new TemplateManager();
        logger.info("Get body with provided parameters -> {}", params);
        return templateManager.processTemplate(ftlFileName, params);
    }

    public static String createBodyForNewUser() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", UserDataProvider.generateName());
        jsonObject.put("email", UserDataProvider.generateEmail());
        jsonObject.put("password", UserDataProvider.generateStrongPassword());
        return jsonObject.toJSONString();
    }

    public static String createInvalidUserBody(String name, String email, String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        return jsonObject.toJSONString();
    }

    public static String createBodyForResettingPassword(String newPassword) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currentPassword", UserDataProvider.getExistedPassword());
        jsonObject.put("newPassword",newPassword);
        jsonObject.put("conformNewPassword", newPassword);
        return jsonObject.toJSONString();
    }

    public static String createBodyForResettingForgottenPassword(String newPassword) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", SharedTestData.getMessageText());
        jsonObject.put("password",newPassword);
        jsonObject.put("confirmPassword", newPassword);
        return jsonObject.toJSONString();
    }

    public static String createBodyForQuestionnaires() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", UserDataProvider.generateName());
        jsonObject.put("description", "description: " + UserDataProvider.generateName());
        return jsonObject.toJSONString();
    }

}
