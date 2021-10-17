package com.pass.seeker.service.factory;

import com.pass.seeker.exception.UnsupportedFileException;
import com.pass.seeker.service.PassSeekerService;
import com.pass.seeker.service.RarPassSeekerService;

import java.io.File;

public class PassSeekerFactory {

    public static PassSeekerService getInstance(File compressedFile){
        String[] filename = compressedFile.getName().split("\\.");
        if(filename.length == 0)
            throw new UnsupportedFileException(String.format("File %s unsupported",compressedFile.getName()));

        switch (filename[filename.length-1]){
            case "rar":
                return new RarPassSeekerService();
            case "zip":
            case "tar":
            case "tar.gz":
                throw new UnsupportedFileException("not yet!!");
            default:
                throw new UnsupportedFileException(String.format("File %s - unsupported extension",compressedFile.getName()));
        }
    }
}
