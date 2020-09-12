package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forcedAssertion;

public class PrintStatement implements Statement {

    /**  <p>What to print. */
    @Getter private final Token token;

    public PrintStatement(final Token token) {

        if (token.getType() == Token.Type.IDENTIFIER) {
            this.token = token;
        } else if (token.getType() == Token.Type.STRING_LITERAL) {
            this.token = token;
        } else {
            forcedAssertion(token.getType() == Token.Type.NATURAL_LITERAL);
            this.token = token;
        }
    }
}
