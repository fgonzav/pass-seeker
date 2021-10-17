package com.pass.seeker.service;

import static com.pass.seeker.constant.PasswordConstants.RESULT_FILE;
import static com.pass.seeker.util.FileUtils.createOrClearDir;

import com.pass.seeker.configuration.PropertiesFactory;
import com.pass.seeker.extractor.RarExtractor;
import com.pass.seeker.password.PasswordGenerator;
import com.pass.seeker.password.PasswordMngr;
import com.pass.seeker.util.FileUtils;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RarPassSeekerService implements PassSeekerService {

    private static final Properties application = PropertiesFactory.getProperties("application");

    private final PasswordGenerator generator = PasswordGenerator.getInstance();

    public RarPassSeekerService(){
        PasswordMngr.readLastUsedPassword().ifPresent(generator::setLastUsed);
        createOrClearDir(Paths.get(application.getProperty("temp.dir")));
    }

    @Override
    public String seekPassword(File compressedFile) {
        log.debug("[seekPassword] init");

        List<CompletableFuture<Void>> futures = runAsync(compressedFile);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .exceptionally(ex->{
                    log.error("[seekPassword] Terminated w/exception",ex);
                    return null;
                }).join();

        List<String> lines = FileUtils.exists(RESULT_FILE)? FileUtils.readFile(RESULT_FILE) : Collections.emptyList();

        log.info("[seekPassword] end, lines: " + Arrays.toString(lines.toArray()));
        return lines.size() > 0? lines.get(0): StringUtils.EMPTY;
    }

    private List<CompletableFuture<Void>> runAsync(File compressedFile){
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        int threadNumber = Integer.parseInt(application.getProperty("max.threads"));
        for(int i = 0; i < threadNumber; i++){
            String file = String.format("%s/%s-%d.rar", application.getProperty("temp.dir"), application.getProperty("temp.prefix"), i);
            FileUtils.createOrClearDir(Paths.get(application.getProperty("temp.dir")));
            futures.add(CompletableFuture.runAsync(()->seek(FileUtils.copy(compressedFile, file)))
                    .exceptionally(ex->{
                        log.error("[runAsync] Termiated w/exception",ex);
                        return null;
                    }));

        }
        return futures;
    }

    private void seek(File compressedFile){
        log.debug("[seek] init");
        while(!FileUtils.exists(RESULT_FILE)){
            tryExtract(compressedFile);
        }
        log.debug("[seek] end");
    }

    private void tryExtract(File compressedFile){
        generator.getNext().ifPresentOrElse(pwd -> {
            boolean result = false;
            try {
                result = RarExtractor.getInstance()
                        .extractWithPassword(compressedFile.getAbsolutePath(), Paths.get(application.getProperty("temp.dir")).toFile().getAbsolutePath(), pwd);
            }
            catch(Exception e){
                log.error("[tryExtract] Exception", e);
            }
            log.debug("[tryExtract] result: "+ result + " pwd: " + pwd);
            if(result) {
                log.info("[tryExtract] pwd: " + pwd);
                FileUtils.createFile(RESULT_FILE, pwd);
            }
        },()->FileUtils.createFile(RESULT_FILE,""));
    }
}
