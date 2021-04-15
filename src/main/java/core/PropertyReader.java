package core;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyReader {
    Properties prop = new Properties();

    public PropertyReader(String filePath) {
        try {
            prop.load(new FileInputStream(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String propName) {
        return prop.getProperty(propName);
    }
}