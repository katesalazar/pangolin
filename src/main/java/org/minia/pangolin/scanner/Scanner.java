package org.minia.pangolin.scanner;

import lombok.val;
import lombok.var;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.minia.pangolin.Program;

import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.util.Util.forcedAssertion;

public class Scanner {

    private final Program program;

    private final List<Token> deliveredTokens;

    private int index;

    private boolean expectingCommentEnd;

    private boolean expectingIdentifierEnd;

    private boolean expectingStringLiteralEnd;

    private StringBuilder currentIdentifier;

    private StringBuilder currentStringLiteral;

    public Scanner(final Program program) {
        this.program = program;
        deliveredTokens = new ArrayList<>(16);
        index = 0;
        expectingCommentEnd = false;
        expectingIdentifierEnd = false;
        expectingStringLiteralEnd = false;
        currentIdentifier = new StringBuilder(16);
        currentStringLiteral = new StringBuilder(16);
    }

    /**  <p>This can surely be optimized by not performing repeated
     * {@link CharSequence#length()} calls. */
    public boolean moreTokens() {
        val documents = program.getDocuments();
        forcedAssertion(documents.size() == 1);
        val document = documents.get(0);
        return index < document.getRaw().length();
    }

    @SuppressWarnings({"java:S1135", "java:S3776"}) /* Track uses of "TODO" tags. */ /* Cognitive Complexity of methods should not be too high. */
    public Token nextToken() {
        forcedAssertion(moreTokens());
        //   `documents.size() == 1`, as asserted inside `moreTokens()`.
        val documents = program.getDocuments();
        val document = documents.get(0);
        val documentRaw = document.getRaw();
        val documentRawAsString = documentRaw.toString();
        var remainingStuff = documentRawAsString.substring(index);
        int remainingStuffLength;
        Token returning = null;
        do {
            if (expectingCommentEnd) {
                if (remainingStuff.startsWith("comment ends")) {
                    index += "comment ends".length();
                    expectingCommentEnd = false;
                } else {
                    index += 1;
                }
            } else if (expectingIdentifierEnd) {
                if (remainingStuff.startsWith("identifier ends")) {
                    index += "identifier ends".length();
                    expectingIdentifierEnd = false;
                    removeTrailingWhitespaces(currentIdentifier);
                    returning = new Token(
                            Token.Type.IDENTIFIER, currentIdentifier);
                    currentIdentifier.setLength(0);
                } else if (remainingStuff.startsWith("ident ends")) {
                    index += "ident ends".length();
                    expectingIdentifierEnd = false;
                    removeTrailingWhitespaces(currentIdentifier);
                    returning = new Token(
                            Token.Type.IDENTIFIER, currentIdentifier);
                    currentIdentifier.setLength(0);
                } else if (remainingStuff.startsWith("id ends")) {
                    index += "id ends".length();
                    expectingIdentifierEnd = false;
                    removeTrailingWhitespaces(currentIdentifier);
                    returning = new Token(
                            Token.Type.IDENTIFIER, currentIdentifier);
                    currentIdentifier.setLength(0);
                } else {
                    currentIdentifier.append(remainingStuff.charAt(0));
                    index += 1;
                }
            } else if (expectingStringLiteralEnd)  {
                if (remainingStuff.startsWith("string literal ends")) {
                    index += "string literal ends".length();
                    expectingStringLiteralEnd = false;
                    removeTrailingWhitespaces(currentStringLiteral);
                    returning = new Token(
                            Token.Type.STRING_LITERAL, currentStringLiteral);
                    currentStringLiteral.setLength(0);
                } else {
                    currentStringLiteral.append(remainingStuff.charAt(0));
                    index += 1;
                }
            } else if (remainingStuff.startsWith("comment")) {
                index += "comment".length();
                expectingCommentEnd = true;
            } else if (remainingStuff.startsWith("identifier")) {
                index += "identifier".length();
                expectingIdentifierEnd = true;
            } else if (remainingStuff.startsWith("ident")) {
                index += "ident".length();
                expectingIdentifierEnd = true;
            } else if (remainingStuff.startsWith("id")) {
                index += "id".length();
                expectingIdentifierEnd = true;
            } else if (remainingStuff.startsWith("string literal")) {
                index += "string literal".length();
                expectingStringLiteralEnd = true;
            } else {
                val canConsumeTokenImmediately =
                        canConsumeTokenImmediately(remainingStuff);
                val canConsumeToken = canConsumeTokenImmediately.getLeft();
                if (canConsumeToken) {
                    val whichType = canConsumeTokenImmediately.getRight();
                    final Token token;
                    if (whichType == Token.Type.NATURAL_LITERAL) {
                        forcedAssertion(remainingStuff.startsWith("0"));  /* XXX */
                        token = new Token(whichType, "0");  /* XXX */
                    } else {  /* XXX */
                        token = new Token(whichType);
                    }  /* XXX */
                    index += Token.stringFor(token).length();
                    returning = token;
                } else if (remainingStuff.startsWith(" ")) {
                    index += 1;
                } else if (remainingStuff.startsWith("\t")) {
                    index += 1;
                } else if (remainingStuff.startsWith("\n")) {
                    index += 1;
                    /* lineFeedCharactersFound += 1; TODO */
                } else {
                    throw new IllegalStateException("FIXME");
                }
            }
            remainingStuff = documentRawAsString.substring(index);
            remainingStuffLength = remainingStuff.length();
        } while (remainingStuffLength > 0 && returning == null);
        deliveredTokens.add(returning);
        return returning;
    }

