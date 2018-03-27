package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;

/**
 * A placeholder node for resolving circular dependencies.
 *
 * @see com.github.amatanhead.pcl.combinators.Combinators#defer()
 */
public class NDefer<R> extends AST<R> {
    private AST<R> deferred;

    public AST<R> getDeferred() {
        return deferred;
    }

    public void setDeferred(AST<R> deferred) {
        this.deferred = deferred;
    }

    @Override
    public NodeType getType() {
        return NodeType.Defer;
    }
}
