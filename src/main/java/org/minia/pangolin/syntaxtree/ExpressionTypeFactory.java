package org.minia.pangolin.syntaxtree;

public class ExpressionTypeFactory {

    public ExpressionTypeFactory() {
        super();
    }

    public UnresolvedExpressionType unresolved() {
        return new UnresolvedExpressionType();
    }

    public UnknownExpressionType unknown() {
        return new UnknownExpressionType();
    }

    public EmptyExpressionType empty() {
        return new EmptyExpressionType();
    }

    public NaturalExpressionType natural() {
        return new NaturalExpressionType();
    }
}
