package org.minia.pangolin.syntaxtree;

import org.minia.pangolin.scanner.Token;

public class NamedFunctionCall {

    private final Token namedFunctionIdentifier;

    NamedFunctionCall(final Token namedFunctionIdentifier) {
        super();
        this.namedFunctionIdentifier = namedFunctionIdentifier;
    }

    public ExpressionType type() {
        return new UnknownExpressionType();
    }
}
