package com.pass.seeker.password;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordGeneratorTest {

    /*@BeforeEach
    public void setup(){
        ReflectionUtils.
    }*/

    @Test
    public void generateConsecutivePasswords(){
        PasswordGenerator passwordGenerator = PasswordGenerator.getInstance();

        String p1 = passwordGenerator.getNext().get();
        String p2 = passwordGenerator.getNext().get();
        String p3 = passwordGenerator.getNext().get();

        assertEquals("a", p1);
        assertEquals("b", p2);
        assertEquals("c", p3);
    }



    /*@Test
    public void shouldReturnEmptyAfterAllCombinationsAreReturned(){
        System.setProperty("dictionary","a07");
        System.setProperty("maxLength","2");

        PasswordGenerator passwordGenerator = PasswordGenerator.getInstance();
        String p1 = passwordGenerator.getNext().get(); //a
        String p2 = passwordGenerator.getNext().get(); //0
        String p3 = passwordGenerator.getNext().get(); //7
        String p4 = passwordGenerator.getNext().get(); //aa
        String p5 = passwordGenerator.getNext().get(); //a0
        String p6 = passwordGenerator.getNext().get(); //a7
        String p7 = passwordGenerator.getNext().get(); //0a
        String p8 = passwordGenerator.getNext().get(); //00
        String p9 = passwordGenerator.getNext().get(); //07
        String p10 = passwordGenerator.getNext().get(); //7a
        String p11 = passwordGenerator.getNext().get(); //70
        String p12 = passwordGenerator.getNext().get(); //77
        Optional<String> empty = passwordGenerator.getNext();

        System.clearProperty("dictionary");
        System.clearProperty("maxLength");

        assertEquals("a", p1);
        assertEquals("0", p2);
        assertEquals("7", p3);
        assertEquals("aa", p4);
        assertEquals("a0", p5);
        assertEquals("a7", p6);
        assertEquals("0a", p7);
        assertEquals("00", p8);
        assertEquals("07", p9);
        assertEquals("7a", p10);
        assertEquals("70", p11);
        assertEquals("77", p12);
        assertEquals(Optional.empty(),empty);
    }*/
}