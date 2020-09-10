package org.minia.pangolin.runtimegraph;

import lombok.val;
import org.minia.pangolin.semantics.UnboundIdentifierException;

import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.util.Util.forceAssert;

public class Operations {

    public enum RunTimeInterleave {

        /**  <p>Operations can run in any order. */
        PARALLEL,

        /**  <p>Operations must be run in a particular order. */
        SEQUENTIAL,

        /**  <p>Don't use this. It only makes sense if only a single
         * operation has to be run. */
        ANY,

        /**  <p>Don't use this. It only makes sense as a `null`
         * placeholder alternative for non optional function call
         * arguments. */
        UNKNOWN
    }

    private final List<Operation> operations;

    private final Operations.RunTimeInterleave runTimeInterleave;

    Operations(
            final org.minia.pangolin.syntaxtree.Operations operations,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        this.operations =
                new ArrayList<>(operations.getOperations().size());
        for (final org.minia.pangolin.syntaxtree.Operation stOperation:
                operations.getOperations()) {
            val operation = new OperationFactory().from(
                    stOperation, whereValueBindings);
            this.operations.add(operation);
        }
        if (org.minia.pangolin.syntaxtree.Operations.RunTimeInterleave.PARALLEL ==
                operations.getRunTimeInterleave()) {
            runTimeInterleave = RunTimeInterleave.PARALLEL;
        } else {
            forceAssert(org.minia.pangolin.syntaxtree.Operations.RunTimeInterleave.SEQUENTIAL ==
                    operations.getRunTimeInterleave());
            runTimeInterleave = RunTimeInterleave.SEQUENTIAL;
        }
    }

    public void run() {
        if (RunTimeInterleave.ANY == runTimeInterleave) {
            forceAssert(operations.size() == 1);
            operations.get(0).run();
        } else if (RunTimeInterleave.PARALLEL == runTimeInterleave) {
            for (final Operation operation: operations) {
                operation.run();
            }
        } else if (RunTimeInterleave.SEQUENTIAL == runTimeInterleave) {
            for (final Operation operation: operations) {
                operation.run();
            }
        }
    }
}
