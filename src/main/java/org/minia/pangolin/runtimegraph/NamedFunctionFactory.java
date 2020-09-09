package org.minia.pangolin.runtimegraph;

import org.minia.pangolin.semantics.UnboundIdentifierException;

public class NamedFunctionFactory {

    public NamedFunctionFactory() {
        super();
    }

    public NamedFunction from(
            final org.minia.pangolin.syntaxtree.NamedFunction namedFunction)
                    throws UnboundIdentifierException {

        return new NamedFunction(namedFunction);
    }
}
