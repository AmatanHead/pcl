package com.github.amatanhead.pcl.token;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    private final TokenKind EOF = TokenKind.EOF;

    @Test
    public void constructors() {
        Token token;

        token = new Token(EOF);
        assertEquals(token.getTokenKind(), EOF);
        assertEquals(token.getData(), "");
        assertEquals(token.getRow(), 0);
        assertEquals(token.getColumn(), 0);

        token = new Token(EOF, "Data");
        assertEquals(token.getTokenKind(), EOF);
        assertEquals(token.getData(), "Data");
        assertEquals(token.getRow(), 0);
        assertEquals(token.getColumn(), 0);

        token = new Token(EOF, "Data 2", 10, 25);
        assertEquals(token.getTokenKind(), EOF);
        assertEquals(token.getData(), "Data 2");
        assertEquals(token.getRow(), 10);
        assertEquals(token.getColumn(), 25);

        Token defaultToken = new Token(EOF, "Data X", 10, 25);

        token = new Token(defaultToken);
        assertEquals(token.getTokenKind(), EOF);
        assertEquals(token.getData(), "Data X");
        assertEquals(token.getRow(), 10);
        assertEquals(token.getColumn(), 25);

        token = new Token(defaultToken, 50, 60);
        assertEquals(token.getTokenKind(), EOF);
        assertEquals(token.getData(), "Data X");
        assertEquals(token.getRow(), 50);
        assertEquals(token.getColumn(), 60);
    }

    @Test
    public void equality() {
        Token base = new Token(EOF, "Data 2", 10, 25);

        assertEquals(base, new Token(EOF, "Data 2", 10, 25));
        assertNotEquals(base, new Token(new TokenKind("TK"), "Data 2", 10, 25));
        assertNotEquals(base, new Token(EOF, "Data 3", 10, 25));
        assertNotEquals(base, new Token(EOF, "Data 2", 11, 25));
        assertNotEquals(base, new Token(EOF, "Data 2", 10, 24));
        assertNotEquals(base, null);
        assertNotEquals(base, "string");
    }
}
