package com.maveric.deliveryms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaPropertiesReader {

    public  static Properties read(String fileName) throws IOException {
     InputStream inputStream= KafkaPropertiesReader.class.getClassLoader().
                getResourceAsStream(fileName);
     Properties properties=new Properties();
     properties.load(inputStream);
     return properties;
    }

}
