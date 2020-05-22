package org.minia.pangolin.parser;

import lombok.Getter;
import org.minia.pangolin.Program;
import org.minia.pangolin.syntaxtree.Application;
import org.minia.pangolin.syntaxtree.ExecutionRequest;
import org.minia.pangolin.syntaxtree.NamedFunction;

public class ParseTree {

    public enum Type {
        APPLICATION, DOC, DOC_FRAGMENT, EMPTY, EXECUTION_REQUEST,
        NAMED_FUNCTION
    }

    @Getter
    private final Program program;

    @Getter
    private final Type type;

    @Getter
    private final NamedFunction namedFunction;

    @Getter
    private final Application application;

    @Getter
    private final ExecutionRequest executionRequest;

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
}
