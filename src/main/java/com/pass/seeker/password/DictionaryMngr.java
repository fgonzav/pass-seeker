package com.pass.seeker.password;

import com.pass.seeker.configuration.PropertiesFactory;
import com.pass.seeker.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class DictionaryMngr {

    private static final Properties properties = PropertiesFactory.getProperties("dictionary");

    public static Character[] getCharacters(){
        String dictionary = getDictionary();
        isValid(dictionary);
        return ArrayUtils.toObject(dictionary.toCharArray());
    }

    private static String getDictionary(){
        String userDictionary = System.getProperty("dictionary");
        return Optional.ofNullable(userDictionary).orElse(properties.getProperty("default.dictionary"));
    }

    private static void isValid(String dictionary){
        if(dictionary.isEmpty())
            throw new ConfigurationException("Dictionary must have at least one character");
        if(checkDuplicated(dictionary))
            throw new ConfigurationException("Dictionary must not have duplicated characters");
    }

    private static boolean checkDuplicated(String dictionary){
        char[] chars = dictionary.toCharArray();
        return Arrays.stream(ArrayUtils.toObject(chars)).anyMatch(c -> StringUtils.countMatches(dictionary,c) > 1);
    }
}
