package org.minia.pangolin.syntaxtree;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class WhereValueBindings {

    @Getter private final List<WhereValueBinding> whereValueBindingsList;

    public WhereValueBindings(final WhereValueBinding whereValueBinding) {
        super();
        whereValueBindingsList = new ArrayList<>(1);
        whereValueBindingsList.add(whereValueBinding);
    }

    public WhereValueBindings(
            final List<WhereValueBinding> whereValueBindings) {

        super();
        this.whereValueBindingsList = new ArrayList<>(whereValueBindings);
    }

    public WhereValueBindings(final WhereValueBindings whereValueBindings) {
        super();
        whereValueBindingsList =
                new ArrayList<>(whereValueBindings.whereValueBindingsList);
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

    /**  Is there any identifier bound to some expression? */
    public boolean identifierBoundToExpression(final Expression expression) {
        for (final WhereValueBinding whereValueBinding :
                whereValueBindingsList) {
            if (whereValueBinding.expression.equals(expression)) {
                return true;
            }
        }
        return false;
    }
}
