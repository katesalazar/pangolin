package org.minia.pangolin.runtimegraph;

import lombok.val;
import org.apache.commons.lang3.NotImplementedException;
import org.minia.pangolin.semantics.UnboundIdentifierException;

import static org.minia.pangolin.util.Util.forceAssert;

public class ExecuteStatement extends Statement {

    private static final String BRANCH_NOT_IMPLEMENTED_YET =
            "branch not implemented yet";

    private final NamedFunctionCallExpression namedFunctionCallExpression;

    ExecuteStatement() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    ExecuteStatement(
            final org.minia.pangolin.syntaxtree.ExecuteStatement executeStatement) {
        super();
        this.namedFunctionCallExpression =
                NamedFunctionCallExpression.fromSyntaxTreeNamedFunctionCallExpression(
                        executeStatement.getNamedFunctionCallExpression());
    }

    ExecuteStatement(final NamedFunctionCallExpression namedFunctionCallExpression) {

        super();
        this.namedFunctionCallExpression = namedFunctionCallExpression;
    }

    public void compute() {
        namedFunctionCallExpression.compute();
    }

    public void run() {
        namedFunctionCallExpression.run();
    }
}
