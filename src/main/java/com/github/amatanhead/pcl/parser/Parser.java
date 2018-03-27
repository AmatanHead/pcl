package com.github.amatanhead.pcl.parser;

import com.github.amatanhead.pcl.errors.ParsingError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.TokenStream;

/**
 * Base interface for all parsers that work with parser combinators defined in this library.
 *
 * @param <R> result type of the root ast node.
 */
public interface Parser<R> {
    /**
     * Parse the given sequence of tokens and apply bound functions to the results.
     * <p>
     * The behavior is unspecified in case of bound functions have side effects. That is, a parser might either
     * apply them as it goes or build an internal representation of parsing results and apply bound functions
     * to this representation in the very end of parsing.
     *
     * @param tokenStream tokenized input that is gping to be parsed.
     * @return result of the root AST node.
     * @throws TokenizationError error if input stream cannot be tokenized. This error may arise in the process
     *                           of parsing in case of lazy tokenization.
     * @throws ParsingError      error if the token stream cannot be parsed.
     */
    R parse(TokenStream tokenStream) throws TokenizationError, ParsingError;
}
