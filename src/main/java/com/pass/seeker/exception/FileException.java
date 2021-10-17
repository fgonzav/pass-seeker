package com.pass.seeker.exception;

public class FileException extends RuntimeException{

    public FileException(Exception e){
        super(e);
    }

    public FileException(String message){
        super(message);
    }

}
