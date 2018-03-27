package com.github.amatanhead.pcl.stream;

import com.github.amatanhead.pcl.errors.NoNewTokenError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.token.Token;

/**
 * Token stream is the result of tokenization.
 * <p>
 * Token streams are somewhat close to {@link java.util.ListIterator list iterators}. They implement
 * {@link #input()} and {@link #canInput()} similar to iterator. They, however, do not implement any modification
 * methods such as remove, set, add.
 */
public interface TokenStream {
    /**
     * Yield current token and adjust position of this stream by one forward.
     *
     * @return next token.
     * @throws TokenizationError if the underlying stream cannot be tokenized (in case of lazy tokenization).
     * @throws NoNewTokenError   no tokens left in the stream. See {@link #canInput()}.
     */
    Token input() throws TokenizationError;

    /**
     * Check if there is a next token available in the given stream.
     *
     * @return true if there are tokens left in this stream, false otherwise.
     */
    boolean canInput();

    /**
     * Indicates that this stream is clear, i.e. untouched.
     * <p>
     * All streams are initially clear. Stream becomes unclear on the first {@link #input()}. There is no way
     * to make stream clear again.
     */
    boolean isClear();
}
