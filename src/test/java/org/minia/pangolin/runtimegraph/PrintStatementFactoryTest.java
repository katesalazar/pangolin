package org.minia.pangolin.runtimegraph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;
import org.minia.pangolin.semantics.UnboundIdentifierException;

/**
 * Unit test.
 */
public class PrintStatementFactoryTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PrintStatementFactoryTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PrintStatementFactoryTest.class);
    }

    public void test0() throws UnboundIdentifierException {
        val token = new Token(Token.Type.NATURAL_LITERAL, "foo");
        val stPrintStatement =
                new org.minia.pangolin.syntaxtree.PrintStatement(token);
        val factory = new PrintStatementFactory();
        assertNotNull(factory);
        val printStatement = factory.from(stPrintStatement, null);
        assertNotNull(printStatement);
    }
}
