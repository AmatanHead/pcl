package com.github.amatanhead.pcl.parser;

import com.github.amatanhead.pcl.combinators.ast.AST;
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
    private static final TokenKind TOK = new TokenKind("TOK");

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
        RDPCompiledAST ast = compiler.compile(a(TOK));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK, "data")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), new Token(TOK, "data"));

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertFalse(result.isSuccess());
        assertEquals(result.getResult(), null);
    }

    @Test
    public void testAn() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(an(TOK));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK, "data")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), new Token(TOK, "data"));

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertFalse(result.isSuccess());
        assertEquals(result.getResult(), null);
    }

    @Test
    public void testSome() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(some(token -> token.getTokenKind() == TOK && token.getData().equals("data")));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK, "data")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), new Token(TOK, "data"));

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "data")));
        assertFalse(result.isSuccess());
        assertEquals(result.getResult(), null);

        result = ast.parse(makeStream(new Token(TOK, "")));
        assertFalse(result.isSuccess());
        assertEquals(result.getResult(), null);
    }

    @Test
    public void testApply() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(a(TOK).bind(Token::getData));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK, "data")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), "data");

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertFalse(result.isSuccess());
        assertEquals(result.getResult(), null);
    }

    @Test
    public void testMany() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(many(a(TOK)));
        RDPResult result;

        result = ast.parse(makeStream());
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), makeArray());

        result = ast.parse(makeStream(new Token(TOK, "data")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), makeArray(new Token(TOK, "data")));

        result = ast.parse(makeStream(new Token(TOK, "data"), new Token(TOK, "data2")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), makeArray(new Token(TOK, "data"), new Token(TOK, "data2")));

        result = ast.parse(makeStream(new Token(TOK, "data"), new Token(TOK, "data2"), new Token(TokenKind.EOF, "")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), makeArray(new Token(TOK, "data"), new Token(TOK, "data2")));
    }

    @Test
    public void testManyRestricted() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(many(a(TOK), 1, 2));
        RDPResult result;

        result = ast.parse(makeStream());
        assertFalse(result.isSuccess());

        result = ast.parse(makeStream(new Token(TOK, "data")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), makeArray(new Token(TOK, "data")));

        result = ast.parse(makeStream(new Token(TOK, "data"), new Token(TOK, "data2")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), makeArray(new Token(TOK, "data"), new Token(TOK, "data2")));

        result = ast.parse(makeStream(new Token(TOK, "data"), new Token(TOK, "data2"), new Token(TokenKind.EOF, "")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), makeArray(new Token(TOK, "data"), new Token(TOK, "data2")));
    }

    @Test
    public void testMaybe() throws TokenizationError, ParsingError {
        RDPCompiledAST ast = compiler.compile(maybe(a(TOK)));
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK, "data")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), new Maybe<Token>(new Token(TOK, "data")));

        result = ast.parse(makeStream(new Token(TokenKind.EOF, "")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), new Maybe<Token>());
    }

    @Test
    public void testOr() throws TokenizationError, ParsingError {

    }

    @Test
    public void testSeq() throws TokenizationError, ParsingError {

    }

    @Test
    public void testDefer() throws TokenizationError, ParsingError {
        NDefer<Token> deferred = defer();
        deferred.setDeferred(or1(a(TOK), deferred));

        RDPCompiledAST ast = compiler.compile(deferred);
        RDPResult result;

        result = ast.parse(makeStream(new Token(TOK, "data")));
        assertTrue(result.isSuccess());
        assertEquals(result.getResult(), new Token(TOK, "data"));
    }
}
