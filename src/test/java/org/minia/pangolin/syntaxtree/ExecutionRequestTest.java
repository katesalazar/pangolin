package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;

/**
 * Unit test.
 */
public class ExecutionRequestTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ExecutionRequestTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ExecutionRequestTest.class);
    }

    public void test0() {

        val executionRequest = new ExecutionRequest(null);
        assertNull(executionRequest.getApplicationRequestedToBeExecutedName());
    }
}
