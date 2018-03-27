package com.github.amatanhead.pcl.stream;

import com.github.amatanhead.pcl.token.Token;

/**
 * TokenStreamN is a TokenStream with a capacity to return arbitrary tokens to a stream.
 * <p>
 * Unlike other backtrackable token streams, this one doesn't keep track of previously generated tokens,
 * which makes it memory-efficient.
 */
public interface TokenStreamN extends TokenStream {
    /**
     * Prepend given token to the stream / backtracking stack.
     * <p>
     * This is a way to return few tokens back to the stream when implementing a backtracking algorithms.
     * Call to {@link #input()} immediately after calling `backtrack(T)` will return exactly `T`.
     *
     * @param token token that shall be returned to the stream.
     */
    Token unput(Token token);
}
