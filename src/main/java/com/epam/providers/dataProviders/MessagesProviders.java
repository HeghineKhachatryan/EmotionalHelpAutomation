package com.epam.providers.dataProviders;

import com.epam.helpers.PropertiesReader;

public class MessagesProviders {
    private static final PropertiesReader properties = PropertiesReader.getInstance("messages.properties");

    public static String getResetPasswordSuccessMsg() {
        return properties.getPropertyByKey("resetPasswordSuccess");
    }


}
