package com.github.amatanhead.pcl.errors;

/**
 * Error when processing user input.
 */
public class ProcessingError extends Exception {
    public ProcessingError() {
        super();
    }

    public ProcessingError(String message) {
        super(message);
    }

    public ProcessingError(String message, Throwable cause) {
        super(message, cause);
    }
}
