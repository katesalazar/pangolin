package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.scanner.Token;
import org.minia.pangolin.semantics.UnboundIdentifierException;

/**
 * Unit test.
 */
public class SideEffectsExpressionTypeTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SideEffectsExpressionTypeTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(SideEffectsExpressionTypeTest.class);
    }

    public void test0() {
        val sideEffectsExpressionTypeTest = new SideEffectsExpressionType();
        assertNotNull(sideEffectsExpressionTypeTest);
    }
}
