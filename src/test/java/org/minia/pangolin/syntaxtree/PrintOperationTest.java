package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;

/**
 * Unit test.
 */
public class PrintOperationTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PrintOperationTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PrintOperationTest.class);
    }

    public void test0() {

        val token = new Token(Token.Type.STRING_LITERAL, "foo");
        val printOperation = new PrintOperation(token);
        assertNotNull(printOperation.getToken());
        assertSame(token, printOperation.getToken());
    }
}
