package com.pass.seeker.extractor;

import com.pass.seeker.configuration.PropertiesFactory;
import com.pass.seeker.constant.PasswordConstants;
import com.pass.seeker.exception.ExtractorException;
import com.pass.seeker.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static com.pass.seeker.constant.PasswordConstants.UNRAR_SCRIPT;
import static com.pass.seeker.constant.PasswordConstants.UNRAR_SCRIPT_WIN;
import static com.pass.seeker.util.FileUtils.createDirIfNotExists;

@Slf4j
public class RarExtractor {

    private static final Properties application = PropertiesFactory.getProperties("application");

    private final Path unrarScript;

    private static RarExtractor extractor;

    private RarExtractor(){
        createDirIfNotExists(application.getProperty("temp.dir"));
        String script = isWindows()?UNRAR_SCRIPT_WIN:UNRAR_SCRIPT;

        try {
            String scriptPath = String.format("%s/%s",application.getProperty("temp.dir"), script);
            if(!Files.exists(Paths.get(scriptPath))){
                InputStream is = RarExtractor.class.getClassLoader().getResourceAsStream("scripts/"+script);
                //Path script = Files.createFile(Paths.get(String.format("%s/%s",application.getProperty("temp.dir"), PasswordConstants.UNRAR_SCRIPT)));
                this.unrarScript = FileUtils.createFile(scriptPath, new String(is.readAllBytes()));
            } else {
                this.unrarScript = Paths.get(scriptPath);
            }
        }
        catch (Exception e){
            log.error("[extractWithPassword] Cannot find script",e);
            throw new ExtractorException(e);
        }
    }

    public static RarExtractor getInstance(){
        if(extractor == null){
            synchronized (RarExtractor.class){
                if(extractor == null){
                    extractor = new RarExtractor();
                }
            }
        }
        return extractor;
    }

    public void destroy(){
        if(Files.exists(unrarScript))
            unrarScript.toFile().delete();
    }

    public boolean extractWithPassword(String rarPath, String dest, String password){

        ProcessBuilder builder = new ProcessBuilder();
        String commandScript = String.format("%s %s %s %s",unrarScript.toAbsolutePath().toString() , password, rarPath, dest);
        log.debug("[extractWithPassword] commandScript: "+commandScript);
        builder.command("sh", "-c", commandScript);
        builder.directory(new File("."));
        try {
            Process process = builder.start();
            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            return process.waitFor() == 0;
        }
        catch (InterruptedException | IOException e){
            throw new ExtractorException(e);
        }
    }

    private static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    private static class StreamGobbler implements Runnable {
        private final InputStream inputStream;
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                    .forEach(consumer);
        }
    }
}
