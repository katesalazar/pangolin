package org.minia.pangolin.runtimegraph;

import lombok.Getter;
import org.minia.pangolin.semantics.UnboundIdentifierException;

public class NamedFunction {

    @Getter private final CharSequence name;

    private final Statements statements;

    private NamedFunction() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    NamedFunction(
            final org.minia.pangolin.syntaxtree.NamedFunction namedFunction)
                    throws UnboundIdentifierException {

        name = namedFunction.getName();
        statements = new StatementsFactory().from(
                namedFunction.getStatements(),
                namedFunction.getWhereValueBindings());
    }

    public void compute() {
        statements.compute();
    }

    public void run() {
        statements.run();
    }
}
