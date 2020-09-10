package org.minia.pangolin.runtimegraph;

import lombok.val;
import org.apache.commons.lang3.NotImplementedException;
import org.minia.pangolin.semantics.UnboundIdentifierException;
import org.minia.pangolin.syntaxtree.NaturalLiteralExpression;

import static org.minia.pangolin.util.Util.forceAssert;

public class PrintOperation extends Operation {

    /**  <p>Direct {@link CharSequence} to be printed, if that is
     * possible for this {@link PrintOperation}. */
    private final CharSequence charSequence;

    private PrintOperation() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    @SuppressWarnings({"java:S1192"})  // Do not duplicate string literals.
    PrintOperation(
            final org.minia.pangolin.scanner.Token token,
            final org.minia.pangolin.syntaxtree.WhereValueBindings whereValueBindings)
                    throws UnboundIdentifierException {

        super();
        if (org.minia.pangolin.scanner.Token.Type.IDENTIFIER ==
                token.getType()) {
            if (whereValueBindings.bound(token.getIdentifierName())) {
                val expression = whereValueBindings.expressionFor(token.getIdentifierName());
                if (org.minia.pangolin.syntaxtree.NaturalLiteralExpression.class.equals(
                        expression.getClass())) {
                    final org.minia.pangolin.syntaxtree.NaturalLiteralExpression naturalLiteralExpression =
                            (org.minia.pangolin.syntaxtree.NaturalLiteralExpression)
                            expression;
                    val naturalLiteralToken =
                            naturalLiteralExpression.getNaturalLiteralToken();
                    forceAssert(org.minia.pangolin.scanner.Token.Type.NATURAL_LITERAL ==
                            naturalLiteralToken.getType());
                    charSequence =
                            naturalLiteralToken.getNaturalLiteralContent();
                } else if (org.minia.pangolin.syntaxtree.IdentifierExpression.class.equals(
                            expression.getClass())) {
                    final org.minia.pangolin.syntaxtree.IdentifierExpression identifierExpression =
                            (org.minia.pangolin.syntaxtree.IdentifierExpression)
                            expression;
                    val identifierToken =
                            identifierExpression.getIdentifierToken();
                    if (whereValueBindings.bound(
                            identifierToken.getIdentifierName())) {
                        val expressionTwo = whereValueBindings.expressionFor(identifierToken.getIdentifierName());
                        forceAssert(org.minia.pangolin.syntaxtree.NaturalLiteralExpression.class.equals(
                                expressionTwo.getClass()));
                        final org.minia.pangolin.syntaxtree.NaturalLiteralExpression naturalLiteralExpressionTwo =
                                (org.minia.pangolin.syntaxtree.NaturalLiteralExpression)
                                expressionTwo;
                        val naturalLiteralTokenTwo =
                                naturalLiteralExpressionTwo.getNaturalLiteralToken();
                        forceAssert(org.minia.pangolin.scanner.Token.Type.NATURAL_LITERAL ==
                                naturalLiteralTokenTwo.getType());
                        charSequence =
                                naturalLiteralTokenTwo.getNaturalLiteralContent();
                    } else {
                        throw new NotImplementedException(
                                "branch not yet implemented");
                    }
                } else if (org.minia.pangolin.syntaxtree.ConditionalExpression.class.equals(
                        expression.getClass())) {
                    final org.minia.pangolin.syntaxtree.ConditionalExpression conditionalExpression =
                            (org.minia.pangolin.syntaxtree.ConditionalExpression)
                            expression;
                    val conditionalExpressionResolvableAtCompileTimeResult =
                            conditionalExpression.resolvableAtCompileTime();
                    val conditionalExpressionResolvableAtCompileTime =
                            conditionalExpressionResolvableAtCompileTimeResult.getLeft();
                    if (conditionalExpressionResolvableAtCompileTime) {
                        if (conditionalExpressionResolvableAtCompileTimeResult.getRight()) {
                            val expressionThen =
                                    conditionalExpression.getExpressionThen();
                            forceAssert(NaturalLiteralExpression.class.equals(
                                    expressionThen.getClass()));
                            val tokenThen = (
                                    (NaturalLiteralExpression) expressionThen
                            ).getNaturalLiteralToken();
                            forceAssert(org.minia.pangolin.scanner.Token.Type.NATURAL_LITERAL ==
                                    tokenThen.getType());
                            charSequence = tokenThen.getNaturalLiteralContent();
                        } else {
                            val expressionElse =
                                    conditionalExpression.getExpressionElse();
                            forceAssert(NaturalLiteralExpression.class.equals(
                                    expressionElse.getClass()));
                            val tokenElse = (
                                    (NaturalLiteralExpression) expressionElse
                            ).getNaturalLiteralToken();
                            forceAssert(org.minia.pangolin.scanner.Token.Type.NATURAL_LITERAL ==
                                    tokenElse.getType());
                            charSequence = tokenElse.getNaturalLiteralContent();
                        }
                    } else {
                        throw new NotImplementedException(
                                "branch not yet implemented");
                    }
                } else {
                    throw new NotImplementedException(
                            "branch not yet implemented");
                }
            } else {
                throw new UnboundIdentifierException("" +
                        "Unbound identifier '" + token.getIdentifierName() +
                        "'");
            }
        } else {
            forceAssert(org.minia.pangolin.scanner.Token.Type.STRING_LITERAL ==
                    token.getType());
            charSequence = token.getStringLiteralContent();
        }
    }

    @SuppressWarnings({"java:S106"}) // "use a logger"
    public void run() {
        System.out.print(charSequence);
    }
}
