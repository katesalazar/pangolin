package org.minia.pangolin.syntaxtree;

public final class FunctionCallExpression extends Expression {

    FunctionCallExpression() {
        super(null, null);
    }

    public static FunctionCallExpression fromThinAir() {
        return new FunctionCallExpression();
    }
}
