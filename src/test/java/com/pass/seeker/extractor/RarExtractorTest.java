package com.pass.seeker.extractor;

import com.pass.seeker.util.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RarExtractorTest {

    @AfterEach
    public void deleteScript(){
        FileUtils.deleteDirectory(Paths.get("temp/").toFile());
    }

    @Test
    public void shouldExtract(){
        File test = new File("./test.txt");
        if(test.exists())
            test.delete();

        File rar = new File("./src/test/resources/test.rar");
        File dest = new File(".");
        assertTrue(RarExtractor.getInstance().extractWithPassword(rar.getAbsolutePath(), dest.getAbsolutePath(), "123"));
        test = new File("./test.txt");
        assertTrue(test.exists());
        test.delete();
    }

    @Test
    public void shouldFailExtract(){
        File test = new File("./test.txt");
        if(test.exists())
            test.delete();

        File rar = new File("./src/test/resources/test.rar");
        File dest = new File(".");
        assertFalse(RarExtractor.getInstance().extractWithPassword(rar.getAbsolutePath(), dest.getAbsolutePath(), "1234"));
    }
}