package org.minia.pangolin;

import lombok.val;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.Util.forceAssert;

public class Parser {

    /**  {@link Program} to be parsed by this parser. */
    private final Program program;

    /**  Exhaustive constructor. */
    public Parser(final Program program) {
        this.program = program;
    }

    public ParseTree parse() {
        final ParseTree stuff;
        synchronized (program) {
            stuff = synchronizedParse();
        }
        return stuff;
    }

    /**  This method is meant to be used from a synchronized block on
     * {@link #program}. */
    public ParseTree synchronizedParse() {
        forceAssert(program.getDocuments().size() == 1);
        val scanner = new Scanner(program);
        List<Token> tokens = new ArrayList<>(16);
        while (scanner.moreTokens()) {
            val currentToken = scanner.nextToken();
            if (currentToken != null) {
                tokens.add(currentToken);
            }
        }
        if (tokens.isEmpty()) {
            return new ParseTree(program, ParseTree.Type.EMPTY);
        }
        while (canReduce(tokens)) {
            Pair<ParseTree, List<Token>> stuff = reduce(tokens);
            val parseTree = stuff.getLeft();
            tokens = stuff.getRight();
            if (tokens.isEmpty()) {
                return parseTree;
            }
        }
        throw new IllegalStateException("FIXME");
    }

    public boolean canReduce(final List<Token> tokens) {
        return canReduceFunction(tokens);
    }

    public boolean canReduceFunction(final List<Token> tokens) {
        if (tokens.size() < 5) {
            return false;
        }
        val tokenZero = tokens.get(0);
        val tokenOne = tokens.get(1);
        val tokenTwo = tokens.get(2);
        val tokenThree = tokens.get(3);
        val tokenFour = tokens.get(4);
        if (tokenZero.getType() != Token.Type.FUNCTION) {
            return false;
        }
        if (tokenOne.notAnIdentifier()) {
            return false;
        }
        if (tokenTwo.getType() != Token.Type.END) {
            return false;
        }
        if (tokenThree.getType() != Token.Type.FUNCTION) {
            return false;
        }
        if (tokenFour.notAnIdentifier()) {
            return false;
        }
        return true;
    }

    public Pair<ParseTree, List<Token>> reduce(final List<Token> tokens) {
        if (canReduceFunction(tokens)) {
            return reduceFunction(tokens);
        }
        throw new IllegalStateException("FIXME");
    }

    public Pair<ParseTree, List<Token>> reduceFunction(final List<Token> tokens) {
        val namedFunctionNameToken = tokens.get(1);
        val namedFunctionName = namedFunctionNameToken.getIdentifierName();
        val namedFunction = new NamedFunction(namedFunctionName);
        val returningTokens = new ArrayList<Token>(0);
        return new ImmutablePair<ParseTree, List<Token>>(
                new ParseTree(
                        new Program(new Document("")),
                        ParseTree.Type.NAMED_FUNCTION
                ), returningTokens);
    }
}
