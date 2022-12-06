package com.epam.providers.dataProviders;

import com.epam.helpers.PropertiesReader;

public class ConfigPropertiesProviders {

    private static final PropertiesReader properties = PropertiesReader.getInstance("config.properties");

    public String getBASE_URI() {
        return properties.getPropertyByKey("BASE_URI");
    }

    public String getSignUpEndpoint() {
        return properties.getPropertyByKey("signUpEndpoint");
    }

    public String getLoginEndpoint() {
        return properties.getPropertyByKey("loginEndpoint");
    }

    public String getResetPasswordEndpoint() {
        return properties.getPropertyByKey("resetPasswordEndpoint");
    }

    public String getResetForgottenPasswordEndpoint() {
        return properties.getPropertyByKey("resetForgottenPasswordEndpoint");
    }

    public String getSendEmailEndpoint() {
        return properties.getPropertyByKey("sendEmailEndpoint");
    }
}
