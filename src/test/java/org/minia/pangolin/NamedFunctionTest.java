package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

/**
 * Unit test.
 */
public class NamedFunctionTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NamedFunctionTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(NamedFunctionTest.class);
    }

    public void test0() {
        val namedFunction = new NamedFunction();
        assertNull(namedFunction.getOperations());
    }
}
