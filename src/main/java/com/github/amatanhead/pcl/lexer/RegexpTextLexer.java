package com.github.amatanhead.pcl.lexer;

import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.token.TokenKind;
import com.github.amatanhead.pcl.stream.TokenStream;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a single lexer rule, i.e. a regexp and a token factory.
 */
final class Rule {
    private final Pattern pattern;
    private final Function<Matcher, Token> tokenFactory;

    Rule(Pattern pattern, Function<Matcher, Token> tokenFactory) {
        this.pattern = pattern;
        this.tokenFactory = tokenFactory;
    }

    Pattern getPattern() {
        return pattern;
    }

    Function<Matcher, Token> getTokenFactory() {
        return tokenFactory;
    }
}

/**
 * Token stream which actually does all tokenization (a lazy token stream).
 */
final class RegexpTextLexerTokenStream implements TokenStream {
    private final ArrayList<Rule> rules;
    private String text;
    private boolean isOver;
    private long currentRow;
    private long currentColumn;
    private boolean clear;

    RegexpTextLexerTokenStream(ArrayList<Rule> rules, String text) {
        this.rules = rules;
        this.text = text;
        this.isOver = false;
        this.currentRow = 0;
        this.currentColumn = 0;
        this.clear = true;
    }

    @Override
    public Token input() throws TokenizationError {
        if (isOver) {
            throw new java.util.NoSuchElementException("no elements left in stream");
        }

        clear = false;

        if (text.isEmpty()) {
            isOver = true;
            return new Token(TokenKind.EOF, "", currentRow, currentColumn);
        }

        for (Rule rule : rules) {
            final Matcher matcher = rule.getPattern().matcher(text);
            if (matcher.lookingAt()) {
                final long rows = matcher.group().chars().filter(ch -> ch == '\n').count();
                final long columns = matcher.group().substring(matcher.group().lastIndexOf('\n') + 1).length();
                final int endPos = matcher.end();

                Token token = rule.getTokenFactory().apply(matcher);

                if (token != null) {
                    token = new Token(token, currentRow, currentColumn);
                }

                currentRow += rows;
                if (rows > 0) {
                    currentColumn = 0;
                }
                currentColumn += columns;

                text = text.substring(endPos);

                if (token == null) {
                    return input();
                } else {
                    return token;
                }
            }
        }

        throw new TokenizationError(currentRow, currentColumn);
    }

    @Override
    public boolean canInput() {
        return !isOver;
    }

    @Override
    public boolean isClear() {
        return clear;
    }
}

/**
 * A simple lexer based on regular expressions.
 * <p>
 * Given a list of matching rules (namely regular expressions and associated token factories), tries to match
 * the beginning of the text with each regular expression sequentially, in the order of registration.
 * Once matched, builds a new token and continues from the position where the match ended.
 */
public class RegexpTextLexer extends AbstractTextLexer {
    private final ArrayList<Rule> rules = new ArrayList<>();

    /**
     * Register a new matching rule.
     * <p>
     * This rule checks the beginning of a text against the given regexp pattern. If matches, yields a token
     * with the given token kind. The token's data will be set to the matched prefix of a text.
     * <p>
     * Passing null as a token kind disables token generation. The rule will be processed as usual (i.e. the current
     * position in a text will be updated upon match) except that no token will be added to the token stream.
     * This is useful to suppress insignificant characters, such as whitespaces.
     *
     * @param pattern   a regexp pattern for this rule.
     * @param tokenKind a token kind which will be used to create new tokens for this rule.
     *                  If null is passed, no token will be generated upon match.
     */
    public void addRule(String pattern, TokenKind tokenKind) {
        addRule(Pattern.compile(pattern), tokenKind);
    }

    /**
     * Register a new matching rule.
     * <p>
     * Identical to the {@link #addRule(String, TokenKind)} except that it accepts a compiled regexp.
     *
     * @param pattern   a regexp pattern for this rule.
     * @param tokenKind a token kind which will be used to create new tokens for this rule.
     *                  If null is passed, no token will be generated upon match.
     */
    public void addRule(Pattern pattern, TokenKind tokenKind) {
        if (tokenKind == null) {
            addRule(pattern, (Matcher matcher) -> null);
        } else {
            addRule(pattern, (Matcher matcher) -> new Token(tokenKind, matcher.group(), 0, 0));
        }
    }

    /**
     * Register a new matching rule.
     * <p>
     * Identical to the {@link #addRule(String, TokenKind)} except that it accepts a token factory.
     *
     * @param pattern      a regexp pattern for this rule.
     * @param tokenFactory a function which, given a {@link Matcher} object, generates a new {@link Token} (or null
     *                     to suppress adding new token to the token stream). NB: token's row and column will
     *                     be overridden by tokenizer.
     */
    public void addRule(Pattern pattern, Function<Matcher, Token> tokenFactory) {
        rules.add(new Rule(pattern, tokenFactory));
    }

    @Override
    public TokenStream tokenize(String text) {
        return new RegexpTextLexerTokenStream(new ArrayList<>(rules), text);
    }
}
