package org.minia.pangolin.parser;

import lombok.Getter;
import lombok.val;
import org.minia.pangolin.Program;
import org.minia.pangolin.semantics.UnboundIdentifierException;
import org.minia.pangolin.syntaxtree.Application;
import org.minia.pangolin.syntaxtree.ExecutionRequest;
import org.minia.pangolin.syntaxtree.NamedFunction;
import org.minia.pangolin.syntaxtree.TopLevelNode;

import java.util.List;

import static org.minia.pangolin.util.Util.forceAssert;

public class ParseTree {

    public enum Type {
        APPLICATION, /* DOC, DOC_FRAGMENT, */ EMPTY, EXECUTION_REQUEST,
        NAMED_FUNCTION
    }

    @Getter private final Program program;

    @Getter private final Type type;

    @Getter private final NamedFunction namedFunction;

    @Getter private final Application application;

    @Getter private final ExecutionRequest executionRequest;

    public ParseTree(final Program program) {
        this.program = program;
        type = null;
        namedFunction = null;
        application = null;
        executionRequest = null;
    }

    public ParseTree(final Program program, final Type type) {
        this.program = program;
        this.type = type;
        namedFunction = null;
        application = null;
        executionRequest = null;
    }

    public ParseTree(final NamedFunction namedFunction) {
        program = null;
        type = Type.NAMED_FUNCTION;
        this.namedFunction = namedFunction;
        application = null;
        executionRequest = null;
    }

    public ParseTree(final Application application) {
        program = null;
        type = Type.APPLICATION;
        namedFunction = null;
        this.application = application;
        executionRequest = null;
    }

    public ParseTree(final ExecutionRequest executionRequest) {
        program = null;
        type = Type.EXECUTION_REQUEST;
        namedFunction = null;
        application = null;
        this.executionRequest = executionRequest;
    }

    public ParseTree(final TopLevelNode topLevelNode) {
        val topLevelNodeClass = topLevelNode.getClass();
        if (NamedFunction.class.equals(topLevelNodeClass)) {
            program = null;
            type = Type.NAMED_FUNCTION;
            namedFunction = (NamedFunction) topLevelNode;
            application = null;
            executionRequest = null;
        } else if (Application.class.equals(topLevelNodeClass)) {
            program = null;
            type = Type.APPLICATION;
            namedFunction = null;
            application = (Application) topLevelNode;
            executionRequest = null;
        } else {
            forceAssert(ExecutionRequest.class.equals(topLevelNodeClass));
            program = null;
            type = Type.EXECUTION_REQUEST;
            namedFunction = null;
            application = null;
            executionRequest = (ExecutionRequest) topLevelNode;
        }
    }

    public org.minia.pangolin.runtimegraph.NamedFunction
    namedFunctionForRunTimeGraph()
            throws UnboundIdentifierException {

        forceAssert(Type.NAMED_FUNCTION == type);
        return new org.minia.pangolin.runtimegraph.NamedFunctionFactory().from(
                this.namedFunction);
    }

    public org.minia.pangolin.runtimegraph.Application
    applicationForRunTimeGraph(
            final List<org.minia.pangolin.runtimegraph.NamedFunction> namedFunctions)
                    throws LanguageNotRecognizedException {

        forceAssert(Type.APPLICATION == type);
        return org.minia.pangolin.runtimegraph.Application.from(
                this.application, namedFunctions);
    }

    public org.minia.pangolin.runtimegraph.ExecutionRequest
    executionRequestForRunTimeGraph(
            final List<org.minia.pangolin.runtimegraph.Application> applications)
                    throws LanguageNotRecognizedException {

        forceAssert(Type.EXECUTION_REQUEST == type);
        return org.minia.pangolin.runtimegraph.ExecutionRequest.from(
                this.executionRequest, applications);
    }
}
