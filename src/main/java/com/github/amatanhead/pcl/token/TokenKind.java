package com.github.amatanhead.pcl.token;

/**
 * Token kind represents a class of tokens. Token kinds used to compare tokens and match them in parsers.
 * Essentially, token kind is a single word in a grammar. Example token kinds are 'word', 'keyword', 'newline', etc.
 * <p>
 * Token kinds are usually being static constants. Their names should be written in UPPERCASE, as you would white names
 * of all constants in java.
 * <p>
 * Note that there is a standard token kind for the end of file. Every lexer yields this token at the very end
 * of a token stream.
 */
public class TokenKind {
    private final String name;

    /**
     * Construct a new `TokenKind` with the given name. Note that token names are purely decorative, i.e. they're
     * used to print token kinds, nothing more. Token kinds compared via the standard java `==` operator.
     */
    public TokenKind(String name) {
        this.name = name;
    }

    /**
     * Get a name associated with this token kind.
     * Do not use token kind names for token kinds identification and comparison.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }

    /**
     * Standard End If File token kind.
     */
    static public final TokenKind EOF = new TokenKind("EOF");
}
