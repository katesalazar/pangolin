package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;

/**
 * Unit test.
 */
public class PrintStatementTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PrintStatementTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PrintStatementTest.class);
    }

    public void test0() {

        val token = new Token(Token.Type.STRING_LITERAL, "foo");
        val printStatement = new PrintStatement(token);
        assertNotNull(printStatement.getToken());
        assertSame(token, printStatement.getToken());
    }
}
