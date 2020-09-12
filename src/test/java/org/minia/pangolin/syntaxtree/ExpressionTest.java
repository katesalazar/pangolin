package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;

/**
 * Unit test.
 */
public class ExpressionTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ExpressionTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ExpressionTest.class);
    }

    public void test0() {
        val expression = NaturalLiteralExpression.fromNaturalLiteralToken(
                new Token(Token.Type.NATURAL_LITERAL, "0"));
        assertNotNull(expression);
    }

    public void test1() {
        val expression0 = NaturalLiteralExpression.fromNaturalLiteralToken(
                new Token(Token.Type.NATURAL_LITERAL, "0"));
        val expression1 = NaturalLiteralExpression.fromNaturalLiteralExpression(
                expression0);
        assertNotNull(expression1);
    }

    public void test2() {
        val tokenZero = new Token(Token.Type.NATURAL_LITERAL, "0");
        val tokenOne = new Token(Token.Type.NATURAL_LITERAL, "1");
        val condition = new LessThanCondition(tokenZero, tokenOne);
        val expressionThen =
                NaturalLiteralExpression.fromNaturalLiteralToken(tokenZero);
        val expressionElse =
                NaturalLiteralExpression.fromNaturalLiteralToken(tokenOne);
        val expression = new ConditionalExpression(
                condition, expressionThen, expressionElse);
        assertNotNull(expression);
    }

    public void test3() {
        val tokenZero = new Token(Token.Type.NATURAL_LITERAL, "0");
        val tokenOne = new Token(Token.Type.NATURAL_LITERAL, "1");
        val condition = new LessThanCondition(tokenZero, tokenOne);
        val expressionThen =
                NaturalLiteralExpression.fromNaturalLiteralToken(tokenZero);
        val expressionElse =
                NaturalLiteralExpression.fromNaturalLiteralToken(tokenOne);
        val expression0 = new ConditionalExpression(
                condition, expressionThen, expressionElse);
        val expression1 = new ConditionalExpression(expression0);
        assertNotNull(expression1);
    }
}
