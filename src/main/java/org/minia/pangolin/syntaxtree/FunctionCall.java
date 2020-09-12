package org.minia.pangolin.syntaxtree;

public class FunctionCall {

    FunctionCall() {
        super();
    }

    public ExpressionType type() {
        return new UnknownExpressionType();
    }
}
