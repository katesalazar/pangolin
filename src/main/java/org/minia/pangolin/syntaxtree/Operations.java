package org.minia.pangolin.syntaxtree;

import java.util.List;

/**  <p>A {@link List} of {@link Operation}s, however the class is
 * necessary because it has to be tagged with either parallel or
 * sequential execution. */
public class Operations {

    private enum RunTimeInterleave { PARALLEL, SEQUENTIAL }

    private final List<Operation> operations;

    private final RunTimeInterleave  runTimeInterleave;

    /**  <p>The default constructor is private. Construct instances by
     * using either {@link #parallel(List)} or {@link
     * #sequential(List)}.
     *   @param operations The operations to be executed.
     *   @param runTimeInterleave The run time interleaving to be used
     * when running the `operations`. */
    private Operations(
            final List<Operation> operations,
            final RunTimeInterleave runTimeInterleave) {

        this.operations = operations;
        this.runTimeInterleave = runTimeInterleave;
    }

    /**  <p>Construct instances of this class by using either this or
     * {@link #sequential(List)}.
     *   @see #Operations(List, RunTimeInterleave) */
    public static Operations parallel(
            final List<Operation> operations) {

        return new Operations(operations, RunTimeInterleave.PARALLEL);
    }

    /**  <p>Construct instances of this class by using either this or
     * {@link #parallel(List)}.
     *   @see #Operations(List, RunTimeInterleave) */
    public static Operations sequential(
            final List<Operation> operations) {

        return new Operations(operations, RunTimeInterleave.SEQUENTIAL);
    }
}
