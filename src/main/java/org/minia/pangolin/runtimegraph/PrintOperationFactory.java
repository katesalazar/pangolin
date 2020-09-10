package org.minia.pangolin.runtimegraph;

import org.minia.pangolin.semantics.UnboundIdentifierException;

public class PrintOperationFactory {

    PrintOperationFactory() {
        super();
    }

    public PrintOperation from(
            final org.minia.pangolin.syntaxtree.PrintOperation printOperation,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        return new PrintOperation(
                printOperation.getToken(), whereValueBindings);
    }
}
