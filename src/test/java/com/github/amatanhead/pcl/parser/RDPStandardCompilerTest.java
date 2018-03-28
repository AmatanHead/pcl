package com.github.amatanhead.pcl.parser;

import com.github.amatanhead.pcl.combinators.ast.NDefer;
import com.github.amatanhead.pcl.errors.ParsingError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.StandardConversions;
import com.github.amatanhead.pcl.stream.TokenStreamStar;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.token.TokenKind;
import com.github.amatanhead.pcl.utils.Maybe;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.amatanhead.pcl.combinators.Combinators.*;
import static org.junit.Assert.*;

public class RDPStandardCompilerTest {
    private static final TokenKind TOK1 = new TokenKind("TOK1");
    private static final TokenKind TOK2 = new TokenKind("TOK2");

    private RDPStandardCompiler compiler;

    private static TokenStreamStar makeStream(Token... tokens) {
        return (TokenStreamStar) StandardConversions.toStream(new ArrayList<>(Arrays.asList(tokens)).listIterator());
    }

    private static ArrayList<Token> makeArray(Token... tokens) {
        return new ArrayList<>(Arrays.asList(tokens));
    }

    @Before
    public void setUp() {
        compiler = new RDPStandardCompiler();
    }

    @Test
    public void testA() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(a(TOK1));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals(new Token(TOK1, "data"), result.getResult());

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertFalse(result.isSuccess());
        assertNull(result.getResult());
    }

    @Test
    public void testAn() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(an(TOK1));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals(new Token(TOK1, "data"), result.getResult());

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertFalse(result.isSuccess());
        assertNull(result.getResult());
    }

    @Test
    public void testSome() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(some(token -> token.getTokenKind() == TOK1 && token.getData().equals("data")));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals(new Token(TOK1, "data"), result.getResult());

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "data")));
        assertFalse(result.isSuccess());
        assertNull(result.getResult());

        result = ast.parse(makeStream(new Token(TOK1, "")));
        assertFalse(result.isSuccess());
        assertNull(result.getResult());
    }

    @Test
    public void testApply() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(a(TOK1).bind(Token::getData));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals("data", result.getResult());

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertFalse(result.isSuccess());
        assertNull(result.getResult());
    }

    @Test
    public void testMany() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(many(a(TOK1)));
        RDPResult result;

        result = ast.parse(makeStream());
        assertTrue(result.isSuccess());
        assertEquals(makeArray(), result.getResult());

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals(makeArray(new Token(TOK1, "data")), result.getResult());

        result = ast.parse(makeStream(new Token(TOK1, "data"), new Token(TOK1, "data2")));
        assertTrue(result.isSuccess());
        assertEquals(makeArray(new Token(TOK1, "data"), new Token(TOK1, "data2")), result.getResult());

        result = ast.parse(makeStream(new Token(TOK1, "data"), new Token(TOK1, "data2"), new Token(TokenKind.EOF, "")));
        assertTrue(result.isSuccess());
        assertEquals(makeArray(new Token(TOK1, "data"), new Token(TOK1, "data2")), result.getResult());
    }

    @Test
    public void testManyRestricted() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(many(a(TOK1), 1, 2));
        RDPResult result;

        result = ast.parse(makeStream());
        assertFalse(result.isSuccess());

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals(makeArray(new Token(TOK1, "data")), result.getResult());

        result = ast.parse(makeStream(new Token(TOK1, "data"), new Token(TOK1, "data2")));
        assertTrue(result.isSuccess());
        assertEquals(makeArray(new Token(TOK1, "data"), new Token(TOK1, "data2")), result.getResult());

        result = ast.parse(makeStream(new Token(TOK1, "data"), new Token(TOK1, "data2"), new Token(TokenKind.EOF, "")));
        assertTrue(result.isSuccess());
        assertEquals(makeArray(new Token(TOK1, "data"), new Token(TOK1, "data2")), result.getResult());
    }

    @Test
    public void testMaybe() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(maybe(a(TOK1)));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals(new Maybe<>(new Token(TOK1, "data")), result.getResult());

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertTrue(result.isSuccess());
        assertEquals(new Maybe<Token>(), result.getResult());
    }

    @Test
    public void testOr() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(or(a(TOK1), a(TOK2)));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals(new Token(TOK1, "data"), result.getResult());

        result = ast.parse(makeStream(new Token(TOK2, "data")));
        assertTrue(result.isSuccess());
        assertEquals(new Token(TOK2, "data"), result.getResult());

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertFalse(result.isSuccess());
    }

    @Test
    public void testSeq() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(seq(a(TOK1), a(TOK2)));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK1, "data"), new Token(TOK2, "data2")));
        assertTrue(result.isSuccess());
        assertEquals(makeArray(new Token(TOK1, "data"), new Token(TOK2, "data2")), result.getResult());

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertFalse(result.isSuccess());

        result = ast.parse(makeStream(new Token(TOK2, "data2")));
        assertFalse(result.isSuccess());

        result = ast.parse(makeStream(new Token(TOK1, "data"), new Token(TokenKind.EOF, "")));
        assertFalse(result.isSuccess());
    }

    @Test
    public void testDefer() throws TokenizationError, ParsingError {
        NDefer<Token> deferred = defer();
        deferred.setDeferred(or(a(TOK1), deferred));

        RDPCompiledAST ast = compiler.compile(deferred);
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK1, "data")));
        assertTrue(result.isSuccess());
        assertEquals(new Token(TOK1, "data"), result.getResult());
    }
}
