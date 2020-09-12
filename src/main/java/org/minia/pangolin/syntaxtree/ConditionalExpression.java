package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

public final class ConditionalExpression extends Expression {

    private final Condition condition;

    @Getter private final Expression expressionThen;

    @Getter private final Expression expressionElse;

    public ConditionalExpression() {
        super(null, null);
        throw new UnsupportedOperationException("FIXME");
    }

    public ConditionalExpression(
            final Condition condition, final Expression expressionThen,
            final Expression expressionElse) {
        super(
                null, new UnionExpressionType(
                        expressionThen.expressionType,
                        expressionElse.expressionType));
        this.condition = condition;
        this.expressionThen = expressionThen;
        this.expressionElse = expressionElse;
    }

    public ConditionalExpression(
            final ConditionalExpression conditionalExpression) {
        super(conditionalExpression);
        this.condition = conditionalExpression.condition;
        expressionThen = conditionalExpression.expressionThen;
        expressionElse = conditionalExpression.expressionElse;
    }

    /**  <p>The element at the left of the {@link Pair} holds if the
     * value of the {@link Condition} of this {@link
     * ConditionalExpression} can already be known at compile time.
     *   <p>The element at the right holds whether that value is true or
     * false, but only in case the element in the left is true. */
    public Pair<Boolean, Boolean> resolvableAtCompileTime() {

        return condition.resolvableAtCompileTime();
    }
}
