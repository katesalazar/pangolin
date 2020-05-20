package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

/**
 * Unit test for simple App.
 */
public class ParseTreeTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParseTreeTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( ParseTreeTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        val parseTree = new ParseTree(null);
        assertTrue(parseTree.getNamedFunctions().isEmpty());
    }
}
