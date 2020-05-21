package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.var;

/**
 * Unit test.
 */
public class TokenTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TokenTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TokenTest.class);
    }

    public void test0() {
        var excepted = false;
        try {
            new Token(Token.Type.FUNCTION, "garbage");
        } catch (AssertionError ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
