package org.minia.pangolin.runtimegraph;

import org.minia.pangolin.semantics.UnboundIdentifierException;

public class OperationsFactory {

    OperationsFactory() {
        super();
    }

    public Operations from(
            final org.minia.pangolin.syntaxtree.Operations operations,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        return new Operations(operations, whereValueBindings);
    }
}
