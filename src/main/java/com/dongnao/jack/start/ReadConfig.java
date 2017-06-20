package com.dongnao.jack.start;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadConfig {
    private static Properties pro = new Properties();
    
    static {
        readConfig();
    }
    
    public static String getValue(String name) {
        return pro.getProperty(name);
    }
    
    private static void readConfig() {
        FileInputStream in;
        try {
            in = new FileInputStream(ReadConfig.class.getResource("/")
                    .getFile() + File.separator + "config.properties");
            pro.load(in);
            in.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
