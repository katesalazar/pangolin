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
public class StatementFactoryTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StatementFactoryTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(StatementFactoryTest.class);
    }

    public void test0() {
        val token = new Token(Token.Type.IDENTIFIER, "foo");
        final org.minia.pangolin.syntaxtree.Statement stStatement =
                /* (org.minia.pangolin.syntaxtree.Statement) */
                new org.minia.pangolin.syntaxtree.PrintStatement(token);
        val factory = new StatementFactory();
        assertNotNull(factory);
        boolean excepted = false;
        try {
            factory.from(stStatement, null);
        } catch (final UnboundIdentifierException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test1() throws UnboundIdentifierException {
        val token = new Token(Token.Type.STRING_LITERAL, "foo");
        final org.minia.pangolin.syntaxtree.Statement stStatement =
                /* (org.minia.pangolin.syntaxtree.Statement) */
                new org.minia.pangolin.syntaxtree.PrintStatement(token);
        val factory = new StatementFactory();
        assertNotNull(factory);
        val printStatement = factory.from(stStatement, null);
        assertNotNull(printStatement);
    }

    public void test2() throws UnboundIdentifierException {
        val token = new Token(Token.Type.NATURAL_LITERAL, "foo");
        final org.minia.pangolin.syntaxtree.Statement stStatement =
                /* (org.minia.pangolin.syntaxtree.Statement) */
                new org.minia.pangolin.syntaxtree.PrintStatement(token);
        val factory = new StatementFactory();
        assertNotNull(factory);
        val printStatement = factory.from(stStatement, null);
        assertNotNull(printStatement);
    }
}
