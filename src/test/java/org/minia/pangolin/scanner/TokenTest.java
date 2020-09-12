package org.minia.pangolin.scanner;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

/**
 * Unit test.
 */
public class TokenTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TokenTest(final String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TokenTest.class);
    }

    public void test0() {
        boolean excepted = false;
        try {
            new Token(Token.Type.FUNCTION, "garbage");
        } catch (AssertionError ignore) {
            excepted = true;
        }
        assertTrue(excepted);
    }

    public void test1() {
        val stringFor = Token.stringFor(
                new Token(Token.Type.IDENTIFIER, "garbage"));
        assertEquals("garbage", stringFor);
    }

    public void testNotAnIdentifier0() {
        val token = new Token(Token.Type.A);
        assertTrue(token.notAnIdentifier());
    }

    public void testNotAnIdentifier1() {
        val token = new Token(Token.Type.IDENTIFIER, "foo");
        assertFalse(token.notAnIdentifier());
    }

    public void testToString0() {
        val token = new Token(Token.Type.A);
        val string = token.toString();
        assertEquals("a", string);
    }

    public void testToString1() {
        val token = new Token(Token.Type.IDENTIFIER, "foo");
        val string = token.toString();
        assertEquals("foo", string);
    }

    public void testToString2() {
        val token = new Token(Token.Type.STRING_LITERAL, "foo");
        val string = token.toString();
        assertEquals("foo", string);
    }

    public void testToString3() {
        val token = new Token(Token.Type.NATURAL_LITERAL, "foo");
        val string = token.toString();
        assertEquals("foo", string);
    }
}
