package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;

import java.util.ArrayList;

/**
 * AST node representing `or` parser combinator.
 *
 * @see com.github.amatanhead.pcl.combinators.Combinators#or(AST[])
 */
public class NOr<R> extends AST<R> {
    private final ArrayList<AST<? extends R>> underlying;

    public NOr(ArrayList<AST<? extends R>> underlying) {
        this.underlying = underlying;
    }

    public ArrayList<AST<? extends R>> getUnderlying() {
        return underlying;
    }

    @Override
    public NodeType getType() {
        return NodeType.Or;
    }
}
