package org.minia.pangolin.syntaxtree;

import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forcedAssertion;

public class PrintOperation implements Operation {

    private final Token token;

    public PrintOperation(final Token token) {
        forcedAssertion(token.getType() == Token.Type.STRING_LITERAL);
        this.token = token;
    }
}
