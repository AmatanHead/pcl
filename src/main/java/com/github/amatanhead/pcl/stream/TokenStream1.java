package com.github.amatanhead.pcl.stream;

import com.github.amatanhead.pcl.errors.NoBacktrackingTokenError;
import com.github.amatanhead.pcl.token.Token;

/**
 * TokenStream1 is a TokenStream with a capacity to return one token to a stream.
 */
public interface TokenStream1 extends TokenStream {
    /**
     * Yield previous token and adjust position of this stream by one backward.
     * <p>
     * For `TokenStream1`, this function cannot be called twice without calling {@link #input()} in between.
     *
     * @return previous token.
     * @throws NoBacktrackingTokenError no tokens left to backtrack. See {@link #canUnput()}.
     */
    Token unput();

    /**
     * Check if there is a token can be returned to the stream.
     *
     * @return true if {@link #unput()} can return token to the stream.
     */
    boolean canUnput();
}