    @SuppressWarnings("java:S3776") /* Cognitive Complexity of methods should not be too high. */
    static Pair<Boolean, Token.Type> canConsumeTokenImmediately(
            final String stuff) {

        /*   Here the stuff is ordered alphabetically on the first
         * letter but reverse alphabetically on the second and other
         * letters. That is because preventing that token e.g.
         * `application` would trigger a wrong instantiation of token
         *  `a`, and token e.g. `then` would trigger a wrong
         * instantiation of token `the`. */
        if (stuff.startsWith(Token.LC_AT)) {
            return new ImmutablePair<>(true, Token.Type.AT);
        } else if (stuff.startsWith(Token.LC_AND)) {
            return new ImmutablePair<>(true, Token.Type.AND);
        } else if (stuff.startsWith(Token.LC_APPLICATION)) {
            return new ImmutablePair<>(true, Token.Type.APPLICATION);
        } else if (stuff.startsWith(Token.LC_ALL)) {
            return new ImmutablePair<>(true, Token.Type.ALL);
        } else if (stuff.startsWith(Token.LC_A)) {
            return new ImmutablePair<>(true, Token.Type.A);
        } else if (stuff.startsWith(Token.LC_BOUND)) {
            return new ImmutablePair<>(true, Token.Type.BOUND);
        } else if (stuff.startsWith(Token.LC_COMMAND)) {
            return new ImmutablePair<>(true, Token.Type.COMMAND);
        } else if (stuff.startsWith(Token.LC_CAUSES)) {
            return new ImmutablePair<>(true, Token.Type.CAUSES);
        } else if (stuff.startsWith(Token.LC_DOES)) {
            return new ImmutablePair<>(true, Token.Type.DOES);
        } else if (stuff.startsWith(Token.LC_ENTRY)) {
            return new ImmutablePair<>(true, Token.Type.ENTRY);
        } else if (stuff.startsWith(Token.LC_END)) {
            return new ImmutablePair<>(true, Token.Type.END);
        } else if (stuff.startsWith(Token.LC_EFFECTS)) {
            return new ImmutablePair<>(true, Token.Type.EFFECTS);
        } else if (stuff.startsWith(Token.LC_FUNCTION)) {
            return new ImmutablePair<>(true, Token.Type.FUNCTION);
        } else if (stuff.startsWith(Token.LC_IT)) {
            return new ImmutablePair<>(true, Token.Type.IT);
        } else if (stuff.startsWith(Token.LC_IS)) {
            return new ImmutablePair<>(true, Token.Type.IS);
        } else if (stuff.startsWith(Token.LC_INTERFACE)) {
            return new ImmutablePair<>(true, Token.Type.INTERFACE);
        } else if (stuff.startsWith(Token.LC_LINE)) {
            return new ImmutablePair<>(true, Token.Type.LINE);
        } else if (stuff.startsWith(Token.LC_NOTHING)) {
            return new ImmutablePair<>(true, Token.Type.NOTHING);
        } else if (stuff.startsWith(Token.LC_NEW)) {
            return new ImmutablePair<>(true, Token.Type.NEW);
        } else if (stuff.startsWith(Token.LC_PRINT)) {
            return new ImmutablePair<>(true, Token.Type.PRINT);
        } else if (stuff.startsWith(Token.LC_POINT)) {
            return new ImmutablePair<>(true, Token.Type.POINT);
        } else if (stuff.startsWith(Token.LC_RETURNS)) {
            return new ImmutablePair<>(true, Token.Type.RETURNS);
        } else if (stuff.startsWith(Token.LC_RECEIVES)) {
            return new ImmutablePair<>(true, Token.Type.RECEIVES);
        } else if (stuff.startsWith(Token.LC_RUN)) {
            return new ImmutablePair<>(true, Token.Type.RUN);
        } else if (stuff.startsWith(Token.LC_SO)) {
            return new ImmutablePair<>(true, Token.Type.SO);
        } else if (stuff.startsWith(Token.LC_SIDE)) {
            return new ImmutablePair<>(true, Token.Type.SIDE);
        } else if (stuff.startsWith(Token.LC_TO)) {
            return new ImmutablePair<>(true, Token.Type.TO);
        } else if (stuff.startsWith(Token.LC_THEN)) {
            return new ImmutablePair<>(true, Token.Type.THEN);
        } else if (stuff.startsWith(Token.LC_THE)) {
            return new ImmutablePair<>(true, Token.Type.THE);
        } else if (stuff.startsWith(Token.LC_WHERE)) {
            return new ImmutablePair<>(true, Token.Type.WHERE);
        } else if (stuff.startsWith("0")) {
            return new ImmutablePair<>(
                    true, Token.Type.NATURAL_LITERAL);
        }
        return new ImmutablePair<>(false, null);
    }

    /**  <p>Edits in place. */
    private static void removeTrailingWhitespaces(
            final StringBuilder sb) {

        var sbCurrentLength = sb.length();
        var indexOfLastCharOfSb = sbCurrentLength - 1;
        var trailingChar = sb.charAt(indexOfLastCharOfSb);
        while (trailingChar == ' ' || trailingChar == '\t') {
            sb.deleteCharAt(indexOfLastCharOfSb);
            sbCurrentLength = sb.length();
            indexOfLastCharOfSb = sbCurrentLength - 1;
            trailingChar = sb.charAt(indexOfLastCharOfSb);
        }
    }
}
