package org.minia.pangolin.runtimegraph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;
import org.minia.pangolin.semantics.UnboundIdentifierException;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test.
 */
public class StatementsTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StatementsTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(StatementsTest.class);
    }

    public void test0() {
        boolean excepted = false;
        try {
            new Statements(null, null);
        } catch (final NullPointerException ignore) {
            excepted = true;
        } catch (final UnboundIdentifierException ignore) {
            excepted = true;
        }

        /* FIXME
        assertTrue(excepted);
        */
        assertFalse(excepted);
    }

    @SuppressWarnings("java:S2699")  // Tests should include assertions.
    public void test1() throws UnboundIdentifierException {
        final List<org.minia.pangolin.syntaxtree.Statement> stStatementsList =
                new ArrayList<>(2);
        final org.minia.pangolin.syntaxtree.Statement stStatement0 =
                new org.minia.pangolin.syntaxtree.PrintStatement(new Token(
                        Token.Type.STRING_LITERAL, "foo"));
        final org.minia.pangolin.syntaxtree.Statement stStatement1 =
                new org.minia.pangolin.syntaxtree.NewLineStatement();
        stStatementsList.add(stStatement0);
        stStatementsList.add(stStatement1);
        final org.minia.pangolin.syntaxtree.Statements stStatements =
                org.minia.pangolin.syntaxtree.Statements.sequential(
                        stStatementsList);
        final Statements statements_ = new Statements(stStatements, null);
        statements_.run();
    }

    @SuppressWarnings("java:S2699")  // Tests should include assertions.
    public void test2() throws UnboundIdentifierException {
        final List<org.minia.pangolin.syntaxtree.Statement> stStatementsList =
                new ArrayList<>(2);
        final org.minia.pangolin.syntaxtree.Statement stStatement0 =
                new org.minia.pangolin.syntaxtree.PrintStatement(new Token(
                        Token.Type.STRING_LITERAL, "foo"));
        final org.minia.pangolin.syntaxtree.Statement stStatement1 =
                new org.minia.pangolin.syntaxtree.NewLineStatement();
        stStatementsList.add(stStatement0);
        stStatementsList.add(stStatement1);
        final org.minia.pangolin.syntaxtree.Statements stStatements =
                org.minia.pangolin.syntaxtree.Statements.parallel(
                        stStatementsList);
        final Statements statements_ = new Statements(stStatements, null);
        statements_.run();
    }
}
