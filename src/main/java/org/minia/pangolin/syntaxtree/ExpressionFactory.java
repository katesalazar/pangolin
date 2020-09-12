package org.minia.pangolin.syntaxtree;

import lombok.val;

import static org.minia.pangolin.util.Util.forceAssert;

public class ExpressionFactory {

    public ExpressionFactory() {
        super();
    }

    public Expression copy(final Expression expression) {
        val expressionType = expression.getClass();
        forceAssert(IdentifierExpression.class.equals(expressionType));
        return new IdentifierExpression((IdentifierExpression) expression);
    }
}
