package org.minia.pangolin.parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import lombok.var;
import org.minia.pangolin.Document;
import org.minia.pangolin.Program;

import java.util.ArrayList;

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
        val document = new Document("comment comment ends");
        val program = new Program(document);
        val parser = new Parser(program);
        val parsedTrees = parser.parse();
        assertEquals(1, parsedTrees.size());
        val parseTree = parsedTrees.get(0);
        val parseTreeType = parseTree.getType();
        assertEquals(ParseTree.Type.EMPTY, parseTreeType);
    }

    public void test1() {
        val document = new Document("" +
                "    function identifier stuff identifier ends " +
                "end function identifier stuff identifier ends");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test2() {
        val document = new Document("garbage");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test3() {
        val aDocument = new Document("garbage");
        val anotherDocument = new Document("more garbage");
        val documents = new ArrayList<Document>(2);
        documents.add(aDocument);
        documents.add(anotherDocument);
        val program = new Program(documents);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (AssertionError ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test4() {
        val document = new Document("comment  comment ends");
        val program = new Program(document);
        val parser = new Parser(program);
        val parsedTrees = parser.parse();
        assertEquals(1, parsedTrees.size());
        val parseTree = parsedTrees.get(0);
        val parseTreeType = parseTree.getType();
        assertEquals(ParseTree.Type.EMPTY, parseTreeType);
    }

    public void test5() {
        val document = new Document("function ");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test6() {
        val document = new Document("function identifier stuff identifier ends ");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test7() {
        val document = new Document("end function function function function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test8() {
        val document = new Document("function function function function function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test9() {
        val document = new Document("function identifier stuff identifier ends function function function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test10() {
        val document = new Document("function identifier stuff identifier ends end end function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test11() {
        val document = new Document("function identifier stuff identifier ends end function function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
