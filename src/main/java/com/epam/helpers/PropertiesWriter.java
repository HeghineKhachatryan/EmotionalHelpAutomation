package com.epam.helpers;

import java.io.*;
import java.util.Properties;

public class PropertiesWriter {

   private final static Properties properties = new Properties();

    public static void writeInPropertyFile(String filePath, String key, String value) {
        try(FileReader reader = new FileReader(filePath); FileWriter writer = new FileWriter(filePath)) {
            properties.load(reader);
            properties.put(key, value);
            properties.store(writer, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
