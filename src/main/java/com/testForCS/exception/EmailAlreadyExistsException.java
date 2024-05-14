package com.testForCS.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
    }

    public EmailAlreadyExistsException(String resourceEmail) {
        super("Sorry - " + resourceEmail + " already used.");
    }
}
