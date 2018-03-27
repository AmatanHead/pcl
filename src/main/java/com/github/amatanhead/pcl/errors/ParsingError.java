package com.github.amatanhead.pcl.errors;

/**
 * Error encountered during parsing. Input token sequence is invalid.
 */
public class ParsingError extends ProcessingError {
    public final long row;
    public final long column;

    public ParsingError() {
        this(0, 0);
    }

    public ParsingError(long row, long column) {
        this(makeMessage(row, column), row, column);
    }

    public ParsingError(String message) {
        this(message, 0, 0);
    }

    public ParsingError(String message, long row, long column) {
        super(message);

        this.row = row;
        this.column = column;
    }

    static private String makeMessage(long row, long column) {
        return "parsing error at " + row + ":" + column;
    }
}
