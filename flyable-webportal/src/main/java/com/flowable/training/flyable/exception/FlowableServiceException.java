package com.flowable.training.flyable.exception;

/**
 * Exception thrown when an error/bad status code occurs in Flowable Work.
 */
public class FlowableServiceException extends RuntimeException {
    private final int statusCode;

    public FlowableServiceException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}