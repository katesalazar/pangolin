package org.minia.pangolin.runtimegraph;

import lombok.val;

public abstract class Statement {

    @SuppressWarnings("java:S1116")  // No empty statements.
    protected Statement() {
        super();
        val clazz = getClass();
        if (PrintStatement.class.equals(clazz))  {
            ;
        } else if (NewLineStatement.class.equals(clazz)) {
            ;
        } else if (ExecuteStatement.class.equals(clazz)) {
            ;
        } else {
            throw new UnsupportedOperationException(clazz.toString());
        }
    }

    public abstract void compute();

    public abstract void run();
}
