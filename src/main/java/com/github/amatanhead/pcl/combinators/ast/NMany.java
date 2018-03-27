package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;

import java.util.ArrayList;

/**
 * AST node representing `many` parser combinator.
 *
 * @see com.github.amatanhead.pcl.combinators.Combinators#many(AST)
 */
public class NMany<R> extends AST<ArrayList<R>> {
    private final AST<R> underlying;
    private final Integer min;
    private final Integer max;

    public NMany(AST<R> underlying, Integer min, Integer max) {
        this.underlying = underlying;
        this.min = min;
        this.max = max;
    }

    public AST<R> getUnderlying() {
        return underlying;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    @Override
    public NodeType getType() {
        return NodeType.Many;
    }
}
