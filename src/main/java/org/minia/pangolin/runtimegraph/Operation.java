package org.minia.pangolin.runtimegraph;

public abstract class Operation {

    @SuppressWarnings("java:S1116")  // No empty statements.
    protected Operation() {
        super();
        if (PrintOperation.class.equals(getClass())) {
            ;
        } else if (NewLineOperation.class.equals(getClass())) {
            ;
        } else {
            throw new UnsupportedOperationException("FIXME");
        }
    }

    public abstract void run();
}
