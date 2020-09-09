package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forceAssert;

public class NaturalLiteralExpression implements Expression {

    @Getter private Token naturalLiteralToken;

    /** Invalidated default constructor. */
    private NaturalLiteralExpression() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    private NaturalLiteralExpression(final Token naturalLiteralToken) {

        super();
        this.naturalLiteralToken = naturalLiteralToken;
    }

    public static NaturalLiteralExpression fromNaturalLiteralToken(
            final Token token) {

        forceAssert(token.getType() == Token.Type.NATURAL_LITERAL);
        return new NaturalLiteralExpression(token);
    }
}
