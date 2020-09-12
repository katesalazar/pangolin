package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

/**
 * Unit test.
 */
public class NamedFunctionBodyTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NamedFunctionBodyTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(NamedFunctionBodyTest.class);
    }

    public void test0() {
        boolean excepted = false;
        try {
            new NamedFunctionBody();
        } catch (final UnsupportedOperationException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
