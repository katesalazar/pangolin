package org.minia.pangolin.syntaxtree;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString public final class UnionExpressionType
        extends ExpressionType {

    private final List<ExpressionType> unitedExpressionTypes;

    UnionExpressionType() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

     UnionExpressionType(
            final ExpressionType type0, final ExpressionType type1) {
        super();
        unitedExpressionTypes = new ArrayList<>(2);
        unitedExpressionTypes.add(type0);
        unitedExpressionTypes.add(type1);
    }
}
