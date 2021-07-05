package org.minia.pangolin.runtimegraph;

public class NamedFunctionCall {

    public final NamedFunction namedFunction;

    public NamedFunctionCall(final NamedFunction namedFunction) {
        super();
        this.namedFunction = namedFunction;
    }

    public void compute() {
        namedFunction.compute();
    }

    public void run() {
        namedFunction.run();
    }
}
