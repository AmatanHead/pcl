package com.github.amatanhead.pcl.errors;

/**
 * Error encountered during tokenization. Input character sequence is invalid.
 */
public class TokenizationError extends ProcessingError {
    public final long row;
    public final long column;

    public TokenizationError() {
        this(0, 0);
    }

    public TokenizationError(long row, long column) {
        this(makeMessage(row, column), row, column);
    }

    public TokenizationError(String message) {
        this(message, 0, 0);
    }

    public TokenizationError(String message, long row, long column) {
        super(message);

        this.row = row;
        this.column = column;
    }

    static private String makeMessage(long row, long column) {
        return "tokenization error at " + row + ":" + column;
    }
}
