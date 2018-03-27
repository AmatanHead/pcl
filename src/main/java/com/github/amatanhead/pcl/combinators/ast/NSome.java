package com.github.amatanhead.pcl.combinators.ast;

import com.github.amatanhead.pcl.combinators.NodeType;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.token.TokenKind;

import java.util.function.Function;

/**
 * AST node representing `maybe` parser combinator.
 *
 * @see com.github.amatanhead.pcl.combinators.Combinators#some(Function)
 * @see com.github.amatanhead.pcl.combinators.Combinators#a(TokenKind)
 */
public class NSome extends AST<Token> {
    private final Function<Token, Boolean> predicate;

    public NSome(Function<Token, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Function<Token, Boolean> getPredicate() {
        return predicate;
    }

    @Override
    public NodeType getType() {
        return NodeType.Some;
    }
}
