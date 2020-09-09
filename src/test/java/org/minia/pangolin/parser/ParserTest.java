package org.minia.pangolin.parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;
import lombok.var;
import org.minia.pangolin.Document;
import org.minia.pangolin.Program;
import org.minia.pangolin.scanner.Token;

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

    public void test0() throws LanguageNotRecognizedException {

        val document = new Document("comment comment ends");
        val program = new Program(document);
        val parser = new Parser(program);
        val parsedTrees = parser.parse();
        assertEquals(1, parsedTrees.size());
        val parseTree = parsedTrees.get(0);
        val parseTreeType = parseTree.getType();
        assertEquals(ParseTree.Type.EMPTY, parseTreeType);
    }

    public void test1() throws LanguageNotRecognizedException {

        val document = new Document("" +
                "    function identifier stuff identifier ends " +
                "end function identifier stuff identifier ends");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test2() throws LanguageNotRecognizedException {

        val document = new Document("garbage");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test3() throws LanguageNotRecognizedException {
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

    public void test4() throws LanguageNotRecognizedException {

        val document = new Document("comment  comment ends");
        val program = new Program(document);
        val parser = new Parser(program);
        val parsedTrees = parser.parse();
        assertEquals(1, parsedTrees.size());
        val parseTree = parsedTrees.get(0);
        val parseTreeType = parseTree.getType();
        assertEquals(ParseTree.Type.EMPTY, parseTreeType);
    }

    public void test5() throws LanguageNotRecognizedException {

        val document = new Document("function ");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test6() throws LanguageNotRecognizedException {

        val document = new Document(
                "function identifier stuff identifier ends ");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test7() throws LanguageNotRecognizedException {

        val document = new Document("end function function function function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test8() throws LanguageNotRecognizedException {

        val document = new Document(
                "function function function function function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test9() throws LanguageNotRecognizedException {

        val document = new Document(
                "function identifier stuff identifier ends function function " +
                        "function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test10() throws LanguageNotRecognizedException {

        val document = new Document(
                "function identifier stuff identifier ends end end function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test11() throws LanguageNotRecognizedException {

        val document = new Document(
                "function identifier stuff identifier ends end function " +
                        "function");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final IllegalStateException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void testCanReduceFunctionBody0() {

        val document = new Document(
                "function identifier main identifier ends is" +
                        " a command line interface application function " +
                        " and receives nothing at all" +
                        " and returns nothing at all" +
                        " so it causes side effects" +
                        " and does" +
                        " print" +
                        " string literal hello, world string literal ends" +
                        " and then");
        val program = new Program(document);
        val parser = new Parser(program);
        var excepted = false;
        try {
            parser.parse();
        } catch (final LanguageNotRecognizedException ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }
}
