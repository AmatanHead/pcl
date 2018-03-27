package com.github.amatanhead.pcl.token;

import java.util.Objects;

/**
 * Token represents a single entity of a text that's being parsed.
 * <p>
 * Each token is a combination of its kind (e.g. word, number, EOL) and an arbitrary string which contains
 * a part of the original text which rendered this token. For example, a word token will contain a word as its data.
 * <p>
 * Tokens can cary information about position in the original text on which they were rendered.
 * <p>
 * Tokens are immutable.
 */
public class Token {
    private final TokenKind tokenKind;
    private final String data;
    private long row;
    private long column;

    /**
     * Construct a token with an empty string as its data and zero position.
     */
    public Token(TokenKind tokenKind) {
        this(tokenKind, "");
    }

    /**
     * Construct a token with zero position.
     */
    public Token(TokenKind tokenKind, String data) {
        this(tokenKind, data, 0, 0);
    }

    /**
     * Construct a full token.
     */
    public Token(TokenKind tokenKind, String data, long row, long column) {
        this.tokenKind = tokenKind;
        this.data = data;
        this.row = row;
        this.column = column;
    }

    /**
     * Copy the given token.
     */
    public Token(Token token) {
        this(token.tokenKind, token.data, token.row, token.column);
    }

    /**
     * Copy the given token and replace its position.
     */
    public Token(Token token, long row, long column) {
        this(token.tokenKind, token.data, row, column);
    }

    /**
     * Get kind associated with the given token.
     */
    public TokenKind getTokenKind() {
        return tokenKind;
    }

    /**
     * Get data associated with the given token.
     */
    public String getData() {
        return data;
    }

    /**
     * Get row of the original text on which the given token was rendered.
     */
    public long getRow() {
        return row;
    }

    /**
     * Get column of the original text on which the given token was rendered.
     */
    public long getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Token(" + tokenKind + " at " + row + ":" + column + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return row == token.row &&
                column == token.column &&
                Objects.equals(tokenKind, token.tokenKind) &&
                Objects.equals(data, token.data);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tokenKind, data, row, column);
    }
}
