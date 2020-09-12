package org.minia.pangolin.runtimegraph;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import org.minia.pangolin.parser.LanguageNotRecognizedException;

import java.util.ArrayList;

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
        val rtgNamedFunctions = new ArrayList<NamedFunction>(0);
        val stApplication =
                new org.minia.pangolin.syntaxtree.Application(
                        "application name", "entry point function");
        Application rtgApplication = null;
        try {
            rtgApplication = Application.from(stApplication, rtgNamedFunctions);
        } catch (final LanguageNotRecognizedException ignore) {}
        val stExecutionRequest =
                new org.minia.pangolin.syntaxtree.ExecutionRequest(
                        "foo");
        val rtgApplications = new ArrayList<Application>(1);
        rtgApplications.add(rtgApplication);
        boolean excepted = false;
        try {
            ExecutionRequest.from(stExecutionRequest, rtgApplications);
        } catch (final LanguageNotRecognizedException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
