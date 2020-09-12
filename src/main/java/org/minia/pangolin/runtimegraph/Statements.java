package org.minia.pangolin.runtimegraph;

import lombok.val;
import org.minia.pangolin.semantics.UnboundIdentifierException;

import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.util.Util.forceAssert;

public class Statements {

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

    private final List<Statement> statements;

    private final Statements.RunTimeInterleave runTimeInterleave;

    Statements(
            final org.minia.pangolin.syntaxtree.Statements statements,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        if (statements == null) {
            this.statements = new ArrayList<>(0);
        } else {
            this.statements =
                    new ArrayList<>(statements.getStatements().size());
            for (final org.minia.pangolin.syntaxtree.Statement stStatement:
                    statements.getStatements()) {
                val statement = new StatementFactory().from(
                        stStatement, whereValueBindings);
                this.statements.add(statement);
            }
        }
        if (statements == null) {
            runTimeInterleave = RunTimeInterleave.UNKNOWN;
        } else {
            val statementsRunTimeInterleave = statements.getRunTimeInterleave();
            if (org.minia.pangolin.syntaxtree.Statements.RunTimeInterleave.PARALLEL ==
                    statementsRunTimeInterleave) {
                runTimeInterleave = RunTimeInterleave.PARALLEL;
            } else {
                forceAssert(org.minia.pangolin.syntaxtree.Statements.RunTimeInterleave.SEQUENTIAL ==
                        statementsRunTimeInterleave);
                runTimeInterleave = RunTimeInterleave.SEQUENTIAL;
            }
        }
    }

    public void run() {
        if (RunTimeInterleave.ANY == runTimeInterleave) {
            forceAssert(statements.size() == 1);
            statements.get(0).run();
        } else if (RunTimeInterleave.PARALLEL == runTimeInterleave) {
            for (final Statement statement: statements) {  // XXX
                statement.run();  // XXX
            }  // XXX
        } else if (RunTimeInterleave.SEQUENTIAL == runTimeInterleave) {
            for (final Statement statement: statements) {
                statement.run();
            }
        }
    }
}
