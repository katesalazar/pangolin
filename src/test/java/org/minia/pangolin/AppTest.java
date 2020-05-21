package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Unit test.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void test0() {
        val app = new App();
        assertNotNull(app);
        App.fakeableMain(new String[]{"run", "app", "examples/0_hello_world"});
    }

    public void test1() {
        val app = new App();
        assertNotNull(app);
        App.fakeableMain(new String[]{"greet", "--no-banner"});
    }

    public void test2() {
        val app = new App();
        assertNotNull(app);
        App.fakeableMain(
                new String[]{"run", "app", "examples/this_should_never_exist"});
    }
}
