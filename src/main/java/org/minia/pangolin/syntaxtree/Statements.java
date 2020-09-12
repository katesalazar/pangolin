package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

/**  <p>A {@link List} of {@link Statement}s, however the class is
 * necessary because it has to be tagged with either parallel or
 * sequential execution. */
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

    @Getter private final List<Statement> statements;

    @Getter private final RunTimeInterleave runTimeInterleave;

    /**  <p>The default constructor is private. Construct instances by
     * using either {@link #parallel(List)} or {@link
     * #sequential(List)}.
     *   @param statements The operations to be executed.
     *   @param runTimeInterleave The run time interleaving to be used
     * when running the `operations`. */
    private Statements(
            final List<Statement> statements,
            final RunTimeInterleave runTimeInterleave) {

        this.statements = statements;
        this.runTimeInterleave = runTimeInterleave;
    }

    private Statements(final Statements statements) {
        this.statements = statements.statements;
        runTimeInterleave = statements.runTimeInterleave;
    }

    /**  <p>Construct instances of this class by using either this or
     * {@link #sequential(List)}.
     *   @see #Statements(List, RunTimeInterleave) */
    public static Statements parallel(
            final List<Statement> statements) {

        return new Statements(statements, RunTimeInterleave.PARALLEL);
    }

    /**  <p>Construct instances of this class by using either this or
     * {@link #parallel(List)}.
     *   @see #Statements(List, RunTimeInterleave) */
    public static Statements sequential(
            final List<Statement> statements) {

        return new Statements(statements, RunTimeInterleave.SEQUENTIAL);
    }

    /**  <p>The special case of a single operation isn't really
     * sequential nor parallel... is it? */
    public static Statements single(final Statement statement) {

        val statements = new ArrayList<Statement>(1);
        statements.add(statement);
        return new Statements(statements, RunTimeInterleave.ANY);
    }

    public static Statements copy(final Statements statements) {
        return new Statements(statements);
    }

    public Short size() {
        return statements == null ? 0 : (short) statements.size();
    }
}
