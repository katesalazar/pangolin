package org.minia.pangolin.syntaxtree;

import lombok.val;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.minia.pangolin.scanner.Token;

import static org.minia.pangolin.util.Util.forceAssert;

public abstract class Condition extends Expression {

    private final Expression leftHandSideExpression;

    private final Expression rightHandSideExpression;

    protected Condition(
            final Token leftHandSideToken, final Token rightHandSideToken) {
        super(null, null);
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
        byte left;
        if ("0".equals(leftHandSideToken.getNaturalLiteralContent())) {
            left = 0;
        } else {
            forceAssert(
                    "1".equals(leftHandSideToken.getNaturalLiteralContent()));
            left = (byte) 1;
        }

        forceAssert(NaturalLiteralExpression.class.equals(
                rightHandSideExpression.getClass()));
        val rightHandSideToken = (
                (NaturalLiteralExpression) rightHandSideExpression
        ).getNaturalLiteralToken();
        forceAssert(Token.Type.NATURAL_LITERAL == rightHandSideToken.getType());
        byte rite;
        if ("0".equals(rightHandSideToken.getNaturalLiteralContent())) {
            rite = 0;
        } else {
            forceAssert(
                    "1".equals(rightHandSideToken.getNaturalLiteralContent()));
            rite = (byte) 1;
        }

        if (left == rite) {
            return new ImmutablePair<>(true, false);
        }
        if (left == 0) {
            forceAssert(rite == 1);
            return new ImmutablePair<>(true, true);
        }
        forceAssert(left == 1);
        forceAssert(rite == 0);
        return new ImmutablePair<>(true, false);
    }
}
