package org.minia.pangolin.syntaxtree;

import lombok.val;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forceAssert;

public class Condition {

    private final Expression leftHandSideExpression;

    private final Expression rightHandSideExpression;

    protected Condition(
            final Token leftHandSideToken, final Token rightHandSideToken) {
        super();
        leftHandSideExpression =
                NaturalLiteralExpression.fromNaturalLiteralToken(
                        leftHandSideToken);
        rightHandSideExpression =
                NaturalLiteralExpression.fromNaturalLiteralToken(
                        rightHandSideToken);
    }

    /**  <p>The element at the left of the {@link Pair} holds if the
     * value of this {@link Condition} can already be known at compile
     * time.
     *   <p>The element at the right holds whether that value is true or
     * false, but only in case the element in the left is true. */
    public Pair<Boolean, Boolean> resolvableAtCompileTime() {

        forceAssert(NaturalLiteralExpression.class.equals(
                leftHandSideExpression.getClass()));
        val leftHandSideToken = (
                (NaturalLiteralExpression) leftHandSideExpression
        ).getNaturalLiteralToken();
        forceAssert(Token.Type.NATURAL_LITERAL == leftHandSideToken.getType());
        forceAssert("0".equals(leftHandSideToken.getNaturalLiteralContent()));

        forceAssert(NaturalLiteralExpression.class.equals(
                rightHandSideExpression.getClass()));
        val rightHandSideToken = (
                (NaturalLiteralExpression) rightHandSideExpression
        ).getNaturalLiteralToken();
        forceAssert(Token.Type.NATURAL_LITERAL == rightHandSideToken.getType());
        forceAssert("1".equals(rightHandSideToken.getNaturalLiteralContent()));

        return new ImmutablePair<>(true, true);
    }
}
