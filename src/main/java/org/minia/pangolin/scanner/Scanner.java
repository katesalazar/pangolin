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

    public boolean moreTokens() {
        val documents = program.getDocuments();
        forcedAssertion(documents.size() == 1);
        val document = documents.get(0);
        return index < document.getRaw().length();
    }

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
                    returning = new Token(Token.Type.IDENTIFIER, currentIdentifier);
                } else {
                    currentIdentifier.append(remainingStuff.charAt(0));
                    index += 1;
                }
            } else if (expectingStringLiteralEnd)  {
                if (remainingStuff.startsWith("string literal ends")) {
                    index += "string literal ends".length();
                    expectingStringLiteralEnd = false;
                    removeTrailingWhitespaces(currentStringLiteral);
                    returning = new Token(Token.Type.STRING_LITERAL, currentStringLiteral);
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
            } else if (remainingStuff.startsWith("string literal")) {
                index += "string literal".length();
                expectingStringLiteralEnd = true;
            } else {
                val canConsumeTokenImmediately = canConsumeTokenImmediately(remainingStuff);
                val canConsumeToken = canConsumeTokenImmediately.getLeft();
                if (canConsumeToken) {
                    val whichType = canConsumeTokenImmediately.getRight();
                    val token = new Token(whichType);
                    index += Token.stringFor(token).length();
                    returning = token;
                } else if (remainingStuff.startsWith(" ")) {
                    index += 1;
                } else if (remainingStuff.startsWith("\t")) {
                    index += 1;
                } else if (remainingStuff.startsWith("\n")) {
                    index += 1;
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
        } else if (stuff.startsWith(Token.LC_THEN)) {
            return new ImmutablePair<>(true, Token.Type.THEN);
        } else if (stuff.startsWith(Token.LC_THE)) {
            return new ImmutablePair<>(true, Token.Type.THE);
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
