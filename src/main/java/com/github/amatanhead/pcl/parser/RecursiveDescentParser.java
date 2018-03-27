package com.github.amatanhead.pcl.parser;

import com.github.amatanhead.pcl.combinators.ast.AST;
import com.github.amatanhead.pcl.errors.ParsingError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.StandardConversions;
import com.github.amatanhead.pcl.stream.TokenStream;
import com.github.amatanhead.pcl.stream.TokenStreamStar;


/**
 * A default `LL(*)`recursive descent parser implementation.
 */
public class RecursiveDescentParser<R> implements Parser<R> {
    private final RDPCompiledAST compiledAST;

    /**
     * Construct parser from parser definition in form of AST. The process of parser compilation invoked upon
     * class creation.
     *
     * @param root root ast mode, i.e. a main derivation rule.
     */
    public RecursiveDescentParser(AST<R> root) {
        compiledAST = gerCompiler().compile(root);
    }

    @Override
    @SuppressWarnings("unchecked")  // type safety guaranteed by AST constructors.
    public R parse(TokenStream tokenStream) throws TokenizationError, ParsingError {
        TokenStreamStar tokenStreamStar = StandardConversions.toStreamStar(tokenStream);
        RDPResult parsingResult = compiledAST.parse(tokenStreamStar);
        if (parsingResult.isSuccess()) {
            return (R) parsingResult.getResult();
        } else {
            String message;
            if (parsingResult.getErrorMessage() != null && !parsingResult.getErrorMessage().isEmpty()) {
                message = parsingResult.getErrorMessage();
            } else {
                message = "parsing error";
            }
            throw new ParsingError(message);
        }
    }

    /**
     * Hook for ancestors to change compiler implementation.
     *
     * @return compiler which will be used to compile the root AST.
     */
    protected RDPCompiler gerCompiler() {
        return new RDPStandardCompiler();
    }
}
