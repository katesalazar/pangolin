package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forceAssert;

public class IdentifierExpression implements Expression {

    @Getter
    private Token identifierToken;

    /** Invalidated default constructor. */
    private IdentifierExpression() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    private IdentifierExpression(final Token identifierToken) {

        super();
        this.identifierToken = identifierToken;
    }

    public static IdentifierExpression fromIdentifierToken(
            final Token token) {

        forceAssert(token.getType() == Token.Type.IDENTIFIER);
        return new IdentifierExpression(token);
    }
}
