package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public class ExecuteStatement implements Statement {

    /**  <p>What to execute. */
    @Getter
    private final NamedFunctionCallExpression namedFunctionCallExpression;

    public ExecuteStatement(
            final NamedFunctionCallExpression namedFunctionCallExpression) {

        this.namedFunctionCallExpression = namedFunctionCallExpression;
    }
}
