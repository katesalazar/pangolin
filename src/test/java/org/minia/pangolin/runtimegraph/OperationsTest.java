package org.minia.pangolin.runtimegraph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.var;
import org.minia.pangolin.semantics.UnboundIdentifierException;

/**
 * Unit test.
 */
public class OperationsTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OperationsTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(OperationsTest.class);
    }

    public void test0() {
        var excepted = false;
        try {
            new Operations(null, null);
        } catch (final NullPointerException ignore) {
            excepted = true;
        } catch (final UnboundIdentifierException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
