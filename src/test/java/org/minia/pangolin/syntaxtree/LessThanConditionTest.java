package org.minia.pangolin.syntaxtree;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.var;

/**
 * Unit test.
 */
public class LessThanConditionTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LessThanConditionTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(LessThanConditionTest.class);
    }

    public void test0() {
        var excepted = false;
        try {
            new LessThanCondition(null, null);
        } catch (final NullPointerException ignore) {
            excepted = true;
        } catch (final UnsupportedOperationException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
