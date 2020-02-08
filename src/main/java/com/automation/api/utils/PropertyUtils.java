package com.automation.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyUtils {

    static Properties properties = new Properties();

    public static void loadProperties() {
        try {
            properties.load(new FileInputStream(new File("./src/main/resources/config.properties")));
            properties.load(new FileInputStream(new File("./src/main/resources/request_config.properties")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyByKey(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        loadProperties();
    }
}
