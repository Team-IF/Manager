package io.teamif.manager.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigHelper extends Properties {
    public Properties main()  {
        final String filePath = System.getProperty("user.dir") + "/config.ini";
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
