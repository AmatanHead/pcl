package com.github.amatanhead.pcl.combinators;

import com.github.amatanhead.pcl.combinators.ast.AST;

/**
 * Enum which contains all types of AST nodes that form parsing notation.
 * <p>
 * This enum is used to avoid multiple `instanceof` calls in parser compilers.
 *
 * @see AST#getType()
 */
public enum NodeType {
    /**
     * Apply node.
     *
     * @see com.github.amatanhead.pcl.combinators.ast.NApply
     */
    Apply,

    /**
     * Many node.
     *
     * @see com.github.amatanhead.pcl.combinators.ast.NMany
     */
    Many,

    /**
     * Maybe node.
     *
     * @see com.github.amatanhead.pcl.combinators.ast.NMaybe
     */
    Maybe,

    /**
     * Or node.
     *
     * @see com.github.amatanhead.pcl.combinators.ast.NOr
     */
    Or,

    /**
     * Seq node.
     *
     * @see com.github.amatanhead.pcl.combinators.ast.NSeq
     */
    Seq,

    /**
     * Some node.
     *
     * @see com.github.amatanhead.pcl.combinators.ast.NSome
     */
    Some,

    /**
     * Special case for AST nodes added by users of this library.
     * <p>
     * Standard parser have no idea what to do with such nodes, so it'll throw an error. Users, however, are free to
     * derive from the standard parser and add support for such nodes by overloading
     * {@link com.github.amatanhead.pcl.parser.RDPStandardCompiler#compileCustom(AST)}.
     * Alternatively, they can implement their own parser from scratch.
     */
    Custom,

    /**
     * Deferred node implementation, for cyclic dependencies.
     */
    Defer
}
