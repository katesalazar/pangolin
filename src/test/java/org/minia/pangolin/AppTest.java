package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.parser.LanguageNotRecognizedException;

/**
 * Unit test.
 */
public class AppTest extends TestCase {

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
        app.fakeableMain(new String[]{"greet", "--no-banner"});
    }

    public void test1() {
        val app = new App();
        assertNotNull(app);
        app.fakeableMain(
                new String[]{"run", "app", "examples/this_should_never_exist"});
    }

    public void test2() {
        val app = new App();
        assertNotNull(app);
        val result = app.fakeableMain(new String[]{
                "run", "app", "src/main/resources/single_where_clause/"});
        assertEquals(0, result);
    }

    public void test3() throws LanguageNotRecognizedException {
        val app = new App();
        val runAppResult =
                app.runApp("src/main/resources/single_where_clause/");
        assertEquals(true, runAppResult);
    }

    public void test4() throws LanguageNotRecognizedException {
        val app = new App();
        val runAppResult =
                app.runApp("src/main/resources/double_where_clause/");
        assertEquals(true, runAppResult);
    }

    public void test5() throws LanguageNotRecognizedException {
        val app = new App();
        val runAppResult = app.runApp("examples/0_hello_world");
        assertEquals(true, runAppResult);
    }

    public void test6() {
        val app = new App();
        assertNotNull(app);
        val result = app.fakeableMain(
                new String[]{"run", "app", "examples/0_hello_world"});
        assertEquals(0, result);
    }

    public void test7() throws LanguageNotRecognizedException {
        val app = new App();
        val runAppResult =
                app.runApp("src/main/resources/single_where_clause_and_run/");
        assertEquals(true, runAppResult);
    }

    public void test8() throws LanguageNotRecognizedException {
        val app = new App();
        val runAppResult =
                app.runApp("src/main/resources/double_where_clause_and_run/");
        assertEquals(true, runAppResult);
    }

    public void test9() throws LanguageNotRecognizedException {
        val app = new App();
        val runAppResult =
                app.runApp("src/main/resources/conditional_expression/");
        assertEquals(true, runAppResult);
    }

    public void test10() throws LanguageNotRecognizedException {
        val app = new App();
        val runAppResult =
                app.runApp("src/main/resources/two_functions/");
        assertEquals(true, runAppResult);
    }
}
