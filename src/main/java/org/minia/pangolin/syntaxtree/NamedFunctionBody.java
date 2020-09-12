package org.minia.pangolin.syntaxtree;

import lombok.Getter;

import static org.minia.pangolin.util.Util.forceAssert;

public final class NamedFunctionBody {

    private final Boolean pure;

    /**  Only when `pure`. */
    @Getter private final Expression expression;

    /**  Only when not `pure` because of itself (it can be non `pure`
     * not because of itself but because its dependencies). */
    @Getter private final Statements statements;

    /**  These can be used no matter if the function is `pure` or not. */
    @Getter private final WhereValueBindings whereValueBindings;

    NamedFunctionBody() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    private NamedFunctionBody(final Boolean pure, final Expression expression) {
        super();
        this.pure = pure;
        this.expression = new ExpressionFactory().copy(expression);
        statements = null;
        whereValueBindings = null;
    }

    private NamedFunctionBody(final Boolean pure, final Statements statements) {
        super();
        this.pure = pure;
        expression = null;
        this.statements = Statements.copy(statements);
        whereValueBindings = null;
    }

    private NamedFunctionBody(
            final Boolean pure, final Expression expression,
            final WhereValueBindings whereValueBindings) {
        super();
        this.pure = pure;
        this.expression = new ExpressionFactory().copy(expression);
        statements = null;
        this.whereValueBindings = new WhereValueBindings(whereValueBindings);
    }

    private NamedFunctionBody(
            final Boolean pure, final Statements statements,
            final WhereValueBindings whereValueBindings) {
        super();
        this.pure = pure;
        expression = null;
        this.statements = statements;
        this.whereValueBindings = new WhereValueBindings(whereValueBindings);
    }

    public static NamedFunctionBody fromPureExpression(
            final Expression expression) {
        forceAssert(expression.getPure());
        return new NamedFunctionBody(true, expression);
    }

    public static NamedFunctionBody fromStatements(
            final Statements statements) {
        return new NamedFunctionBody(false, statements);
    }

    public static NamedFunctionBody fromStatementsAndWhereValueBindings(
            final Statements statements,
            final WhereValueBindings whereValueBindings) {
        return new NamedFunctionBody(false, statements, whereValueBindings);
    }

    public NamedFunctionBody where(
            final WhereValueBindings whereValueBindings) {
        return new NamedFunctionBody(
                pure, expression, new WhereValueBindings(whereValueBindings));
    }
}
