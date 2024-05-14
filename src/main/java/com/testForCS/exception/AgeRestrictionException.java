package com.testForCS.exception;

public class AgeRestrictionException extends RuntimeException {
    public AgeRestrictionException() {
    }

    public AgeRestrictionException(String resourceName) {
        super("Client "+resourceName +" is not yet 18 years old.");
    }
}
