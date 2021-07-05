package org.minia.pangolin.syntaxtree;

public class NamedFunctionCall {

    private final NamedFunction namedFunction;

    NamedFunctionCall(final NamedFunction namedFunction) {
        super();
        this.namedFunction = namedFunction;
    }

    public ExpressionType type() {
        return new UnknownExpressionType();
    }
}
