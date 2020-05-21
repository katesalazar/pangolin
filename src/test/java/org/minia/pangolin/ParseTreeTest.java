package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

/**
 * Unit test.
 */
public class ParseTreeTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParseTreeTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ParseTreeTest.class);
    }

    public void test0() {
        val parseTree = new ParseTree(null);
        assertTrue(parseTree.getNamedFunctions().isEmpty());
        val raw = parseTree.getRaw();
        assertNull(raw);
    }
}
