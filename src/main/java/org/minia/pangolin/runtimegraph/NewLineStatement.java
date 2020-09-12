package org.minia.pangolin.runtimegraph;

public class NewLineStatement extends Statement {

    NewLineStatement() {

        super();
    }

    @SuppressWarnings({"java:S106"}) // "use a logger"
    public void run() {
        System.out.print('\n');
    }
}
