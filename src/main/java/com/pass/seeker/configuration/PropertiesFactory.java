package com.pass.seeker.configuration;

import com.pass.seeker.exception.ConfigurationException;
import com.pass.seeker.exception.PropertiesNotFoundException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class PropertiesFactory {

    private static final Map<String, Properties> propertyMap = new HashMap<>();

    static{
        initializePropertyMap();
        load();
    }

    private static String[] getFilenameList(){
        return new String[]{"application", "dictionary"};
    }

    private static void initializePropertyMap(){
        Arrays.stream(getFilenameList()).forEach(fn -> propertyMap.put(fn,new Properties()));
    }

    private static void load(){
        for (Map.Entry<String, Properties> entry : propertyMap.entrySet()) {
            String k = entry.getKey();
            Properties v = entry.getValue();
            try {
                java.io.InputStream r = PropertiesFactory.class.getClassLoader().getResourceAsStream(k + ".properties");
                if (r != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(r, StandardCharsets.UTF_8);
                    v.load(inputStreamReader);
                }
            } catch (IOException e) {
                throw new ConfigurationException(e);
            }
        }
    }

    public static Properties getProperties(String name){
        return Optional.ofNullable(propertyMap.get(name)).orElseThrow(PropertiesNotFoundException::new);
    }
}
