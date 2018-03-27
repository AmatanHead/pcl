package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;

import java.util.function.Function;

/**
 * AST node representing application of a function to a parser result.
 */
public class NApply<T, R> extends AST<R> {
    private final AST<T> underlying;
    private final Function<T, R> functor;

    public NApply(AST<T> underlying, Function<T, R> functor) {
        this.underlying = underlying;
        this.functor = functor;
    }

    public AST<T> getUnderlying() {
        return underlying;
    }

    public Function<T, R> getFunctor() {
        return functor;
    }

    @Override
    public NodeType getType() {
        return NodeType.Apply;
    }
}
