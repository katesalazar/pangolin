package org.minia.pangolin.syntaxtree;

import lombok.val;

import java.util.ArrayList;

public class WhereValueBindingsFactory {

    public WhereValueBindings prepend(
            final WhereValueBinding whereValueBinding,
            final WhereValueBindings whereValueBindings) {
        val whereValueBindingsList =
                whereValueBindings.getWhereValueBindingsList();
        val whereValueBindingsListSize = whereValueBindingsList.size();
        val newWhereValueBindingsList = new ArrayList<WhereValueBinding>(
                whereValueBindingsListSize + 1);
        newWhereValueBindingsList.add(whereValueBinding);
        newWhereValueBindingsList.addAll(whereValueBindingsList);
        return new WhereValueBindings(newWhereValueBindingsList);
    }
}
