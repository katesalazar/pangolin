package org.minia.pangolin;

import lombok.val;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.Util.forcedAssertion;

public class Scanner {

    private final Program program;

    private final List<Token> deliveredTokens;

    private int index;

    private boolean expectingCommentEnd;

    private boolean expectingIdentifierEnd;

    private StringBuilder currentIdentifier;

    public Scanner(final Program program) {
        this.program = program;
        deliveredTokens = new ArrayList<>(16);
        index = 0;
        expectingCommentEnd = false;
        expectingIdentifierEnd = false;
        currentIdentifier = new StringBuilder(16);
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
                    //   Remove any whitespace before the "identifier ends" sequence.
                    currentIdentifier.deleteCharAt(currentIdentifier.length() - 1);
                    returning = Token.newIdentifierToken(currentIdentifier);
                } else {
                    currentIdentifier.append(remainingStuff.charAt(0));
                    index += 1;
                }
            } else if (remainingStuff.startsWith("comment")) {
                index += "comment".length();
                expectingCommentEnd = true;
            } else if (remainingStuff.startsWith("end")) {
                index += "end".length();
                returning = Token.newEndToken();
            } else if (remainingStuff.startsWith("function")) {
                index += "function".length();
                returning = Token.newFunctionToken();
            } else if (remainingStuff.startsWith("identifier")) {
                index += "identifier".length();
                expectingIdentifierEnd = true;
            } else if (remainingStuff.startsWith(" ")) {
                index += 1;
            } else {
                throw new IllegalStateException("FIXME");
            }
            remainingStuff = documentRawAsString.substring(index);
            remainingStuffLength = remainingStuff.length();
        } while (remainingStuffLength > 0 && returning == null);
        deliveredTokens.add(returning);
        return returning;
    }
}
