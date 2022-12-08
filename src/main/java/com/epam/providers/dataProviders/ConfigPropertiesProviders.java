package com.epam.providers.dataProviders;

import com.epam.helpers.PropertiesReader;

public class ConfigPropertiesProviders {

    private static final PropertiesReader properties = PropertiesReader.getInstance("config.properties");

    public String getBASE_URI() {
        return properties.getPropertyByKey("BASE_URI");
    }
}
