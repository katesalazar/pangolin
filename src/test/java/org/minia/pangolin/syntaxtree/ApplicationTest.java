package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;

/**
 * Unit test.
 */
public class ApplicationTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ApplicationTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ApplicationTest.class);
    }

    public void test0() {

        val application = new Application(null, null);
        assertNull(application.getApplicationName());
        assertNull(application.getEntryPointFunctionName());
    }
}
