package org.minia.pangolin.runtimegraph;

import lombok.val;
import org.apache.commons.lang3.NotImplementedException;
import org.minia.pangolin.semantics.UnboundIdentifierException;

public class OperationFactory {

    OperationFactory() {
        super();
    }

    public Operation from(
            final org.minia.pangolin.syntaxtree.Operation operation,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        if (org.minia.pangolin.syntaxtree.PrintOperation.class.equals(
                operation.getClass())) {
            val printOperation =
                    (org.minia.pangolin.syntaxtree.PrintOperation)
                    operation;
            return new PrintOperationFactory().from(
                    printOperation, whereValueBindings);
        }
        if (org.minia.pangolin.syntaxtree.NewLineOperation.class.equals(
                operation.getClass())) {
            val newLineOperation =
                    (org.minia.pangolin.syntaxtree.NewLineOperation)
                    operation;
            return new NewLineOperationFactory().from(newLineOperation);
        }
        throw new NotImplementedException("FIXME");
    }
}
