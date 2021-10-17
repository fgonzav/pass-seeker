package com.pass.seeker.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class PropertiesFactoryTest {

    @Test
    public void shouldLoadTwoPropertiesFiles(){
        Properties dictionary = PropertiesFactory.getProperties("dictionary");
        Properties application = PropertiesFactory.getProperties("application");

        String p1 = application.getProperty("temp.dir");
        String p2 = dictionary.getProperty("default.dictionary");

        assertNotNull(dictionary);
        assertNotNull(application);
        assertNotNull(p1);
        assertNotNull(p2);

        assertEquals("temp/", p1);
        assertEquals("abc0123456789@#!$%&()=¿?|.,;/\\~", p2);
    }
}