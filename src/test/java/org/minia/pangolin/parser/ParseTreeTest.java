package org.minia.pangolin.parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.Program;

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
        val parseTree = new ParseTree((Program) null);
        val raw = parseTree.getProgram();
        assertNull(raw);
    }
}
