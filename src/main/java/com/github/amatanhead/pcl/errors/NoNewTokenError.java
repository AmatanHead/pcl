package com.github.amatanhead.pcl.errors;

/**
 * Token stream is empty. No new token available.
 */
public class NoNewTokenError extends LexerProtocolError {
    public NoNewTokenError() {
        super();
    }

    public NoNewTokenError(String message) {
        super(message);
    }

    public NoNewTokenError(String message, Throwable cause) {
        super(message, cause);
    }
}
