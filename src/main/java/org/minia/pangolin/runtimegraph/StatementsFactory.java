package org.minia.pangolin.runtimegraph;

import org.minia.pangolin.semantics.UnboundIdentifierException;

public class StatementsFactory {

    StatementsFactory() {
        super();
    }

    public Statements from(
            final org.minia.pangolin.syntaxtree.Statements statements,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        return new Statements(statements, whereValueBindings);
    }
}
