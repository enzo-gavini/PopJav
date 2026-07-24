package com.enzo.commentservice.exception;

/**
 * Thrown when a comment id does not exist in MongoDB, so the controller can
 * answer 404 instead of letting a generic runtime error become a 500.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
