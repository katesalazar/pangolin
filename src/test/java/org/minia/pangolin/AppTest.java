package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.apache.commons.lang3.NotImplementedException;
import org.minia.pangolin.parser.LanguageNotRecognizedException;

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
        val result = App.fakeableMain(
                new String[]{"run", "app", "examples/0_hello_world"});
        assertEquals(0, result);
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

    public void test3() {
        val app = new App();
        assertNotNull(app);
        val result = App.fakeableMain(new String[]{
                "run", "app", "src/main/resources/single_where_clause/"});
        assertEquals(0, result);
    }

    public void test4() throws LanguageNotRecognizedException {
        val runAppResult = App.runApp("examples/0_hello_world");
        assertEquals(true, runAppResult);
    }

    public void test5() throws LanguageNotRecognizedException {
        val runAppResult = App.runApp("src/main/resources/single_where_clause/");
        assertEquals(true, runAppResult);
    }
}
