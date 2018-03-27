package com.github.amatanhead.pcl.errors;

/**
 * An incorrect bookmark passed to stream.
 */
public class BookmarkError extends LexerProtocolError {
    public BookmarkError() {
        super();
    }

    public BookmarkError(String message) {
        super(message);
    }

    public BookmarkError(String message, Throwable cause) {
        super(message, cause);
    }
}
