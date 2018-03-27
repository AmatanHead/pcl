package com.github.amatanhead.pcl.errors;

/**
 * Backtracking stack is empty. No token available.
 */
public class NoBacktrackingTokenError extends LexerProtocolError {
    public NoBacktrackingTokenError() {
        super();
    }

    public NoBacktrackingTokenError(String message) {
        super(message);
    }

    public NoBacktrackingTokenError(String message, Throwable cause) {
        super(message, cause);
    }
}
