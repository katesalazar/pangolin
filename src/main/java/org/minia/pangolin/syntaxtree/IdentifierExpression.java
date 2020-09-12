package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forceAssert;

public class IdentifierExpression extends Expression {

    @Getter
    private Token identifierToken;

    /** Invalidated default constructor. */
    private IdentifierExpression() {
        super(null, null);
        throw new UnsupportedOperationException("FIXME");
    }

    private IdentifierExpression(final Token identifierToken) {

        super(null, null);
        this.identifierToken = identifierToken;
    }

    private IdentifierExpression(
            final Boolean pure, final Token identifierToken) {
        super(pure, null);
        this.identifierToken = identifierToken;
    }

    public IdentifierExpression(
            final IdentifierExpression identifierExpression) {
        super(identifierExpression.getPure(), new UnknownExpressionType());
        this.identifierToken =
                new Token(identifierExpression.getIdentifierToken());
    }

    public static IdentifierExpression fromIdentifierToken(
            final Token token) {

        forceAssert(token.getType() == Token.Type.IDENTIFIER);
        return new IdentifierExpression(token);
    }

    public IdentifierExpression makePure() {

        return new IdentifierExpression(true, identifierToken);
    }
}
