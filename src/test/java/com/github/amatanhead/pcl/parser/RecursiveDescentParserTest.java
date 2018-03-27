package com.github.amatanhead.pcl.parser;

import com.github.amatanhead.pcl.combinators.ast.AST;
import com.github.amatanhead.pcl.errors.ParsingError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.token.TokenKind;
import com.github.amatanhead.pcl.stream.StandardConversions;
import com.github.amatanhead.pcl.stream.TokenStream;
import org.junit.Test;

import java.util.ArrayList;

import static com.github.amatanhead.pcl.combinators.Combinators.*;
import static org.junit.Assert.*;

public class RecursiveDescentParserTest {
    static private final TokenKind TOK1 = new TokenKind("TOK1");
    static private final TokenKind TOK2 = new TokenKind("TOK2");
    static private final TokenKind TOK3 = new TokenKind("TOK3");
    static private final TokenKind BT = new TokenKind("BT");
    static private final TokenKind EOF = TokenKind.EOF;

    static private ArrayList<Token> makeTok1s(int n) {
        ArrayList<Token> tokens = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            tokens.add(new Token(TOK1, " " + i + " "));
        }
        tokens.add(new Token(EOF));
        return tokens;
    }

    static private AST<Token> tok1 = a(TOK1);
    static private AST<ArrayList<Token>> tok1s = many(tok1, 1, 5);
    static private AST<ArrayList<Token>> tok1sE = seq1(tok1s, a(EOF)).bind(t -> t.t1);
    static private AST<String> tok1sCount = tok1sE.bind(tokens -> tokens.stream().map(Token::getData).reduce("", String::concat));
    static private RecursiveDescentParser<String> parser = new RecursiveDescentParser<>(tok1sCount);

    TokenStream stream = StandardConversions.toStream(makeTok1s(5).listIterator());

    @Test
    public void test() throws TokenizationError, ParsingError {
        String s = parser.parse(stream);
        assertEquals(" 0  1  2  3  4 ", s);
    }
}
