package com.github.amatanhead.pcl.lexer;

import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.TokenStream;
import com.github.amatanhead.pcl.token.Token;
import org.junit.Before;
import org.junit.Test;
import com.github.amatanhead.pcl.token.TokenKind;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class RegexpTextLexerTest {
    private final TokenKind TOK1 = new TokenKind("TOK1");
    private final TokenKind TOK2 = new TokenKind("TOK2");
    private final TokenKind NAME = new TokenKind("NAME");
    private final TokenKind STRING = new TokenKind("STRING");
    private final TokenKind NUMBER = new TokenKind("NUMBER");
    private final TokenKind EQ_SIGN = new TokenKind("EQ_SIGN");
    private final TokenKind NEWLINE = new TokenKind("NEWLINE");

    private RegexpTextLexer lexer;

    @Before
    public void setUp() {
        lexer = new RegexpTextLexer();

        lexer.addRule(Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*"), NAME);
        lexer.addRule(Pattern.compile("'(\\\\.|[^\\\\.])*'"), STRING);
        lexer.addRule(Pattern.compile("\"(\\\\.|[^\\\\.])*\""), STRING);
        lexer.addRule(Pattern.compile("[0-9]+"), NUMBER);
        lexer.addRule(Pattern.compile("="), EQ_SIGN);
        lexer.addRule(Pattern.compile("\r?\n"), NEWLINE);
        lexer.addRule(Pattern.compile("\\s"), (TokenKind)null);
    }

    @Test
    public void addRule() throws TokenizationError {
        lexer = new RegexpTextLexer();

        lexer.addRule("[a-zA-Z_]+", TOK1);
        lexer.addRule("\\s", null);

        TokenStream stream = lexer.tokenize("asd dsa");

        assertEquals(new Token(TOK1, "asd", 0, 0), stream.input());
        assertEquals(new Token(TOK1, "dsa", 0, 4), stream.input());

        assertEquals(new Token(TokenKind.EOF, "", 0, 7), stream.input());

        assertFalse(stream.canInput());
    }

    @Test
    public void addRule1() throws TokenizationError {
        lexer = new RegexpTextLexer();

        lexer.addRule(Pattern.compile("[a-zA-Z_]+"), TOK1);
        lexer.addRule(Pattern.compile("\\s"), (TokenKind) null);

        TokenStream stream = lexer.tokenize("asd dsa");

        assertEquals(new Token(TOK1, "asd", 0, 0), stream.input());
        assertEquals(new Token(TOK1, "dsa", 0, 4), stream.input());

        assertEquals(new Token(TokenKind.EOF, "", 0, 7), stream.input());

        assertFalse(stream.canInput());
    }

    @Test
    public void addRule2() throws TokenizationError {
        lexer = new RegexpTextLexer();

        lexer.addRule(Pattern.compile("[a-zA-Z_]+"), matcher -> new Token(TOK1, matcher.group()));
        lexer.addRule(Pattern.compile("\\s"), matcher -> null);

        TokenStream stream = lexer.tokenize("asd dsa");

        assertEquals(new Token(TOK1, "asd", 0, 0), stream.input());
        assertEquals(new Token(TOK1, "dsa", 0, 4), stream.input());

        assertEquals(new Token(TokenKind.EOF, "", 0, 7), stream.input());

        assertFalse(stream.canInput());
    }

    @Test
    public void tokenize() throws TokenizationError {
        String text = "" +
                "TEST_VAR_1 = 10\n" +
                "TEST_VAR_2 = 'test \\' string'\n" +
                "TEST_VAR_3 = \"test \\\" string\"\n";

        TokenStream stream = lexer.tokenize(text);

        assertEquals(new Token(NAME, "TEST_VAR_1", 0, 0), stream.input());
        assertEquals(new Token(EQ_SIGN, "=", 0,  11), stream.input());
        assertEquals(new Token(NUMBER, "10", 0,  13), stream.input());
        assertEquals(new Token(NEWLINE, "\n", 0,  15), stream.input());

        assertEquals(new Token(NAME, "TEST_VAR_2", 1, 0), stream.input());
        assertEquals(new Token(EQ_SIGN, "=", 1,  11), stream.input());
        assertEquals(new Token(STRING, "'test \\' string'", 1,  13), stream.input());
        assertEquals(new Token(NEWLINE, "\n", 1,  29), stream.input());

        assertEquals(new Token(NAME, "TEST_VAR_3", 2, 0), stream.input());
        assertEquals(new Token(EQ_SIGN, "=", 2,  11), stream.input());
        assertEquals(new Token(STRING, "\"test \\\" string\"", 2,  13), stream.input());
        assertEquals(new Token(NEWLINE, "\n", 2,  29), stream.input());

        assertEquals(new Token(TokenKind.EOF, "", 3, 0), stream.input());

        assertFalse(stream.canInput());
    }

    @Test
    public void tokenizationError()  throws TokenizationError {
        String text = "" +
                "TEST_VAR_1 = 10\n" +
                "TEST_VAR_2 = 'test \\' string";

        TokenStream stream = lexer.tokenize(text);

        assertEquals(new Token(NAME, "TEST_VAR_1", 0, 0), stream.input());
        assertEquals(new Token(EQ_SIGN, "=", 0,  11), stream.input());
        assertEquals(new Token(NUMBER, "10", 0,  13), stream.input());
        assertEquals(new Token(NEWLINE, "\n", 0,  15), stream.input());

        assertEquals(new Token(NAME, "TEST_VAR_2", 1, 0), stream.input());
        assertEquals(new Token(EQ_SIGN, "=", 1,  11), stream.input());

        try {
            assertEquals(new Token(STRING, "'test \\' string'", 1, 13), stream.input());
            fail("should've fail on malformed input");
        } catch (TokenizationError error) {
            // ok
        }
    }

    @Test
    public void tokenizationPriority() throws TokenizationError {
        lexer = new RegexpTextLexer();

        lexer.addRule("[a-zA-Z_]+", TOK1);
        lexer.addRule("[a-zA-Z_]+", TOK2);
        lexer.addRule("\\s", null);

        TokenStream stream = lexer.tokenize("asd dsa");

        assertEquals(new Token(TOK1, "asd", 0, 0), stream.input());
        assertEquals(new Token(TOK1, "dsa", 0, 4), stream.input());

        assertEquals(new Token(TokenKind.EOF, "", 0, 7), stream.input());

        assertFalse(stream.canInput());
    }

    // TODO test encoding and streams?
}
