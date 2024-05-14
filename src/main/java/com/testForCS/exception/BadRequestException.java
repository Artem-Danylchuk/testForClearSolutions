package com.testForCS.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
    }

    public BadRequestException(String resourceMessage) {
        super("Are date -" + resourceMessage + " is invalid");
    }
}
