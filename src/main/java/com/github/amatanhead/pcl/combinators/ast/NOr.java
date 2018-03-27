package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;

import java.util.ArrayList;


/**
 * AST node representing `or` parser combinator.
 *
 * @see com.github.amatanhead.pcl.combinators.Combinators#or(AST[])
 */
public class NOr extends AST<Object> {
    private final ArrayList<AST<Object>> underlying;

    public NOr(ArrayList<AST<Object>> underlying) {
        this.underlying = underlying;
    }

    public ArrayList<AST<Object>> getUnderlying() {
        return underlying;
    }

    @Override
    public NodeType getType() {
        return NodeType.Or;
    }
}
