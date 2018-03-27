package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;
import com.github.amatanhead.pcl.utils.Maybe;

/**
 * AST node representing `maybe` parser combinator.
 *
 * @see com.github.amatanhead.pcl.combinators.Combinators#maybe(AST)
 */
public class NMaybe<R> extends AST<Maybe<R>> {
    private final AST<R> underlying;

    public NMaybe(AST<R> underlying) {
        this.underlying = underlying;
    }

    public AST<R> getUnderlying() {
        return underlying;
    }

    @Override
    public NodeType getType() {
        return NodeType.Maybe;
    }
}
