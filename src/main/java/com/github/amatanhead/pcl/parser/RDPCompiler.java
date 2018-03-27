package com.github.amatanhead.pcl.parser;

import com.github.amatanhead.pcl.combinators.ast.AST;

/**
 * A compiler used in RDP to compile root AST node into actual parser.
 */
public interface RDPCompiler {
    /**
     * Compile root AST node, return something that can parse a token stream.
     */
    RDPCompiledAST compile(AST root);
}
