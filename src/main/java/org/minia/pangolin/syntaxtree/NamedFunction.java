package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public class NamedFunction {

    @Getter private final CharSequence name;

    @Getter private final Operations operations;

    @Getter private final WhereValueBindings whereValueBindings;

    public NamedFunction(
            final CharSequence name, final Operations operations,
            final WhereValueBindings whereValueBindings) {

        this.name = name;
        this.operations = operations;
        this.whereValueBindings = whereValueBindings;
    }
}
