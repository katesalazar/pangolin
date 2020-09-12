package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public abstract class Expression {

    @Getter private final Boolean pure;

    @Getter protected final ExpressionType expressionType;

    protected Expression() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    Expression(final Boolean pure, final ExpressionType expressionType) {
        super();
        this.pure = pure;
        this.expressionType = expressionType;
    }

    Expression(final Expression expression) {
        super();
        pure = expression.pure;
        expressionType = new ExpressionType(expression.expressionType);
    }
}
