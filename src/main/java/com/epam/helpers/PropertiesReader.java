package com.epam.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesReader {

    private final Properties properties;

    private PropertiesReader(String resourceName) {
        try {
            properties = new Properties();
            properties.load(getInputStream(resourceName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PropertiesReader getInstance(String resourceName) {
        return new PropertiesReader(resourceName);
    }

    private InputStream getInputStream(String filename) {
        return PropertiesReader.class.getClassLoader().getResourceAsStream(filename);
    }

    public String getPropertyByKey(String key) {
        return properties.getProperty(key);
    }

    public void updateValueOfProperty(String key, String value) {
        properties.put(key, value);
    }
}
