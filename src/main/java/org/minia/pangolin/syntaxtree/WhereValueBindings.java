package org.minia.pangolin.syntaxtree;

import java.util.ArrayList;
import java.util.List;

public class WhereValueBindings {

    @SuppressWarnings({"java:S1068"}) // "unused private field"
    private final List<WhereValueBinding> whereValueBindingsList;

    public WhereValueBindings(
            final List<WhereValueBinding> whereValueBindings) {

        super();
        this.whereValueBindingsList = new ArrayList<>(whereValueBindings);
    }
}
