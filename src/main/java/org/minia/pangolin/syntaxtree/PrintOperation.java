package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forcedAssertion;

public class PrintOperation implements Operation {

    /**  <p>What to print. */
    @Getter private final Token token;

    public PrintOperation(final Token token) {

        if (token.getType() == Token.Type.IDENTIFIER) {
            forcedAssertion(token.getType() == Token.Type.IDENTIFIER);
            this.token = token;
        } else {
            forcedAssertion(token.getType() == Token.Type.STRING_LITERAL);
            this.token = token;
        }
    }
}
