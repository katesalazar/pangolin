package org.minia.pangolin.syntaxtree;

import lombok.Getter;
import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forceAssert;

public final class NaturalLiteralExpression extends Expression {

    @Getter private Token naturalLiteralToken;

    /** Invalidated default constructor. */
    NaturalLiteralExpression() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    private NaturalLiteralExpression(final Token naturalLiteralToken) {

        super(true, new NaturalExpressionType());
        this.naturalLiteralToken = naturalLiteralToken;
    }

    private NaturalLiteralExpression(
            final NaturalLiteralExpression naturalLiteralExpression) {
        super(true, new NaturalExpressionType());
        this.naturalLiteralToken =
                naturalLiteralExpression.naturalLiteralToken;
    }

    public static NaturalLiteralExpression fromNaturalLiteralToken(
            final Token token) {

        forceAssert(token.getType() == Token.Type.NATURAL_LITERAL);
        return new NaturalLiteralExpression(token);
    }

    public static NaturalLiteralExpression fromNaturalLiteralExpression(
            final NaturalLiteralExpression naturalLiteralExpression) {
        return new NaturalLiteralExpression(naturalLiteralExpression);
    }
}
