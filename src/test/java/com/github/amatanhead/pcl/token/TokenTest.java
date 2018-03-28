package com.github.amatanhead.pcl.token;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    private final TokenKind EOF = TokenKind.EOF;

    @Test
    public void constructors() {
        Token token;

        token = new Token(EOF);
        assertEquals(EOF, token.getTokenKind());
        assertEquals("", token.getData());
        assertEquals(0, token.getRow());
        assertEquals(0, token.getColumn());

        token = new Token(EOF, "Data");
        assertEquals(EOF, token.getTokenKind());
        assertEquals("Data", token.getData());
        assertEquals(0, token.getRow());
        assertEquals(0, token.getColumn());

        token = new Token(EOF, "Data 2", 10, 25);
        assertEquals(EOF, token.getTokenKind());
        assertEquals("Data 2", token.getData());
        assertEquals(10, token.getRow());
        assertEquals(25, token.getColumn());

        Token defaultToken = new Token(EOF, "Data X", 10, 25);

        token = new Token(defaultToken);
        assertEquals(EOF, token.getTokenKind());
        assertEquals("Data X", token.getData());
        assertEquals(10, token.getRow());
        assertEquals(25, token.getColumn());

        token = new Token(defaultToken, 50, 60);
        assertEquals(EOF, token.getTokenKind());
        assertEquals("Data X", token.getData());
        assertEquals(50, token.getRow());
        assertEquals(60, token.getColumn());
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
