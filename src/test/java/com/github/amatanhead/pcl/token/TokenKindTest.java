package com.github.amatanhead.pcl.token;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenKindTest {
    @Test
    public void constructors() {
        TokenKind tokenKind = new TokenKind("Name");

        assertEquals(tokenKind.getName(), "Name");
    }

    @Test
    public void predefinedTokenKinds() {
        assertTrue(TokenKind.EOF.getName().equals("EOF"));
    }
}
