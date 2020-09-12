package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

/**
 * Unit test.
 */
public class FunctionCallTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FunctionCallTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(FunctionCallTest.class);
    }

    public void test0() {
        val functionCall = new FunctionCall();
        assertNotNull(functionCall);
    }

    public void test1() {
        val functionCall = new FunctionCall();
        val type = functionCall.type();
        assertEquals(UnknownExpressionType.class, type.getClass());
    }
}
