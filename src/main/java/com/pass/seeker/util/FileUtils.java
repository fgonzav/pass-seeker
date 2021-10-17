package com.pass.seeker.util;

import com.pass.seeker.exception.FileException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Slf4j
public class FileUtils {

    public synchronized static File copy(File ori, String destPath){
        Path copy = Paths.get(destPath);
        try{
            log.debug("[copy] before copy");
            Files.copy(ori.toPath(),copy, StandardCopyOption.REPLACE_EXISTING);
            log.debug("[copy] copy success");
        }
        catch (Exception e){
            log.error("[copy] copy failed", e);
            throw new FileException(e);
        }
        return copy.toFile();
    }

    public static List<String> readFile(String path){
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new FileException(e);
        }
    }

    public static void createOrClearDir(Path path){
        if(path.toFile().exists()){
            deleteDirectory(path.toFile());
        }
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            throw new FileException(e);
        }
    }

    public static void deleteDirectory(File directory){
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

    public static Path createFile(String pathname, String content){
        try {
            Path file = Paths.get(pathname);
            if(file.toFile().exists())
                file.toFile().delete();

            return Files.write(Paths.get(pathname), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new FileException(e);
        }
    }

    public static Path createDirIfNotExists(String path){
        try {
            Path dir = Paths.get(path);
            if (!dir.toFile().exists()) {
                Files.createDirectory(dir);
            }
            return dir;
        }
        catch (Exception e){
            log.error("Error creating directory " + path, e);
            throw new FileException(e);
        }
    }

    public static boolean exists(String path){
        return Files.exists(Paths.get(path));
    }
}
