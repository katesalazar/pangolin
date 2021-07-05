package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;

/**
 * Unit test.
 */
public class NamedFunctionCallTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NamedFunctionCallTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(NamedFunctionCallTest.class);
    }

    public void test0() {
        val namedFunctionCall =
                new NamedFunctionCall(new Token(Token.Type.IDENTIFIER, "foo"));
        assertNotNull(namedFunctionCall);
    }

    public void test1() {
        val namedFunctionCall =
                new NamedFunctionCall(new Token(Token.Type.IDENTIFIER, "foo"));
        val type = namedFunctionCall.type();
        assertEquals(UnknownExpressionType.class, type.getClass());
    }
}
