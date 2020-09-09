package org.minia.pangolin.syntaxtree;

import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forceAssert;

public class WhereValueBinding {

    final CharSequence identifier;

    final Expression expression;

    public WhereValueBinding(
            final Token identifierToken, final Expression expression) {

        super();
        forceAssert(identifierToken.getType() == Token.Type.IDENTIFIER);
        identifier = identifierToken.getIdentifierName();
        this.expression = expression;
    }

    public boolean bound(final CharSequence identifier) {
        return this.identifier.equals(identifier);
    }

    public Expression expressionFor(final CharSequence identifier) {
        if (this.identifier.equals(identifier)) {
            return expression;
        }
        throw new IllegalStateException("FIXME");
    }
}
