package com.github.amatanhead.pcl.parser;

import com.github.amatanhead.pcl.errors.ParsingError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.TokenStreamStar;

/**
 * An interface for a compiled root AST node.
 */
public interface RDPCompiledAST {
    /**
     * Actual parsing implementation. Given a {@link TokenStreamStar}, parse it and return parsing result.
     *
     * @param tokenStream tokenized input that is going to be parsed.
     * @return result of the parsing.
     * @throws TokenizationError error if input stream cannot be tokenized. This error may arise in the process
     *                           of parsing in case of lazy tokenization.
     * @throws ParsingError      error if the token stream cannot be parsed.
     */
    RDPResult parse(TokenStreamStar tokenStream) throws TokenizationError, ParsingError;
}
