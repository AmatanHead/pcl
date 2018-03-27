package com.github.amatanhead.pcl.errors;

/**
 * Error when working with token stream. Usually means bug in parser.
 */
public class LexerProtocolError extends RuntimeException {
    public LexerProtocolError() {
        super();
    }

    public LexerProtocolError(String message) {
        super(message);
    }

    public LexerProtocolError(String message, Throwable cause) {
        super(message, cause);
    }
}
