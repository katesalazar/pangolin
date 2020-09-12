package org.minia.pangolin.runtimegraph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

/**
 * Unit test.
 */
public class NewLineStatementFactoryTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public NewLineStatementFactoryTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(NewLineStatementFactoryTest.class);
    }

    public void test0() {
        val factory = new NewLineStatementFactory();
        assertNotNull(factory);
        val statement = factory.build();
        assertNotNull(statement);
    }
}
