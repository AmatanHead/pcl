package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;

import java.util.ArrayList;

/**
 * AST node representing `seq` parser combinator.
 *
 * @see com.github.amatanhead.pcl.combinators.Combinators#seq(AST[])
 */
public class NSeq extends AST<ArrayList<Object>> {
    private final ArrayList<AST<Object>> underlying;

    public NSeq(ArrayList<AST<Object>> underlying) {
        this.underlying = underlying;
    }

    public ArrayList<AST<Object>> getUnderlying() {
        return underlying;
    }

    @Override
    public NodeType getType() {
        return NodeType.Seq;
    }
}
