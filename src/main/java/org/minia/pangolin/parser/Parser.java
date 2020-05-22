package org.minia.pangolin.parser;

import lombok.val;
import lombok.var;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.minia.pangolin.scanner.Scanner;
import org.minia.pangolin.scanner.Token;
import org.minia.pangolin.syntaxtree.Application;
import org.minia.pangolin.syntaxtree.ExecutionRequest;
import org.minia.pangolin.syntaxtree.NamedFunction;
import org.minia.pangolin.syntaxtree.NewLineOperation;
import org.minia.pangolin.syntaxtree.Operation;
import org.minia.pangolin.syntaxtree.Operations;
import org.minia.pangolin.syntaxtree.PrintOperation;
import org.minia.pangolin.Program;

import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.util.Util.forceAssert;
import static org.minia.pangolin.util.Util.forcedAssertion;

public class Parser {

    private enum Reduction {
        NAMED_FUNCTION, APPLICATION, EXECUTION_REQUEST
    }

    /**  {@link Program} to be parsed by this parser. */
    private final Program program;

    /**  Exhaustive constructor. */
    public Parser(final Program program) {
        this.program = program;
    }

    public List<ParseTree> parse() {
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
            val parseTree = new ParseTree(program, ParseTree.Type.EMPTY);
            val parsedTrees = new ArrayList<ParseTree>(1);
            parsedTrees.add(parseTree);
            return parsedTrees;
        }
        var canReduceResults = canReduce(tokens);
        var canReduce = canReduceResults.getLeft();
        var whichReduction = canReduceResults.getRight();
        val parsedTrees = new ArrayList<ParseTree>(16);
        while (canReduce) {
            final Pair<ParseTree, List<Token>> stuff =
                    reduce(tokens, whichReduction);
            val parseTree = stuff.getLeft();
            parsedTrees.add(parseTree);
            tokens = stuff.getRight();
            if (tokens.isEmpty()) {
                return parsedTrees;
            }
            canReduceResults = canReduce(tokens);
            canReduce = canReduceResults.getLeft();
            whichReduction = canReduceResults.getRight();
        }
        throw new IllegalStateException("FIXME");
    }

    public Pair<Boolean, Reduction> canReduce(final List<Token> tokens) {
        if (canReduceFunction(tokens)) {
            return new ImmutablePair<>(true, Reduction.NAMED_FUNCTION);
        }
        if (canReduceApplication(tokens)) {
            return new ImmutablePair<>(true, Reduction.APPLICATION);
        }
        if (canReduceExecutionRequest(tokens)) {
            return new ImmutablePair<>(
                    true, Reduction.EXECUTION_REQUEST);
        }
        return new ImmutablePair<>(false, null);
    }

    public boolean canReduceFunction(final List<Token> tokens) {

        val tokensSize = tokens.size();

        if (tokensSize < 26) {
            return false;
        }

        //   Would expect `FUNCTION`.
        val tokenZero = tokens.get(0);
        if (tokenZero.getType() != Token.Type.FUNCTION) {
            return false;
        }

        //   Would expect `IDENTIFIER`.
        val tokenOne = tokens.get(1);
        if (tokenOne.notAnIdentifier()) {
            return false;
        }

        //   Would expect `IS`.
        val tokenTwo = tokens.get(2);
        if (tokenTwo.getType() != Token.Type.IS) {
            return false;
        }

        //   Would expect `A`.
        val tokenThree = tokens.get(3);
        if (tokenThree.getType() != Token.Type.A) {
            return false;
        }

        //   Would expect `COMMAND`.
        val tokenFour = tokens.get(4);
        if (tokenFour.getType() != Token.Type.COMMAND) {
            return false;
        }

        //   Would expect `LINE`.
        val tokenFive = tokens.get(5);
        if (tokenFive.getType() != Token.Type.LINE) {
            return false;
        }

        //   Would expect `INTERFACE`.
        val tokenSix = tokens.get(6);
        if (tokenSix.getType() != Token.Type.INTERFACE) {
            return false;
        }

        //   Would expect `APPLICATION`.
        val tokenSeven = tokens.get(7);
        if (tokenSeven.getType() != Token.Type.APPLICATION) {
            return false;
        }

        //   Would expect `FUNCTION`.
        val tokenEight = tokens.get(8);
        if (tokenEight.getType() != Token.Type.FUNCTION) {
            return false;
        }

        //   Would expect `AND`.
        val tokenNine = tokens.get(9);
        if (tokenNine.getType() != Token.Type.AND) {
            return false;
        }

        //   Would expect `RECEIVES`.
        val tokenTen = tokens.get(10);
        if (tokenTen.getType() != Token.Type.RECEIVES) {
            return false;
        }

        //   Would expect `NOTHING`.
        val tokenEleven = tokens.get(11);
        if (tokenEleven.getType() != Token.Type.NOTHING) {
            return false;
        }

        //   Would expect `AT`.
        val tokenTwelve = tokens.get(12);
        if (tokenTwelve.getType() != Token.Type.AT) {
            return false;
        }

        //   Would expect `ALL`.
        val tokenThirteen = tokens.get(13);
        if (tokenThirteen.getType() != Token.Type.ALL) {
            return false;
        }

        //   Would expect `AND`.
        val tokenFourteen = tokens.get(14);
        if (tokenFourteen.getType() != Token.Type.AND) {
            return false;
        }

        //   Would expect `RETURNS`.
        val token15 = tokens.get(15);
        if (token15.getType() != Token.Type.RETURNS) {
            return false;
        }

        //   Would expect `NOTHING`.
        val token16 = tokens.get(16);
        if (token16.getType() != Token.Type.NOTHING) {
            return false;
        }

        //   Would expect `AT`.
        val token17 = tokens.get(17);
        if (token17.getType() != Token.Type.AT) {
            return false;
        }

        //   Would expect `ALL`.
        val token18 = tokens.get(18);
        if (token18.getType() != Token.Type.ALL) {
            return false;
        }

        //   Would expect `SO`.
        val token19 = tokens.get(19);
        if (token19.getType() != Token.Type.SO) {
            return false;
        }

        //   Would expect `IT`.
        val token20 = tokens.get(20);
        if (token20.getType() != Token.Type.IT) {
            return false;
        }

        //   Would expect `CAUSES`.
        val token21 = tokens.get(21);
        if (token21.getType() != Token.Type.CAUSES) {
            return false;
        }

        //   Would expect `SIDE`.
        val token22 = tokens.get(22);
        if (token22.getType() != Token.Type.SIDE) {
            return false;
        }

        //   Would expect `EFFECTS`.
        val token23 = tokens.get(23);
        if (token23.getType() != Token.Type.EFFECTS) {
            return false;
        }

        //   Would expect `AND`.
        val token24 = tokens.get(24);
        if (token24.getType() != Token.Type.AND) {
            return false;
        }

        //   Would expect `DOES`.
        val token25 = tokens.get(25);
        if (token25.getType() != Token.Type.DOES) {
            return false;
        }

        val nestedCallTokensSize = tokensSize - 26;
        List<Token> nestedCallTokens = new ArrayList<>(nestedCallTokensSize);
        for (int i = 0; i < nestedCallTokensSize; i++) {
            nestedCallTokens.add(tokens.get(26 + i));
        }

        Pair<Boolean, Short> canReduceFunctionBodyResults =
                canReduceFunctionBody(nestedCallTokens);
        boolean canReduceFunctionBody = canReduceFunctionBodyResults.getLeft();
        if (canReduceFunctionBody)  {
            return true;  /* FIXME: Actually, quite not. */
        }
        return false;
    }

    /**  @param tokens A list of tokens remaining to be processed.
     *   @return A {@link Pair} holding whether can do a function body
     * reduction or not (at {@link Pair#getLeft()}) and, in case that is
     * true, how many tokens would that reduction consume (at {@link
     * Pair#getRight()}). */
    public Pair<Boolean, Short> canReduceFunctionBody(final List<Token> tokens) {

        if (tokens.size() < 6) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }

        //   Would expect `PRINT`.
        val token0 = tokens.get(0);
        if (token0.getType() != Token.Type.PRINT) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }

        //   Would expect `STRING_LITERAL`.
        val token1 = tokens.get(1);
        if (token1.getType() != Token.Type.STRING_LITERAL) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }

        //   Would expect `AND`.
        val token2 = tokens.get(2);
        if (token2.getType() != Token.Type.AND) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }

        //   Would expect `THEN`.
        val token3 = tokens.get(3);
        if (token3.getType() != Token.Type.THEN) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }

        //   Would expect `NEW`.
        val token4 = tokens.get(4);
        if (token4.getType() != Token.Type.NEW) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }

        //   Would expect `LINE`.
        val token5 = tokens.get(5);
        if (token5.getType() != Token.Type.LINE) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }

        return new ImmutablePair<>(true, (short) 6);
    }

    public boolean canReduceApplication(final List<Token> tokens) {

        if (tokens.get(0).getType() == Token.Type.APPLICATION) {
            return true;  /* FIXME Actually quite not true. */
        }
        return false;
    }

    public boolean canReduceExecutionRequest(final List<Token> tokens) {

        if (tokens.get(0).getType() == Token.Type.RUN) {
            return true;  /* FIXME Actually quite not true. */
        }
        return false;
    }

    public Pair<ParseTree, List<Token>> reduce(
            final List<Token> tokens, final Reduction reduction) {

        if (reduction == Reduction.NAMED_FUNCTION) {

            val reduceFunctionResult = reduceFunction(tokens);
            return new ImmutablePair<>(
                    new ParseTree(reduceFunctionResult.getLeft()),
                    reduceFunctionResult.getRight());
        } else if (reduction == Reduction.APPLICATION) {

            val reduceApplicationResult = reduceApplication(tokens);
            return new ImmutablePair<>(
                    new ParseTree(reduceApplicationResult.getLeft()),
                    reduceApplicationResult.getRight());
        } else {
            forcedAssertion(reduction == Reduction.EXECUTION_REQUEST);

            val reduceExecutionRequestResult =
                    reduceExecutionRequest(tokens);
            return new ImmutablePair<>(
                    new ParseTree(
                            reduceExecutionRequestResult.getLeft()),
                    reduceExecutionRequestResult.getRight());
        }
    }

    public Pair<NamedFunction, List<Token>> reduceFunction(
            final List<Token> tokens) {

        val tokensAfterFunctionHeadReduction =
                reduceFunctionHeadBeginning(tokens);
        val expectedFunctionNameToken =
                tokensAfterFunctionHeadReduction.getLeft();
        val expectedFunctionName =
                expectedFunctionNameToken.getIdentifierName();
        val tokensAfterReceivesClauseReduction = reduceFunctionReceivesClause(
                tokensAfterFunctionHeadReduction.getRight());
        val tokensAfterReturnsClauseReduction = reduceFunctionReturnsClause(
                tokensAfterReceivesClauseReduction);
        val tokensAfterSideEffectsClauseReduction =
                reduceFunctionSideEffectsClause(
                        tokensAfterReturnsClauseReduction);
        val tokensAfterAndDoesClauseReduction = reduceFunctionAndDoesClause(
                tokensAfterSideEffectsClauseReduction);
        val reduceFunctionBodyClauseReturnedPair = reduceFunctionBodyClause(
                tokensAfterAndDoesClauseReduction);
        val functionOperations =
                reduceFunctionBodyClauseReturnedPair.getLeft();
        val remainingTokensAfterFunctionBody =
                reduceFunctionBodyClauseReturnedPair.getRight();
        val remainingTokensAfterFunctionReduction = reduceFunctionTail(
                remainingTokensAfterFunctionBody, expectedFunctionName);
        val functionName = expectedFunctionName;
        return new ImmutablePair<>(
                new NamedFunction(functionName, functionOperations),
                remainingTokensAfterFunctionReduction
        );
    }

    /**  @return A {@link Pair} where the left side holds the name of
     * the function, expected to be found later as well, and for that
     * reason is returned; and the right side holds the reduced list of
     * tokens. */
    public Pair<Token, List<Token>> reduceFunctionHeadBeginning(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 9);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.FUNCTION);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.IDENTIFIER);
        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.IS);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.A);
        val token4 = tokens.get(4);
        forcedAssertion(token4.getType() == Token.Type.COMMAND);
        val token5 = tokens.get(5);
        forcedAssertion(token5.getType() == Token.Type.LINE);
        val token6 = tokens.get(6);
        forcedAssertion(token6.getType() == Token.Type.INTERFACE);
        val token7 = tokens.get(7);
        forcedAssertion(token7.getType() == Token.Type.APPLICATION);
        val token8 = tokens.get(8);
        forcedAssertion(token8.getType() == Token.Type.FUNCTION);
        final List<Token> returningRight = new ArrayList<>(tokensSize - 9);
        for (int i = 0; i < tokensSize - 9; i++) {
            returningRight.add(tokens.get(i + 9));
        }
        return new ImmutablePair<>(token1, returningRight);
    }

    public List<Token> reduceFunctionReceivesClause(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 5);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.AND);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.RECEIVES);

        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.NOTHING);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.AT);
        val token4 = tokens.get(4);
        forcedAssertion(token4.getType() == Token.Type.ALL);

        final List<Token> returning = new ArrayList<>(tokensSize - 5);
        for (int i = 0; i < tokensSize - 5; i++) {
            returning.add(tokens.get(i + 5));
        }
        return returning;
    }

    public List<Token> reduceFunctionReturnsClause(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 5);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.AND);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.RETURNS);

        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.NOTHING);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.AT);
        val token4 = tokens.get(4);
        forcedAssertion(token4.getType() == Token.Type.ALL);

        final List<Token> returning = new ArrayList<>(tokensSize - 5);
        for (int i = 0; i < tokensSize - 5; i++) {
            returning.add(tokens.get(i + 5));
        }
        return returning;
    }

    public List<Token> reduceFunctionSideEffectsClause(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 5);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.SO);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.IT);
        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.CAUSES);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.SIDE);
        val token4 = tokens.get(4);
        forcedAssertion(token4.getType() == Token.Type.EFFECTS);
        final List<Token> returning = new ArrayList<>(tokensSize - 5);
        for (int i = 0; i < tokensSize - 5; i++) {
            returning.add(tokens.get(i + 5));
        }
        return returning;
    }

    public List<Token> reduceFunctionAndDoesClause(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 2);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.AND);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.DOES);
        final List<Token> returning = new ArrayList<>(tokensSize - 2);
        for (int i = 0; i < tokensSize - 2; i++) {
            returning.add(tokens.get(i + 2));
        }
        return returning;
    }

    public Pair<Operations, List<Token>> reduceFunctionBodyClause(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 6);

        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.PRINT);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.STRING_LITERAL);
        val operation0 = new PrintOperation(token1);

        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.AND);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.THEN);

        val token4 = tokens.get(4);
        forcedAssertion(token4.getType() == Token.Type.NEW);
        val token5 = tokens.get(5);
        forcedAssertion(token5.getType() == Token.Type.LINE);
        val operation1 = new NewLineOperation();

        val operations = new ArrayList<Operation>(2);
        operations.add(operation0);
        operations.add(operation1);

        val returningLeft = Operations.sequential(operations);

        final List<Token> returningRight = new ArrayList<>(tokensSize - 6);
        for (int i = 0; i < tokensSize - 6; i++) {
            returningRight.add(tokens.get(i + 6));
        }

        return new ImmutablePair<>(returningLeft, returningRight);
    }

    public List<Token> reduceFunctionTail(
            final List<Token> tokens,
            final CharSequence expectedFunctionName) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize >= 3);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.END);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.FUNCTION);
        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.IDENTIFIER);

        forcedAssertion(expectedFunctionName.equals(
                token2.getIdentifierName()));

        final List<Token> returning = new ArrayList<>(tokensSize - 3);
        for (int i = 0; i < tokensSize - 3; i++) {
            returning.add(tokens.get(i + 3));
        }
        return returning;
    }

    public Pair<Application, List<Token>> reduceApplication(
            final List<Token> tokens) {

        val tokensAfterApplicationHeadReduction =
                reduceApplicationHead(tokens);
        val expectedApplicationNameTokenFirstPosition =
                tokensAfterApplicationHeadReduction.getLeft();
        val expectedApplicationNameTokenFirstPositionCharSequence =
                expectedApplicationNameTokenFirstPosition.getIdentifierName();
        val reduceApplicationEntryPointClauseResults =
                reduceApplicationEntryPointClause(
                        tokensAfterApplicationHeadReduction.getRight());
        val entryPointNamedFunctionNameToken =
                reduceApplicationEntryPointClauseResults.getLeft();
        val entryPointNamedFunctionName =
                entryPointNamedFunctionNameToken.getIdentifierName();
        val reduceApplicationTailResults = reduceApplicationTail(
                reduceApplicationEntryPointClauseResults.getRight());
        val remainingTokensAfterApplicationReduction =
                reduceApplicationTailResults.getRight();
        val expectedApplicationNameSecondPosition =
                reduceApplicationEntryPointClauseResults.getLeft();
        val expectedApplicationNameSecondPositionCharSequence =
                expectedApplicationNameSecondPosition.getIdentifierName();
        forcedAssertion(
                expectedApplicationNameTokenFirstPositionCharSequence ==
                        expectedApplicationNameSecondPositionCharSequence);
        val applicationName =
                expectedApplicationNameTokenFirstPositionCharSequence;
        return new ImmutablePair<>(
                new Application(
                        applicationName, entryPointNamedFunctionName),
                remainingTokensAfterApplicationReduction);
    }

    /**  @return A {@link Pair} where the left side holds the name of
     * the application, expected to be found later as well, and for that
     * reason is returned; and the right side holds the reduced list of
     * tokens. */
    public Pair<Token, List<Token>> reduceApplicationHead(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 8);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.APPLICATION);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.IDENTIFIER);
        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.IS);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.A);
        val token4 = tokens.get(4);
        forcedAssertion(token4.getType() == Token.Type.COMMAND);
        val token5 = tokens.get(5);
        forcedAssertion(token5.getType() == Token.Type.LINE);
        val token6 = tokens.get(6);
        forcedAssertion(token6.getType() == Token.Type.INTERFACE);
        val token7 = tokens.get(7);
        forcedAssertion(token7.getType() == Token.Type.APPLICATION);
        final List<Token> returningRight =
                new ArrayList<>(tokensSize - 8);
        for (int i = 0; i < tokensSize - 8; i++) {
            returningRight.add(tokens.get(i + 8));
        }
        return new ImmutablePair<>(token1, returningRight);
    }

    /**  @return A {@link Pair} where the left side holds the name of
     * the entry point named function of the application, and the right
     * side holds the reduced list of tokens. */
    public Pair<Token, List<Token>> reduceApplicationEntryPointClause(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 7);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.AND);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.THE);
        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.ENTRY);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.POINT);
        val token4 = tokens.get(4);
        forcedAssertion(token4.getType() == Token.Type.IS);
        val token5 = tokens.get(5);
        forcedAssertion(token5.getType() == Token.Type.FUNCTION);
        val token6 = tokens.get(6);
        forcedAssertion(token6.getType() == Token.Type.IDENTIFIER);
        final List<Token> returningRight =
                new ArrayList<>(tokensSize - 7);
        for (int i = 0; i < tokensSize - 7; i++) {
            returningRight.add(tokens.get(i + 7));
        }
        return new ImmutablePair<>(token6, returningRight);
    }

    /**  @return A {@link Pair} where the left side holds the name of
     * the application, expected to be the same as that one found
     * earlier; and the right side holds the reduced list of tokens. */
    public Pair<Token, List<Token>> reduceApplicationTail(
            final List<Token> tokens) {

        val expectedTokensCt = 3;
        val tokensSize = tokens.size();
        forcedAssertion(tokensSize >= expectedTokensCt);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.END);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.APPLICATION);
        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.IDENTIFIER);
        final List<Token> returningRight =
                new ArrayList<>(tokensSize - expectedTokensCt);
        for (int i = 0; i < tokensSize - expectedTokensCt; i++) {
            returningRight.add(tokens.get(i + expectedTokensCt));
        }
        return new ImmutablePair<>(token2, returningRight);
    }

    public Pair<ExecutionRequest, List<Token>> reduceExecutionRequest(
            final List<Token> tokens) {

        val expectedTokensCt = 6;
        val tokensSize = tokens.size();
        forcedAssertion(tokensSize >= expectedTokensCt);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.RUN);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.COMMAND);
        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.LINE);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.INTERFACE);
        val token4 = tokens.get(4);
        forcedAssertion(token4.getType() == Token.Type.APPLICATION);
        val token5 = tokens.get(5);
        forcedAssertion(token5.getType() == Token.Type.IDENTIFIER);
        final List<Token> returningRight =
                new ArrayList<>(tokensSize - expectedTokensCt);
        for (int i = 0; i < tokensSize - expectedTokensCt; i++) {
            returningRight.add(tokens.get(i + expectedTokensCt));
        }
        return new ImmutablePair<>(
                new ExecutionRequest(
                        token5.getIdentifierName()), returningRight);
    }
}
