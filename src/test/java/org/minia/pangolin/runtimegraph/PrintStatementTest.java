package org.minia.pangolin.runtimegraph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.apache.commons.lang3.NotImplementedException;
import org.minia.pangolin.scanner.Token;
import org.minia.pangolin.semantics.UnboundIdentifierException;
import org.minia.pangolin.syntaxtree.WhereValueBinding;

import java.util.ArrayList;

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
        boolean excepted = false;
        try {
            new PrintStatement();
        } catch (final UnsupportedOperationException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test1() throws UnboundIdentifierException {
        val stExpression =
                org.minia.pangolin.syntaxtree.NaturalLiteralExpression.fromNaturalLiteralToken(
                        new Token(Token.Type.NATURAL_LITERAL, "0"));
        val stWhereValueBinding =
                new org.minia.pangolin.syntaxtree.WhereValueBinding(
                        new Token(Token.Type.IDENTIFIER, "foo"), stExpression);
        val stWhereValueBindings =
                new org.minia.pangolin.syntaxtree.WhereValueBindings(
                        stWhereValueBinding);
        val printStatement = new PrintStatement(
                new Token(Token.Type.IDENTIFIER, "foo"), stWhereValueBindings);
        assertNotNull(printStatement);
    }

    public void test2() throws UnboundIdentifierException {
        val stExpression =
                org.minia.pangolin.syntaxtree.IdentifierExpression.fromIdentifierToken(
                        new Token(Token.Type.IDENTIFIER, "bar"));
        val stWhereValueBinding =
                new org.minia.pangolin.syntaxtree.WhereValueBinding(
                        new Token(Token.Type.IDENTIFIER, "foo"), stExpression);
        val stWhereValueBindings =
                new org.minia.pangolin.syntaxtree.WhereValueBindings(
                        stWhereValueBinding);
        boolean excepted = false;
        try {
            new PrintStatement(
                    new Token(Token.Type.IDENTIFIER, "foo"),
                    stWhereValueBindings);
        } catch (final NotImplementedException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test3() throws UnboundIdentifierException {
        val stExpression0 =
                org.minia.pangolin.syntaxtree.NaturalLiteralExpression.fromNaturalLiteralToken(
                        new Token(Token.Type.NATURAL_LITERAL, "0"));
        val stWhereValueBinding0 =
                new org.minia.pangolin.syntaxtree.WhereValueBinding(
                        new Token(Token.Type.IDENTIFIER, "bar"), stExpression0);
        val stExpression1 =
                org.minia.pangolin.syntaxtree.IdentifierExpression.fromIdentifierToken(
                        new Token(Token.Type.IDENTIFIER, "bar"));
        val stWhereValueBinding1 =
                new org.minia.pangolin.syntaxtree.WhereValueBinding(
                        new Token(Token.Type.IDENTIFIER, "foo"), stExpression1);
        val stWhereValueBindingsList = new ArrayList<WhereValueBinding>(2);
        stWhereValueBindingsList.add(stWhereValueBinding0);
        stWhereValueBindingsList.add(stWhereValueBinding1);
        val stWhereValueBindings =
                new org.minia.pangolin.syntaxtree.WhereValueBindings(
                        stWhereValueBindingsList);
        val printStatement = new PrintStatement(
                new Token(Token.Type.IDENTIFIER, "foo"), stWhereValueBindings);
        assertNotNull(printStatement);
    }

    public void test4() throws UnboundIdentifierException {
        val stLtCondition = new org.minia.pangolin.syntaxtree.LessThanCondition(
                new Token(Token.Type.NATURAL_LITERAL, "0"),
                new Token(Token.Type.NATURAL_LITERAL, "1"));
        val stExpressionThen =
                org.minia.pangolin.syntaxtree.NaturalLiteralExpression.fromNaturalLiteralToken(
                        new Token(Token.Type.NATURAL_LITERAL, "0"));
        val stExpressionElse =
                org.minia.pangolin.syntaxtree.NaturalLiteralExpression.fromNaturalLiteralToken(
                        new Token(Token.Type.NATURAL_LITERAL, "1"));
        val stConditionalExpression =
                new org.minia.pangolin.syntaxtree.ConditionalExpression(
                        stLtCondition, stExpressionThen, stExpressionElse);
        val stWhereValueBinding =
                new org.minia.pangolin.syntaxtree.WhereValueBinding(
                        new Token(Token.Type.IDENTIFIER, "foo"),
                        stConditionalExpression);
        val stWhereValueBindingsList = new ArrayList<WhereValueBinding>(1);
        stWhereValueBindingsList.add(stWhereValueBinding);
        val stWhereValueBindings =
                new org.minia.pangolin.syntaxtree.WhereValueBindings(
                        stWhereValueBindingsList);
        val printStatement = new PrintStatement(
                new Token(Token.Type.IDENTIFIER, "foo"), stWhereValueBindings);
        assertNotNull(printStatement);
    }

    public void test5() throws UnboundIdentifierException {
        val stLtCondition = new org.minia.pangolin.syntaxtree.LessThanCondition(
                new Token(Token.Type.NATURAL_LITERAL, "1"),
                new Token(Token.Type.NATURAL_LITERAL, "0"));
        val stExpressionThen =
                org.minia.pangolin.syntaxtree.NaturalLiteralExpression.fromNaturalLiteralToken(
                        new Token(Token.Type.NATURAL_LITERAL, "0"));
        val stExpressionElse =
                org.minia.pangolin.syntaxtree.NaturalLiteralExpression.fromNaturalLiteralToken(
                        new Token(Token.Type.NATURAL_LITERAL, "1"));
        val stConditionalExpression =
                new org.minia.pangolin.syntaxtree.ConditionalExpression(
                        stLtCondition, stExpressionThen, stExpressionElse);
        val stWhereValueBinding =
                new org.minia.pangolin.syntaxtree.WhereValueBinding(
                        new Token(Token.Type.IDENTIFIER, "foo"),
                        stConditionalExpression);
        val stWhereValueBindingsList = new ArrayList<WhereValueBinding>(1);
        stWhereValueBindingsList.add(stWhereValueBinding);
        val stWhereValueBindings =
                new org.minia.pangolin.syntaxtree.WhereValueBindings(
                        stWhereValueBindingsList);
        val printStatement = new PrintStatement(
                new Token(Token.Type.IDENTIFIER, "foo"), stWhereValueBindings);
        assertNotNull(printStatement);
    }
}
