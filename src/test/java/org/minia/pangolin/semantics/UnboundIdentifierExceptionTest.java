package org.minia.pangolin.semantics;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test.
 */
public class UnboundIdentifierExceptionTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public UnboundIdentifierExceptionTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(UnboundIdentifierExceptionTest.class);
    }

    public void test0() {
        boolean excepted = false;
        try {
            throw new UnboundIdentifierException("foo");
        } catch (final UnboundIdentifierException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
