package com.github.amatanhead.pcl.stream;

import com.github.amatanhead.pcl.errors.LexerProtocolError;
import com.github.amatanhead.pcl.errors.NoBacktrackingTokenError;
import com.github.amatanhead.pcl.errors.NoNewTokenError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.token.TokenKind;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.Consumer;

import static org.junit.Assert.*;


class IteratorDelegate<T> implements Iterator<T> {
    private final ListIterator<T> iterator;

    IteratorDelegate(ListIterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        iterator.forEachRemaining(action);
    }
}

public class StandardConversionsTest {
    static private final TokenKind TOK1 = new TokenKind("TOK1");
    static private final TokenKind TOK2 = new TokenKind("TOK2");
    static private final TokenKind TOK3 = new TokenKind("TOK3");
    static private final TokenKind BT = new TokenKind("BT");
    static private final TokenKind EOF = TokenKind.EOF;

    private LinkedList<Token> makeTokens() {
        LinkedList<Token> tokens = new LinkedList<>();

        tokens.add(new Token(TOK1, "Data 1"));
        tokens.add(new Token(TOK2, "Data 2"));
        tokens.add(new Token(TOK3, "Data 3"));
        tokens.add(new Token(EOF));

        return tokens;
    }

    private TokenStream makeTokenStreamFromIterator() {
        Iterator<Token> iterator = new IteratorDelegate<>(makeTokens().listIterator());
        return StandardConversions.toStream(iterator);
    }

    private TokenStream makeTokenStreamFromListIterator() {
        Iterator<Token> iterator = makeTokens().listIterator();
        return StandardConversions.toStream(iterator);
    }

    static private void assertToken(String message, TokenKind expectedKind, Token actual) {
        assertEquals(message, expectedKind, actual.getTokenKind());
    }

    static private void assertToken(TokenKind expectedKind, Token actual) {
        assertEquals(expectedKind, actual.getTokenKind());
    }

    static private void assertToken(String message, TokenKind expectedKind, String expectedData, Token actual) {
        assertEquals(message, expectedKind, actual.getTokenKind());
        assertEquals(message, expectedData, actual.getData());
    }

    static private void assertToken(TokenKind expectedKind, String expectedData, Token actual) {
        assertEquals(expectedKind, actual.getTokenKind());
        assertEquals(expectedData, actual.getData());
    }

    static private void testTokenStreamInput(TokenStream tokenStream) throws TokenizationError {

        assertToken(TOK1, "Data 1", tokenStream.input());

        assertToken(TOK2, "Data 2", tokenStream.input());

        assertToken(TOK3, "Data 3", tokenStream.input());

        assertToken(EOF, tokenStream.input());


        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
    }

    static private void testTokenStreamCanInput(TokenStream tokenStream) throws TokenizationError {
        assertTrue(tokenStream.canInput());

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(TOK3, "Data 3", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
    }

    static private void testTokenStreamNUnput(TokenStreamN tokenStream) throws TokenizationError {

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 1", tokenStream.unput(new Token(BT, "Backtracked 1")));
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 1", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 2", tokenStream.unput(new Token(BT, "Backtracked 2")));
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 3", tokenStream.unput(new Token(BT, "Backtracked 3")));
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 3", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 2", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(TOK3, "Data 3", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());

        assertToken(BT, "Backtracked 4", tokenStream.unput(new Token(BT, "Backtracked 4")));
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 5", tokenStream.unput(new Token(BT, "Backtracked 5")));
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 5", tokenStream.input());
        assertTrue(tokenStream.canInput());

        assertToken(BT, "Backtracked 4", tokenStream.input());
        assertFalse(tokenStream.canInput());


        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no new nor backtracked tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
    }

