package org.minia.pangolin.syntaxtree;

public final class NamedFunctionCallExpression extends Expression {

    private final NamedFunctionCall namedFunctionCall;

    NamedFunctionCallExpression(final NamedFunctionCall namedFunctionCall) {
        super(null, null);
        this.namedFunctionCall = namedFunctionCall;
    }

    public static NamedFunctionCallExpression fromNamedFunctionCall(
            final NamedFunctionCall namedFunctionCall) {
        return new NamedFunctionCallExpression(namedFunctionCall);
    }
}
