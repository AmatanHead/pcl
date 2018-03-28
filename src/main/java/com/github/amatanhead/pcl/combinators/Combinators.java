package com.github.amatanhead.pcl.combinators;

import com.github.amatanhead.pcl.combinators.ast.*;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.token.TokenKind;
import com.github.amatanhead.pcl.utils.Maybe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Basic interface for parser combinators.
 * <p>
 * Parser combinators are a way of expressing `LL` and other grammars. Essentially, single parsing combinator
 * is a function which accepts a parser and optional arguments and returns a new parser based on the given one.
 * <p>
 * For example, <i>{@link #a(TokenKind) a}(T)</i> returns a parser which matches a single token of type `T` and
 * <i>{@link #many(AST)} many}({@link #a(TokenKind) a}(T))</i> returns a parser which matches an arbitrary long
 * sequence of tokens of type `T`.
 * <p>
 * In this library, parser combinators return simple AST nodes which describe parsing sequence. Those AST nodes can
 * than be compiled into parsers (see {@link com.github.amatanhead.pcl.parser.RecursiveDescentParser}, for example).
 * Thus, properties of the resulting parser completely depend on compiler.
 */
public final class Combinators {
    /**
     * Protect constructor since this is a static-only class.
     */
    protected Combinators() {
    }

    /**
     * A parser which matches a single token iff predicate returns true.
     */
    static public AST<Token> some(Function<Token, Boolean> predicate) {
        return new NSome(predicate);
    }

    /**
     * A parser which matches any token of the given type.
     */
    static public AST<Token> a(TokenKind tokenKind) {
        return some(token -> token.getTokenKind() == tokenKind);
    }

    /**
     * A parser which matches any token of the given type.
     *
     * Completely identical to {@link #a(TokenKind)} (except for name).
     */
    static public AST<Token> an(TokenKind tokenKind) {
        return some(token -> token.getTokenKind() == tokenKind);
    }

    /**
     * A parser which matches a sequence of tokens, following one after another.
     * That is, `seq(a(T1), a(T2), a(T3))` will match token `T3` following immediately after token `T2` following
     * immediately after token `T1`.
     *
     * In other languages, this is usually implemented as an operator `+`.
     */
    @SafeVarargs
    static public <T> AST<ArrayList<T>> seq(AST<? extends T>... as) {
        return new NSeq<>(new ArrayList<>(Arrays.asList(as)));
    }

    /**
     * A parser which matches either of the given parsers.
     */
    @SafeVarargs
    static public <T> AST<T> or(AST<? extends T>... as) {
        return new NOr<>(new ArrayList<>(Arrays.asList(as)));
    }

    /**
     * Repeat a parser 0 to infinity times.
     */
    static public <R> AST<ArrayList<R>> many(AST<R> a) {
        return many(a, 0, null);
    }

    /**
     * Repeat a parser `min` to `max` times. Pass `max = null` for an infinite matcher.
     */
    static public <R> AST<ArrayList<R>> many(AST<R> a, Integer min, Integer max) {
        return new NMany<>(a, min, max);
    }

    /**
     * Repeat a parser zero or one time.
     */
    static public <R> AST<Maybe<R>> maybe(AST<R> a) {
        return new NMaybe<>(a);
    }

    /**
     * Create a placeholder node. Use for resolving circular dependencies.
     */
    static public <R> NDefer<R> defer() {
        return new NDefer<>();
    }
}
