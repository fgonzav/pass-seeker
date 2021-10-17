package com.pass.seeker.exception;

public class ConfigurationException extends RuntimeException{

    public ConfigurationException(Throwable e){
        super(e);
    }

    public ConfigurationException(String message){
        super(message);
    }

}
