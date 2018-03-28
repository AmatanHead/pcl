package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;

import java.util.ArrayList;

/**
 * AST node representing `seq` parser combinator.
 *
 * @see com.github.amatanhead.pcl.combinators.Combinators#seq(AST[])
 */
public class NSeq<R> extends AST<ArrayList<R>> {
    private final ArrayList<AST<? extends R>> underlying;

    public NSeq(ArrayList<AST<? extends R>> underlying) {
        this.underlying = underlying;
    }

    public ArrayList<AST<? extends R>> getUnderlying() {
        return underlying;
    }

    @Override
    public NodeType getType() {
        return NodeType.Seq;
    }
}
