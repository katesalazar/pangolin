package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import lombok.var;

import java.util.ArrayList;

/**
 * Unit test.
 */
public class ScannerTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ScannerTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ScannerTest.class);
    }

    public void test0() {
        val aDocument = new Document("garbage");
        val anotherDocument = new Document("more garbage");
        val documents = new ArrayList<Document>(2);
        documents.add(aDocument);
        documents.add(anotherDocument);
        val program = new Program(documents);
        val scanner = new Scanner(program);
        var excepted = false;
        try {
            scanner.moreTokens();
        } catch (AssertionError ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    /**  Not covering any case not covered already by {@link #test0}.
     * Should remove this later. */
    public void test1() {
        val aDocument = new Document("garbage");
        val anotherDocument = new Document("more garbage");
        val documents = new ArrayList<Document>(2);
        documents.add(aDocument);
        documents.add(anotherDocument);
        val program = new Program(documents);
        val scanner = new Scanner(program);
        var excepted = false;
        try {
            scanner.nextToken();
        } catch (AssertionError ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
