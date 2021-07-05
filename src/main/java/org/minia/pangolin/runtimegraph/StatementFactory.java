package org.minia.pangolin.runtimegraph;

import lombok.val;
import org.apache.commons.lang3.NotImplementedException;
import org.minia.pangolin.semantics.UnboundIdentifierException;

public class StatementFactory {

    StatementFactory() {
        super();
    }

    public Statement from(
            final org.minia.pangolin.syntaxtree.Statement statement,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        if (org.minia.pangolin.syntaxtree.PrintStatement.class.equals(
                statement.getClass())) {
            val printStatement =
                    (org.minia.pangolin.syntaxtree.PrintStatement)
                    statement;
            return new PrintStatementFactory().from(
                    printStatement, whereValueBindings);
        }
        if (org.minia.pangolin.syntaxtree.NewLineStatement.class.equals(
                statement.getClass())) {
            return new NewLineStatementFactory().build();
        }
        if (org.minia.pangolin.syntaxtree.ExecuteStatement.class.equals(
                statement.getClass())){
            throw new NotImplementedException("FIXME");
        }
        throw new NotImplementedException("FIXME");
    }
}
