package org.minia.pangolin.syntaxtree;

public class NamedFunction {

    private final CharSequence name;

    private final Operations operations;

    public NamedFunction(
            final CharSequence name, Operations operations) {

        this.name = name;
        this.operations = operations;
    }
}
