package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public class NamedFunction {

    @Getter
    private final CharSequence name;

    @Getter
    private final Operations operations;

    public NamedFunction(
            final CharSequence name, Operations operations) {

        this.name = name;
        this.operations = operations;
    }
}
