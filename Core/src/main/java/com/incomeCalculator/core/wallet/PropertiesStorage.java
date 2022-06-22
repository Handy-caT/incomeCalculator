package com.incomeCalculator.core.wallet;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesStorage {

    private static String propertiesPath;
    private static PropertiesStorage instance;
    private Properties properties;

    private PropertiesStorage() {

    }

    public void setPropertiesPath(String propertiesPath) throws IOException {
        PropertiesStorage.propertiesPath = propertiesPath;

        FileInputStream fis = new FileInputStream(propertiesPath);
        properties = new Properties();
        properties.load(fis);
        fis.close();
    }

    public static PropertiesStorage getInstance() {
       if(instance == null){
           instance = new PropertiesStorage();
       }
       return instance;
    }

    public Object getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public void addProperty(String key, Object value) throws IOException {
        properties.put(key,value);
        properties.store(Files.newOutputStream(Paths.get(propertiesPath)),null);
    }

}
