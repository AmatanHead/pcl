package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;

import java.util.function.Function;

/**
 * Base class for all AST nodes.
 *
 * @param <R> return type of a bound function. By default, `some` and `a` combinators return tokens.
 *            Use {@link #bind(Function)} to specify a processor which changes token into something useful.
 */
abstract public class AST<R> {
    /**
     * Bind this combinator to the given functor.
     *
     * @param functor result modifier.
     * @param <NT>    return type of the functor.
     * @return new ast node with the return type `NT`.
     */
    public <NT> AST<NT> bind(Function<R, NT> functor) {
        return new NApply<>(this, functor);
    }

    abstract public NodeType getType();
}
