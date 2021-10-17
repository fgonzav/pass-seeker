package com.pass.seeker.exception;

public class ExtractorException extends RuntimeException{

    public ExtractorException(Exception e){
        super(e);
    }

    public ExtractorException(String message){
        super(message);
    }
}
