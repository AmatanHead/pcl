package com.github.amatanhead.pcl.parser;

import com.github.amatanhead.pcl.combinators.ast.*;
import com.github.amatanhead.pcl.errors.ParsingError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.stream.TokenStreamStar;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.utils.Maybe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A standard recursive descent parser compiler.
 */
public class RDPStandardCompiler implements RDPCompiler {
    protected static class CompilationState {
        public Map<AST, RDPCompiledAST> compiled;

        CompilationState() {
            compiled = new HashMap<>();
        }
    }

    public RDPCompiledAST compile(AST root) {
        return compile(root, new CompilationState());
    }

    public RDPCompiledAST compile(AST root, CompilationState compilationState) {
        if (compilationState.compiled.containsKey(root)) {
            return compilationState.compiled.get(root);
        }

        RDPCompiledAST compiled = null;

        switch (root.getType()) {
            case Apply:
                if (root instanceof NApply) { compiled = compileApply((NApply) root, compilationState); }
                break;
            case Many:
                if (root instanceof NMany) { compiled = compileMany((NMany) root, compilationState); }
                break;
            case Maybe:
                if (root instanceof NMaybe) { compiled = compileMaybe((NMaybe) root, compilationState); }
                break;
            case Or:
                if (root instanceof NOr) { compiled = compileOr((NOr) root, compilationState); }
                break;
            case Seq:
                if (root instanceof NSeq) { compiled = compileSeq((NSeq) root, compilationState); }
                break;
            case Some:
                if (root instanceof NSome) { compiled = compileSome((NSome) root, compilationState); }
                break;
            case Custom:
                compiled = compileCustom(root, compilationState);
                break;
            case Defer:
                if (root instanceof NDefer) { compiled = compileDefer((NDefer) root, compilationState); }
                break;
        }

        if (compiled != null) {
            compilationState.compiled.put(root, compiled);
            return compiled;
        }

        throw new IllegalArgumentException("AST node class doesnt't match its getType()");
    }

    /**
     * Compile an `apply` node.
     */
    protected CompiledApply compileApply(NApply node, CompilationState compilationState) {
        RDPCompiledAST underlying = compile(node.getUnderlying(), compilationState);
        return new CompiledApply(underlying, node.getFunctor());
    }

    /**
     * Compile a `many` node.
     */
    protected CompiledMany compileMany(NMany node, CompilationState compilationState) {
        RDPCompiledAST underlying = compile(node.getUnderlying(), compilationState);
        return new CompiledMany(underlying, node.getMin(), node.getMax());
    }

    /**
     * Compile a `maybe` node.
     */
    protected CompiledMaybe compileMaybe(NMaybe node, CompilationState compilationState) {
        RDPCompiledAST underlying = compile(node.getUnderlying(), compilationState);
        return new CompiledMaybe(underlying);
    }

    /**
     * Compile an `or` node.
     */
    protected CompiledOr compileOr(NOr node, CompilationState compilationState) {
        ArrayList<RDPCompiledAST> underlying = node.getUnderlying().stream()
                .map(ast -> compile(ast, compilationState))
                .collect(Collectors.toCollection(ArrayList::new));
        return new CompiledOr(underlying);
    }

    /**
     * Compile a `seq` node.
     */
    protected CompiledSeq compileSeq(NSeq node, CompilationState compilationState) {
        ArrayList<RDPCompiledAST> underlying = node.getUnderlying().stream()
                .map(ast -> compile(ast, compilationState))
                .collect(Collectors.toCollection(ArrayList::new));
        return new CompiledSeq(underlying);
    }

    /**
     * Compile a `some` node.
     */
    @SuppressWarnings("unused")
    protected CompiledSome compileSome(NSome node, CompilationState compilationState) {
        return new CompiledSome(node.getPredicate());
    }

    /**
     * Compile a `defer` node.
     */
    protected RDPCompiledAST compileDefer(NDefer node, CompilationState compilationState) {
        if (node.getDeferred() == null) {
            throw new IllegalArgumentException("deferred node is not defined");
        }

        CompiledDefer compiled = new CompiledDefer();

        compilationState.compiled.put(node, compiled);

        compiled.setUnderlying(compile(node.getDeferred(), compilationState));

        return compiled;
    }

    /**
     * Compile a `custom` node. Throws an error by default. Override to add support for custom nodes.
     */
    @SuppressWarnings("unused")
    protected RDPCompiledAST compileCustom(AST node, CompilationState compilationState) {
        throw new IllegalArgumentException(
                "this parser does not support custom AST nodes; " +
                        "you're free to derive from this parser and add support for custom AST nodes");
    }

    // Classes

    /**
     * Default `apply` node implementation.
     */
    static protected class CompiledApply implements RDPCompiledAST {
        protected final RDPCompiledAST underlying;
        protected final Function functor;

        CompiledApply(RDPCompiledAST underlying, Function functor) {
            this.underlying = underlying;
            this.functor = functor;
        }

