package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public class ExecuteStatement implements Statement {

    /**  <p>What to execute. */
    @Getter
    private final FunctionCallExpression functionCallExpression;

    public ExecuteStatement(
            final FunctionCallExpression functionCallExpression) {

        this.functionCallExpression = functionCallExpression;
    }
}
