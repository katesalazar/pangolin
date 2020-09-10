package org.minia.pangolin.runtimegraph;

public class NewLineOperation extends Operation {

    NewLineOperation() {

        super();
    }

    @SuppressWarnings({"java:S106"}) // "use a logger"
    public void run() {
        System.out.print('\n');
    }
}