        @Override
        @SuppressWarnings("unchecked")  // type safety guaranteed by AST constructors.
        public RDPResult parse(TokenStreamStar tokenStream) throws TokenizationError, ParsingError {
            RDPResult parsingResult = underlying.parse(tokenStream);

            if (parsingResult.isSuccess()) {
                parsingResult.setResult(functor.apply(parsingResult.getResult()));
            }

            return parsingResult;
        }
    }

    /**
     * Default `many` node implementation.
     */
    static protected class CompiledMany implements RDPCompiledAST {
        protected final RDPCompiledAST underlying;
        protected final Integer min;
        protected final Integer max;

        CompiledMany(RDPCompiledAST underlying, Integer min, Integer max) {
            this.underlying = underlying;
            this.min = min;
            this.max = max;
        }

        @Override
        public RDPResult parse(TokenStreamStar tokenStream) throws TokenizationError, ParsingError {
            TokenStreamStar.Bookmark bookmark = tokenStream.makeBookmark();

            ArrayList<Object> results = new ArrayList<>();

            for (int i = 0; max == null || i < max; i++) {
                RDPResult parsingResult = underlying.parse(tokenStream);
                if (parsingResult.isSuccess()) {
                    results.add(parsingResult.getResult());
                } else if (i >= min) {
                    break;
                } else {
                    tokenStream.restoreBookmark(bookmark);
                    return new RDPResult(false, null);
                }
            }

            return new RDPResult(true, results);
        }
    }

    /**
     * Default `maybe` node implementation.
     */
    static protected class CompiledMaybe implements RDPCompiledAST {
        protected final RDPCompiledAST underlying;

        CompiledMaybe(RDPCompiledAST underlying) {
            this.underlying = underlying;
        }

        @Override
        @SuppressWarnings("unchecked")  // type safety guaranteed by AST constructors.
        public RDPResult parse(TokenStreamStar tokenStream) throws TokenizationError, ParsingError {
            RDPResult parsingResult = underlying.parse(tokenStream);

            if (parsingResult.isSuccess()) {
                parsingResult.setResult(new Maybe(parsingResult.getResult()));
            } else {
                parsingResult.setResult(new Maybe());
                parsingResult.setSuccess(true);
            }

            return parsingResult;
        }
    }

    /**
     * Default `or` node implementation.
     */
    static protected class CompiledOr implements RDPCompiledAST {
        protected final ArrayList<RDPCompiledAST> underlying;

        CompiledOr(ArrayList<RDPCompiledAST> underlying) {
            this.underlying = underlying;
        }

        @Override
        public RDPResult parse(TokenStreamStar tokenStream) throws TokenizationError, ParsingError {
            for (RDPCompiledAST compiledAST : underlying) {
                RDPResult parsingResult = compiledAST.parse(tokenStream);
                if (parsingResult.isSuccess()) {
                    return parsingResult;
                }
            }

            return new RDPResult(false, null);
        }
    }

    /**
     * Default `seq` node implementation.
     */
    static protected class CompiledSeq implements RDPCompiledAST {
        protected final ArrayList<RDPCompiledAST> underlying;

        CompiledSeq(ArrayList<RDPCompiledAST> underlying) {
            this.underlying = underlying;
        }

        @Override
        public RDPResult parse(TokenStreamStar tokenStream) throws TokenizationError, ParsingError {
            TokenStreamStar.Bookmark bookmark = tokenStream.makeBookmark();

            ArrayList<Object> results = new ArrayList<>(underlying.size());

            for (RDPCompiledAST compiledAST : underlying) {
                RDPResult parsingResult = compiledAST.parse(tokenStream);
                if (parsingResult.isSuccess()) {
                    results.add(parsingResult.getResult());
                } else {
                    tokenStream.restoreBookmark(bookmark);
                    return parsingResult;
                }
            }

            return new RDPResult(true, results);
        }
    }

    /**
     * Default `some` node implementation.
     */
    static protected class CompiledSome implements RDPCompiledAST {
        protected final Function<Token, Boolean> predicate;

        CompiledSome(Function<Token, Boolean> predicate) {
            this.predicate = predicate;
        }

        @Override
        public RDPResult parse(TokenStreamStar tokenStream) throws TokenizationError {
            if (!tokenStream.canInput()) {
                return new RDPResult(false, null);
            }

            Token token = tokenStream.input();

            if (predicate.apply(token)) {
                return new RDPResult(true,token);
            } else {
                tokenStream.unput();
                return new RDPResult(false, null);
            }
        }
    }

    /**
     * Default `defer` node implementation.
     */
    static protected class CompiledDefer implements RDPCompiledAST {
        protected RDPCompiledAST underlying;

        public CompiledDefer() {
        }

        public RDPCompiledAST getUnderlying() {
            return underlying;
        }

        public void setUnderlying(RDPCompiledAST underlying) {
            this.underlying = underlying;
        }

        @Override
        @SuppressWarnings("unchecked")  // type safety guaranteed by AST constructors.
        public RDPResult parse(TokenStreamStar tokenStream) throws TokenizationError, ParsingError {
            return underlying.parse(tokenStream);
        }
    }
}
