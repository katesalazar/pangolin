package org.minia.pangolin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import lombok.var;

/**
 * Unit test.
 */
public class ParserTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParserTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ParserTest.class);
    }

    public void test0() {
        val document = new Document("comment  comment ends");
        val program = new Program(document);
        val parser = new Parser(program);
        val parseTree = parser.parse();
        assertEquals(0, parseTree.getNamedFunctions().size());
    }

    public void test1() {
        val document = new Document("" +
                "    function identifier stuff identifier ends " +
                "end function identifier stuff identifier ends");
        val program = new Program(document);
        val parser = new Parser(program);
        val parseTree = parser.parse();
        assertEquals(0, parseTree.getNamedFunctions().size());
    }

    public void test2() {
        val document = new Document("garbage");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            val parseTree = parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
