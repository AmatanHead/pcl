package com.github.amatanhead.pcl.combinators;

import com.github.amatanhead.pcl.combinators.ast.*;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.token.TokenKind;
import com.github.amatanhead.pcl.utils.GenericTuple;
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
    static public AST<ArrayList<Object>> seq(AST<Object>... as) {
        return new NSeq(new ArrayList<>(Arrays.asList(as)));
    }

    /**
     * A special case for sequence of two tokens. Used to ensure type-safety and spare users from
     * casting objects around.
     *
     * @see #seq(AST[])
     */
    @SuppressWarnings("unchecked")
    static public <R0, R1> AST<GenericTuple.Tuple2<R0, R1>> seq1(AST<R0> a0, AST<R1> a1) {
        return seq((AST<Object>) a0, (AST<Object>) a1).bind(array -> new GenericTuple.Tuple2<>((R0) array.get(0), (R1) array.get(1)));
    }

    /**
     * A special case for sequence of three tokens. Used to ensure type-safety and spare users from
     * casting objects around.
     *
     * @see #seq(AST[])
     */
    @SuppressWarnings("unchecked")
    static public <R0, R1, R2> AST<GenericTuple.Tuple3<R0, R1, R2>> seq1(AST<R0> a0, AST<R1> a1, AST<R2> a2) {
        return seq((AST<Object>) a0, (AST<Object>) a1, (AST<Object>) a2).bind(array -> new GenericTuple.Tuple3<>((R0) array.get(0), (R1) array.get(1), (R2) array.get(2)));
    }

    /**
     * A special case for sequence of four tokens. Used to ensure type-safety and spare users from
     * casting objects around.
     *
     * @see #seq(AST[])
     */
    @SuppressWarnings("unchecked")
    static public <R0, R1, R2, R3> AST<GenericTuple.Tuple4<R0, R1, R2, R3>> seq1(AST<R0> a0, AST<R1> a1, AST<R2> a2, AST<R3> a3) {
        return seq((AST<Object>) a0, (AST<Object>) a1, (AST<Object>) a2, (AST<Object>) a3).bind(array -> new GenericTuple.Tuple4<>((R0) array.get(0), (R1) array.get(1), (R2) array.get(2), (R3) array.get(3)));
    }

    /**
     * A special case for sequence of five tokens. Used to ensure type-safety and spare users from
     * casting objects around.
     *
     * @see #seq(AST[])
     */
    @SuppressWarnings("unchecked")
    static public <R0, R1, R2, R3, R4> AST<GenericTuple.Tuple5<R0, R1, R2, R3, R4>> seq1(AST<R0> a0, AST<R1> a1, AST<R2> a2, AST<R3> a3, AST<R4> a4) {  // woooo!
        return seq((AST<Object>) a0, (AST<Object>) a1, (AST<Object>) a2, (AST<Object>) a3, (AST<Object>) a4).bind(array -> new GenericTuple.Tuple5<>((R0) array.get(0), (R1) array.get(1), (R2) array.get(2), (R3) array.get(3), (R4) array.get(5)));
    }

    /**
     * A parser which matches either of the given parsers.
     */
    @SafeVarargs
    static public AST<Object> or(AST<Object>... as) {
        return new NOr(new ArrayList<>(Arrays.asList(as)));
    }

    /**
     * A special case for matching one of two parsers.
     *
     * @see #or(AST[])
     */
    @SuppressWarnings("unchecked")
    static public <R> AST<R> or1(AST<R> a0, AST<R> a1) {
        return (AST<R>) or((AST<Object>) a0, (AST<Object>) a1);
    }

    /**
     * A special case for matching one of three parsers.
     *
     * @see #or(AST[])
     */
    @SuppressWarnings("unchecked")
    static public <R> AST<R> or1(AST<R> a0, AST<R> a1, AST<R> a2) {
        return (AST<R>) or((AST<Object>) a0, (AST<Object>) a1, (AST<Object>) a2);
    }

    /**
     * A special case for matching one of four parsers.
     *
     * @see #or(AST[])
     */
    @SuppressWarnings("unchecked")
    static public <R> AST<R> or1(AST<R> a0, AST<R> a1, AST<R> a2, AST<R> a3) {
        return (AST<R>) or((AST<Object>) a0, (AST<Object>) a1, (AST<Object>) a2, (AST<Object>) a3);
    }

    /**
     * A special case for matching one of five parsers.
     *
     * @see #or(AST[])
     */
    @SuppressWarnings("unchecked")
    static public <R> AST<R> or1(AST<R> a0, AST<R> a1, AST<R> a2, AST<R> a3, AST<R> a4) {
        return (AST<R>) or((AST<Object>) a0, (AST<Object>) a1, (AST<Object>) a2, (AST<Object>) a3, (AST<Object>) a4);
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
