package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.var;

import static org.minia.pangolin.Util.forceAssert;

/**
 * Unit test for simple App.
 */
public class UtilTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public UtilTest( String testName )
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
    public void test0() {
        var excepted = false;
        try {
            forceAssert(false);
        } catch (AssertionError ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
