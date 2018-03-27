package com.github.amatanhead.pcl.lexer;

import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.TokenStream;

import java.io.InputStream;

/**
 * Base interface for raw bytes lexer.
 * <p>
 * Raw byte lexers can convert an arbitrary sequence of raw bytes into a sequence of tokens.
 * <p>
 * Note: every lexer should yield the standard EOF token at the end of its stream.
 */
public interface Lexer {
    TokenStream tokenize(InputStream inputStream) throws java.io.IOException, TokenizationError;
}
