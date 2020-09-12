package org.minia.pangolin.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

import static org.minia.pangolin.util.Util.forceAssert;

/**
 * Unit test.
 */
public class UtilTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public UtilTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(UtilTest.class);
    }

    public void test0() {
        val util = new Util();
        assertNotNull(util);
    }

    public void test1() {
        boolean excepted = false;
        try {
            forceAssert(false);
        } catch (AssertionError ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
