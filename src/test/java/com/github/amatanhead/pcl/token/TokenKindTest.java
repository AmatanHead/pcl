package com.github.amatanhead.pcl.token;

import org.junit.Test;

import static org.junit.Assert.*;

public class TokenKindTest {
    @Test
    public void constructors() {
        TokenKind tokenKind = new TokenKind("Name");

        assertEquals("Name", tokenKind.getName());
    }

    @Test
    public void predefinedTokenKinds() {
        assertEquals("EOF", TokenKind.EOF.getName());
    }
}
