package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.minia.pangolin.scanner.Token;

/**
 * Unit test.
 */
public class FunctionCallExpressionTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FunctionCallExpressionTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(FunctionCallExpressionTest.class);
    }


    public void test0() {
        boolean excepted = false;
        try {
            new NamedFunctionCallExpression(new NamedFunctionCall(new Token(
                    Token.Type.IDENTIFIER, "foo")));
        } catch (final UnsupportedOperationException ignore) {
            excepted = true;
        }
        assertFalse(excepted);
    }
}
