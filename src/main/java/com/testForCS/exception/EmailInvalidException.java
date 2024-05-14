package com.testForCS.exception;

public class EmailInvalidException extends RuntimeException{
    public EmailInvalidException() {
    }
    public EmailInvalidException(String resourceEmail){
        super(resourceEmail+" invalid email.");
    }
}
