package org.minia.pangolin.runtimegraph;

public abstract class Statement {

    @SuppressWarnings("java:S1116")  // No empty statements.
    protected Statement() {
        super();
        if (PrintStatement.class.equals(getClass())) {
            ;
        } else if (NewLineStatement.class.equals(getClass())) {
            ;
        } else {
            throw new UnsupportedOperationException("FIXME");
        }
    }

    public abstract void run();
}
