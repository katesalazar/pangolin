package org.minia.pangolin.runtimegraph;

public class NewLineStatement extends Statement {

    NewLineStatement() {

        super();
    }

    public void compute() {
        throw new IllegalStateException(
                "can not compute a print statement, only run it");
    }

    @SuppressWarnings({"java:S106"}) // "use a logger"
    public void run() {
        System.out.print('\n');
    }
}
