package com.testForCS.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {

    }

    public NotFoundException(String resourceName, Long resourceId) {
        super(resourceName + " with id " + resourceId + " not found.");
    }

}
