package org.minia.pangolin.syntaxtree;

import lombok.val;
import org.minia.pangolin.scanner.Token;

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

    public static NamedFunctionCallExpression fromNamedFunctionIdentifier(
            final Token namedFunctionIdentifier) {
        val namedFunctionCall = new NamedFunctionCall(namedFunctionIdentifier);
        return new NamedFunctionCallExpression(namedFunctionCall);
    }
}
