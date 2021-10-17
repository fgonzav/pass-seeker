package com.pass.seeker.service;

import com.pass.seeker.password.PasswordGenerator;
import com.pass.seeker.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.pass.seeker.constant.PasswordConstants.LUP_PATH;

@Slf4j
public class SavePasswordService {

    private static final PasswordGenerator generator = PasswordGenerator.getInstance();

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static final Timer timer = new Timer();


    private SavePasswordService(){

    }

    public static void start(){
        log.debug("[start] init");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SavePasswordService.saveLastUsedPassword();
            }
        },10000,10000);

        log.debug("[start] end");
    }

    private static void saveLastUsedPassword(){
        String lup = generator.getLastUsed();
        log.info("[saveLastUsedPassword] lup: " + lup);

        FileUtils.createFile(LUP_PATH,lup);
    }

    public static void stop(){
        timer.cancel();
    }
}
