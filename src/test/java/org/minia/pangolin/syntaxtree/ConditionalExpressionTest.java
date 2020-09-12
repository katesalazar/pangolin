package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;

/**
 * Unit test.
 */
public class ConditionalExpressionTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ConditionalExpressionTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ConditionalExpressionTest.class);
    }

    public void test0() {
        boolean excepted = false;
        try {
            new ConditionalExpression();
        } catch (final UnsupportedOperationException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test1() {
        val condition = new LessThanCondition(
                new Token(Token.Type.NATURAL_LITERAL, "0"),
                new Token(Token.Type.NATURAL_LITERAL, "1"));
        val expressionThen = NaturalLiteralExpression.fromNaturalLiteralToken(
                new Token(Token.Type.NATURAL_LITERAL, "0"));
        val expressionElse = NaturalLiteralExpression.fromNaturalLiteralToken(
                new Token(Token.Type.NATURAL_LITERAL, "1"));
        val conditionalExpression0 = new ConditionalExpression(
                condition, expressionThen, expressionElse);
        assertNotNull(conditionalExpression0);
        val conditionalExpression1 = new ConditionalExpression(
                conditionalExpression0);
        assertNotNull(conditionalExpression1);
    }
}
