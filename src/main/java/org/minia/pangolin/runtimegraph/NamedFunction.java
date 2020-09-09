package org.minia.pangolin.runtimegraph;

import lombok.Getter;
import org.minia.pangolin.semantics.UnboundIdentifierException;

public class NamedFunction {

    @Getter private final CharSequence name;

    private final Operations operations;

    private NamedFunction() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    NamedFunction(
            final org.minia.pangolin.syntaxtree.NamedFunction namedFunction)
                    throws UnboundIdentifierException {

        name = namedFunction.getName();
        operations = new OperationsFactory().from(
                namedFunction.getOperations(),
                namedFunction.getWhereValueBindings());
    }

    public void run() {
        operations.run();
    }
}
