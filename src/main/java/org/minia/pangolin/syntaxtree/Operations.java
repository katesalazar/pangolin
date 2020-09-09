package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

/**  <p>A {@link List} of {@link Operation}s, however the class is
 * necessary because it has to be tagged with either parallel or
 * sequential execution. */
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

    @Getter private final List<Operation> operations;

    @Getter private final RunTimeInterleave  runTimeInterleave;

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

    /**  <p>The special case of a single operation isn't really
     * sequential nor parallel... is it? */
    public static Operations single(final Operation operation) {

        val operations = new ArrayList<Operation>(1);
        operations.add(operation);
        return new Operations(operations, RunTimeInterleave.ANY);
    }

    public Short size() {
        return operations == null ? 0 : (short) operations.size();
    }
}
