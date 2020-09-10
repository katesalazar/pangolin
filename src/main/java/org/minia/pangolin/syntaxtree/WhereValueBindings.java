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

    public boolean bound(final CharSequence identifier) {
        for (final WhereValueBinding whereValueBinding:
                whereValueBindingsList) {
            if (whereValueBinding.bound(identifier)) {
                return true;
            }
        }
        return false;
    }

    public Expression expressionFor(final CharSequence identifier) {
        for (final WhereValueBinding whereValueBinding:
                whereValueBindingsList) {
            if (whereValueBinding.bound(identifier)) {
                return whereValueBinding.expressionFor(identifier);
            }
        }
        throw new IllegalStateException("FIXME");
    }
}
