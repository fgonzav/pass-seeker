/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.pass.seeker;

import com.pass.seeker.exception.ConfigurationException;
import com.pass.seeker.service.PassSeekerService;
import com.pass.seeker.service.SavePasswordService;
import com.pass.seeker.service.factory.PassSeekerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;

@Slf4j
public class App {

    public static void main(String[] args) {

        log.debug("[main] init: "+ Arrays.toString(args));

        if(args.length < 1)
            throw new ConfigurationException("How to use - java -jar pass-seeker-0.0.1-SNAPSHOT.jar path_to_compressed_file");

        File compressedFile = new File(args[0]);

        SavePasswordService.start();
        PassSeekerService seekerService = PassSeekerFactory.getInstance(compressedFile);
        seekerService.seekPassword(compressedFile);
        SavePasswordService.stop();
        log.debug("[main] end");
        System.exit(0);
    }
}
