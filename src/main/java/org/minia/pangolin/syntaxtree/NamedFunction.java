package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import lombok.val;

public class NamedFunction implements TopLevelNode {

    public enum Type {

        CLI, PURE, NONPURE
    }

    @Getter private final CharSequence name;

    @Getter private final Statements statements;

    @Getter private final Expression expression;

    @Getter private final WhereValueBindings whereValueBindings;

    public NamedFunction(
            final CharSequence name, final Statements statements,
            final WhereValueBindings whereValueBindings) {

        this.name = name;
        this.statements = statements;
        expression = null;
        this.whereValueBindings = whereValueBindings;
    }

    public NamedFunction(
            final CharSequence name, final Expression expression,
            final WhereValueBindings whereValueBindings) {

        this.name = name;
        statements = null;
        this.expression = expression;
        this.whereValueBindings = whereValueBindings;
    }

    public NamedFunction(
            final CharSequence name,
            final NamedFunctionBody namedFunctionBody) {
        this.name = name;
        statements = namedFunctionBody.getStatements();
        expression = namedFunctionBody.getExpression();
        val namedFunctionBodyWhereValueBindings =
                namedFunctionBody.getWhereValueBindings();
        if (namedFunctionBodyWhereValueBindings == null) {
            whereValueBindings = null;
        } else {
            whereValueBindings = new WhereValueBindings(
                    namedFunctionBodyWhereValueBindings);
        }
    }
}