    static private void testTokenStream1Unput(TokenStream1 tokenStream) throws TokenizationError {
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        try {
            tokenStream.unput();
            fail("TokenStream.Unput() should fail in the beginning of the stream");
        } catch (NoBacktrackingTokenError ignored) {
            // ok
        }
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        try {

            tokenStream.unput();
            fail("TokenStream.Unput() should fail if called two times in a row");
        } catch (NoBacktrackingTokenError ignored) {
            // ok
        }
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK3, "Data 3", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no new nor backtracked tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no new nor backtracked tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());
    }
    static private void testTokenStreamStarUnput(TokenStreamStar tokenStream) throws TokenizationError {
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        try {

            tokenStream.unput();
            fail("TokenStream.Unput() should fail in the beginning of the stream");
        } catch (NoBacktrackingTokenError ignored) {
            // ok
        }
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        try {

            tokenStream.unput();
            fail("TokenStream.Unput() should fail if reached the beginning of the stream");
        } catch (NoBacktrackingTokenError ignored) {
            // ok
        }
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK3, "Data 3", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK3, "Data 3", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        try {
            tokenStream.unput();
            fail("TokenStream.Unput() should fail if reached the beginning of the stream");
        } catch (NoBacktrackingTokenError ignored) {
            // ok
        }
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK3, "Data 3", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no new nor backtracked tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no new nor backtracked tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());
    }
    static private void testTokenStreamStarBookmark(TokenStreamStar tokenStream) throws TokenizationError {
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        TokenStreamStar.Bookmark b1 = tokenStream.makeBookmark();
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        TokenStreamStar.Bookmark b2 = tokenStream.makeBookmark();
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK3, "Data 3", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no new nor backtracked tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        TokenStreamStar.Bookmark b3 = tokenStream.makeBookmark();
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        tokenStream.restoreBookmark(b2);
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK3, "Data 3", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK3, "Data 3", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.unput());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        tokenStream.restoreBookmark(b1);
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        assertToken(TOK1, "Data 1", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        tokenStream.restoreBookmark(b3);
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no new nor backtracked tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        tokenStream.restoreBookmark(b1);
        assertTrue(tokenStream.canInput());
        assertFalse(tokenStream.canUnput());

        tokenStream.restoreBookmark(b2);
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK2, "Data 2", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(TOK3, "Data 3", tokenStream.input());
        assertTrue(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        assertToken(EOF, tokenStream.input());
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());

        try {
            tokenStream.input();
            fail("TokenStream.canInput() should fail when there are no new nor backtracked tokens left");
        } catch (NoNewTokenError ignored) {
            // ok
        }
        assertFalse(tokenStream.canInput());
        assertTrue(tokenStream.canUnput());
    }

    static private TokenStream makeDirty(TokenStream tokenStream) throws TokenizationError {
        tokenStream.input();
        return tokenStream;
    }

    static private void assertNonConvertible(TokenStream tokenStream) {
        try {
            StandardConversions.toStream(tokenStream);
            fail("StandardConversions.toStream should fail on unclear streams");
        } catch (LexerProtocolError ignored) {
            // ok
        }

        try {
            StandardConversions.toStream1(tokenStream);
            fail("StandardConversions.toStream1 should fail on unclear streams");
        } catch (LexerProtocolError ignored) {
            // ok
        }

        try {
            StandardConversions.toStreamN(tokenStream);
            fail("StandardConversions.toStreamN should fail on unclear streams");
        } catch (LexerProtocolError ignored) {
            // ok
        }

        try {
            StandardConversions.toStreamStar(tokenStream);
            fail("StandardConversions.toStreamStar should fail on unclear streams");
        } catch (LexerProtocolError ignored) {
            // ok
        }
    }

    @Test
    public void toStream() {
        TokenStream stream;

        stream = makeTokenStreamFromIterator();
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = makeTokenStreamFromListIterator();
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = StandardConversions.toStream(makeTokenStreamFromIterator());
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = StandardConversions.toStream(makeTokenStreamFromListIterator());
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = StandardConversions.toStream1(makeTokenStreamFromIterator());
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = StandardConversions.toStream1(makeTokenStreamFromListIterator());
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = StandardConversions.toStreamN(makeTokenStreamFromIterator());
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = StandardConversions.toStreamN(makeTokenStreamFromListIterator());
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = StandardConversions.toStreamStar(makeTokenStreamFromIterator());
        assertEquals(stream, StandardConversions.toStream(stream));

        stream = StandardConversions.toStreamStar(makeTokenStreamFromListIterator());
        assertEquals(stream, StandardConversions.toStream(stream));
    }

    @Test
    public void toStreamForIterator() throws TokenizationError {
        assertFalse(makeTokenStreamFromIterator() instanceof TokenStream1);
        assertFalse(makeTokenStreamFromIterator() instanceof TokenStreamStar);
        assertFalse(makeTokenStreamFromIterator() instanceof TokenStreamN);

        testTokenStreamInput(makeTokenStreamFromIterator());
        testTokenStreamCanInput(makeTokenStreamFromIterator());

        assertTrue(makeTokenStreamFromListIterator() instanceof TokenStream1);
        assertTrue(makeTokenStreamFromListIterator() instanceof TokenStreamStar);
        assertFalse(makeTokenStreamFromListIterator() instanceof TokenStreamN);

        testTokenStreamInput(makeTokenStreamFromListIterator());
        testTokenStreamCanInput(makeTokenStreamFromListIterator());
        testTokenStreamStarUnput((TokenStreamStar) makeTokenStreamFromListIterator());
        testTokenStreamStarBookmark((TokenStreamStar) makeTokenStreamFromListIterator());
    }

    @Test
    public void toStream1() throws TokenizationError {
        assertFalse(StandardConversions.toStream1(makeTokenStreamFromIterator()) instanceof TokenStreamStar);
        assertFalse(StandardConversions.toStream1(makeTokenStreamFromIterator()) instanceof TokenStreamN);

        testTokenStreamInput(StandardConversions.toStream1(makeTokenStreamFromIterator()));
        testTokenStreamCanInput(StandardConversions.toStream1(makeTokenStreamFromIterator()));
        testTokenStream1Unput(StandardConversions.toStream1(makeTokenStreamFromIterator()));

        TokenStream stream = makeTokenStreamFromListIterator();

        assertEquals("conversions don't wrap classes that implement interfaces hither than requested",
                stream, StandardConversions.toStream1(stream));

        assertTrue(StandardConversions.toStream1(makeTokenStreamFromListIterator()) instanceof TokenStreamStar);
        assertFalse(StandardConversions.toStream1(makeTokenStreamFromListIterator()) instanceof TokenStreamN);

        testTokenStreamInput(StandardConversions.toStream1(makeTokenStreamFromListIterator()));
        testTokenStreamCanInput(StandardConversions.toStream1(makeTokenStreamFromListIterator()));
        testTokenStreamStarUnput((TokenStreamStar) StandardConversions.toStream1(makeTokenStreamFromListIterator()));
        testTokenStreamStarBookmark((TokenStreamStar) StandardConversions.toStream1(makeTokenStreamFromListIterator()));
    }

    @Test
    public void toStreamN() throws TokenizationError {
        assertFalse(StandardConversions.toStreamN(makeTokenStreamFromIterator()) instanceof TokenStream1);
        assertFalse(StandardConversions.toStreamN(makeTokenStreamFromIterator()) instanceof TokenStreamStar);

        testTokenStreamInput(StandardConversions.toStreamN(makeTokenStreamFromIterator()));
        testTokenStreamCanInput(StandardConversions.toStreamN(makeTokenStreamFromIterator()));
        testTokenStreamNUnput(StandardConversions.toStreamN(makeTokenStreamFromIterator()));

        assertFalse(StandardConversions.toStreamN(makeTokenStreamFromListIterator()) instanceof TokenStream1);
        assertFalse(StandardConversions.toStreamN(makeTokenStreamFromListIterator()) instanceof TokenStreamStar);

        testTokenStreamInput(StandardConversions.toStreamN(makeTokenStreamFromListIterator()));
        testTokenStreamCanInput(StandardConversions.toStreamN(makeTokenStreamFromListIterator()));
        testTokenStreamNUnput(StandardConversions.toStreamN(makeTokenStreamFromListIterator()));

        TokenStream stream = StandardConversions.toStreamN(makeTokenStreamFromIterator());

        assertEquals("conversions don't wrap classes that implement interfaces hither than requested",
                stream, StandardConversions.toStreamN(stream));
    }

    @Test
    public void toStreamStar() throws TokenizationError {
        assertFalse(StandardConversions.toStreamStar(makeTokenStreamFromIterator()) instanceof TokenStreamN);
        testTokenStreamInput(StandardConversions.toStreamStar(makeTokenStreamFromIterator()));

        testTokenStreamCanInput(StandardConversions.toStreamStar(makeTokenStreamFromIterator()));
        testTokenStreamStarUnput(StandardConversions.toStreamStar(makeTokenStreamFromIterator()));
        testTokenStreamStarBookmark(StandardConversions.toStreamStar(makeTokenStreamFromIterator()));

        TokenStream stream = makeTokenStreamFromListIterator();

        assertEquals("conversions don't wrap classes that implement interfaces hither than requested",
                stream, StandardConversions.toStreamStar(stream));

        assertFalse(StandardConversions.toStreamStar(makeTokenStreamFromListIterator()) instanceof TokenStreamN);
        testTokenStreamInput(StandardConversions.toStreamStar(makeTokenStreamFromListIterator()));

        testTokenStreamCanInput(StandardConversions.toStreamStar(makeTokenStreamFromListIterator()));
        testTokenStreamStarUnput(StandardConversions.toStreamStar(makeTokenStreamFromListIterator()));
        testTokenStreamStarBookmark(StandardConversions.toStreamStar(makeTokenStreamFromListIterator()));
    }

    @Test
    public void convertDirty() throws TokenizationError {
        assertNonConvertible(makeDirty(makeTokenStreamFromIterator()));
        assertNonConvertible(makeDirty(makeTokenStreamFromListIterator()));

        assertNonConvertible(makeDirty(StandardConversions.toStream(makeTokenStreamFromIterator())));
        assertNonConvertible(makeDirty(StandardConversions.toStream(makeTokenStreamFromListIterator())));

        assertNonConvertible(makeDirty(StandardConversions.toStream1(makeTokenStreamFromIterator())));
        assertNonConvertible(makeDirty(StandardConversions.toStream1(makeTokenStreamFromListIterator())));

        assertNonConvertible(makeDirty(StandardConversions.toStreamN(makeTokenStreamFromIterator())));
        assertNonConvertible(makeDirty(StandardConversions.toStreamN(makeTokenStreamFromListIterator())));

        assertNonConvertible(makeDirty(StandardConversions.toStreamStar(makeTokenStreamFromIterator())));
        assertNonConvertible(makeDirty(StandardConversions.toStreamStar(makeTokenStreamFromListIterator())));
    }
}
