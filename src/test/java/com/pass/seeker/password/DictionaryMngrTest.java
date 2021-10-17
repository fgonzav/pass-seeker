package com.pass.seeker.password;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DictionaryMngrTest {


    @Test
    public void shouldReturnDictionaryFromSystemProperties(){
        Character[] testArray = new Character[]{ 'a','s','d','f','g','h','j','k','l','ñ' };
        System.setProperty("dictionary", Arrays.stream(testArray).map(Object::toString).reduce("", (str, c)->str+c));

        Character[] dictionary = DictionaryMngr.getCharacters();

        System.clearProperty("dictionary");
        assertEquals(dictionary.length, testArray.length);
        assertArrayEquals(dictionary,testArray);
    }

    @Test
    public void shouldReturnDictionaryFromFileProperties(){
        Character[] testArray = new Character[]{'a','b','c','0','1','2','3','4','5','6','7','8','9','@','#','!','$','%','&','(',')','=','¿','?','|','.',',',';','/','\\','~'};
        Character[] dictionary = DictionaryMngr.getCharacters();

        System.out.println(Arrays.toString(dictionary));

        assertEquals(dictionary.length, testArray.length);
        assertArrayEquals(dictionary,testArray);
    }
}