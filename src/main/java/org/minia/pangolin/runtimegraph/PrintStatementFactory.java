package org.minia.pangolin.runtimegraph;

import org.minia.pangolin.semantics.UnboundIdentifierException;

public class PrintStatementFactory {

    PrintStatementFactory() {
        super();
    }

    public PrintStatement from(
            final org.minia.pangolin.syntaxtree.PrintStatement printStatement,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        return new PrintStatement(
                printStatement.getToken(), whereValueBindings);
    }
}
