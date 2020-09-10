package org.minia.pangolin.parser;

import lombok.extern.java.Log;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.minia.pangolin.scanner.Scanner;
import org.minia.pangolin.scanner.Token;
import org.minia.pangolin.syntaxtree.Application;
import org.minia.pangolin.syntaxtree.Condition;
import org.minia.pangolin.syntaxtree.ConditionalExpression;
import org.minia.pangolin.syntaxtree.ExecutionRequest;
import org.minia.pangolin.syntaxtree.Expression;
import org.minia.pangolin.syntaxtree.IdentifierExpression;
import org.minia.pangolin.syntaxtree.LessThanCondition;
import org.minia.pangolin.syntaxtree.NamedFunction;
import org.minia.pangolin.syntaxtree.NaturalLiteralExpression;
import org.minia.pangolin.syntaxtree.NewLineOperation;
import org.minia.pangolin.syntaxtree.Operation;
import org.minia.pangolin.syntaxtree.Operations;
import org.minia.pangolin.syntaxtree.Operations.RunTimeInterleave;
import org.minia.pangolin.syntaxtree.PrintOperation;
import org.minia.pangolin.syntaxtree.WhereValueBinding;
import org.minia.pangolin.syntaxtree.WhereValueBindings;
import org.minia.pangolin.Program;

import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.util.Util.forceAssert;
import static org.minia.pangolin.util.Util.forcedAssertion;

@Log public final class Parser {

    private static final String FIXME = "FIXME";

    private enum Reduction {
        NAMED_FUNCTION, APPLICATION, EXECUTION_REQUEST
    }

    /**  {@link Program} to be parsed by this parser. */
    private final Program program;

    /**  Exhaustive constructor. */
    public Parser(final Program program) {
        this.program = program;
    }

    public List<ParseTree> parse()
            throws LanguageNotRecognizedException {

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
        throw new IllegalStateException(FIXME);
    }

    public Pair<Boolean, Reduction> canReduce(final List<Token> tokens)
            throws LanguageNotRecognizedException {

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

    @SuppressWarnings({/*"java:S1126", "java:S1134", */"java:S3776"}) /* Return of boolean expressions should not be wrapped into an "if-then-else" statement. */ /* Track uses of "FIX-ME" tags. */ /* Cognitive Complexity of methods should not be too high. */
    public boolean canReduceFunction(final List<Token> tokens)
            throws LanguageNotRecognizedException {

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
        val expectedIdentifierAtFunctionTail = tokenOne.getIdentifierName();

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

        //   Would expect `EXECUTES`.
        val token25 = tokens.get(25);
        if (token25.getType() != Token.Type.EXECUTES) {
            return false;
        }

        //   Would expect `STATEMENTS`.
        val token26 = tokens.get(26);
        if (token26.getType() != Token.Type.STATEMENTS) {
            return false;
        }

        //   Would expect `SEQUENTIALLY`.
        val token27 = tokens.get(27);
        if (token27.getType() != Token.Type.SEQUENTIALLY) {
            return false;
        }

        //   Would expect `AND`.
        val token28 = tokens.get(28);
        if (token28.getType() != Token.Type.AND) {
            return false;
        }

        //   Would expect `DOES`.
        val token29 = tokens.get(29);
        if (token29.getType() != Token.Type.DOES) {
            return false;
        }

        val nestedCallTokensSize = tokensSize - 30;
        List<Token> nestedCallTokens = new ArrayList<>(nestedCallTokensSize);
        for (int i = 0; i < nestedCallTokensSize; i++) {
            nestedCallTokens.add(tokens.get(30 + i));
        }

        val canReduceFunctionBodyResults =
                canReduceFunctionBody(nestedCallTokens);
        val canReduceFunctionBody = canReduceFunctionBodyResults.getLeft();
        forceAssert(canReduceFunctionBody);
        val discardHowManyTokensToGetToFunctionTail =
                canReduceFunctionBodyResults.getRight();
        val tokensAfterFunctionBody = new ArrayList<Token>(
                nestedCallTokensSize - discardHowManyTokensToGetToFunctionTail);
        val limit = nestedCallTokensSize
                - discardHowManyTokensToGetToFunctionTail;
        for (int i = 0; i < limit; i++) {
            val i0 = i + discardHowManyTokensToGetToFunctionTail;
            val someToken = nestedCallTokens.get(i0);
            tokensAfterFunctionBody.add(someToken);
        }
        return canReduceFunctionTail(
                tokensAfterFunctionBody,
                expectedIdentifierAtFunctionTail.toString());
    }

    /**  @param tokens A list of tokens remaining to be processed.
     *   @return A {@link Pair} holding whether can do a function body
     * reduction or not (at {@link Pair#getLeft()}) and, in case that is
     * true, how many tokens would that reduction consume (at {@link
     * Pair#getRight()}). */
    public Pair<Boolean, Short> canReduceFunctionBody(
            final List<Token> tokens) throws LanguageNotRecognizedException {

        val tokensSize = tokens.size();
        var canReduceOperationResult = canReduceOperation(tokens);
        var canReduceOperation = canReduceOperationResult.getLeft();
        if (!canReduceOperation) {
            return new ImmutablePair<>(false, null);
        }
        var remainingTokens = canReduceOperationResult.getRight();
        var remainingTokensSize = remainingTokens.size();
        var canReduceAnOperationsConnectingClauseResult =
                canReduceAnOperationsConnectingClause(remainingTokens);
        var canReduceAnOperationsConnectingClause =
                canReduceAnOperationsConnectingClauseResult.getLeft();
        val expectedRunTimeInterleave =
                canReduceAnOperationsConnectingClauseResult.getMiddle();
        while (canReduceAnOperationsConnectingClause) {
            remainingTokens =
                    canReduceAnOperationsConnectingClauseResult.getRight();
            canReduceOperationResult = canReduceOperation(remainingTokens);
            canReduceOperation = canReduceOperationResult.getLeft();
            if (!canReduceOperation) {
                throw new LanguageNotRecognizedException(FIXME);
            }
            remainingTokens = canReduceOperationResult.getRight();
            remainingTokensSize = remainingTokens.size();
            canReduceAnOperationsConnectingClauseResult =
                    canReduceAnOperationsConnectingClause(remainingTokens);
            canReduceAnOperationsConnectingClause =
                    canReduceAnOperationsConnectingClauseResult.getLeft();
            if (canReduceAnOperationsConnectingClause) {
                val actualRunTimeInterleave =
                        canReduceAnOperationsConnectingClauseResult.getMiddle();
                forcedAssertion(expectedRunTimeInterleave ==
                        actualRunTimeInterleave);
            }
        }

        //   No further operations? Any `where` value binding clauses?

        val canReduceWhereValueBindingClausesResult =
                canReduceWhereValueBindingClauses(remainingTokens);
        val canReduceWhereValueBindingClauses =
                canReduceWhereValueBindingClausesResult.getLeft();
        val howManyTokensInWhereValueBindingClauses =
                canReduceWhereValueBindingClausesResult.getRight();
        if (canReduceWhereValueBindingClauses) {
            if (howManyTokensInWhereValueBindingClauses > 0) {
                remainingTokens.subList(
                        0, howManyTokensInWhereValueBindingClauses).clear();
            }
            remainingTokensSize = remainingTokens.size();
        }

        //   No [more] `where` value binding clauses? Assume the
        // function tail is coming up next. That is, return true.

        forcedAssertion(tokensSize < Short.MAX_VALUE + 1);
        forcedAssertion(remainingTokensSize < Short.MAX_VALUE + 1);
        forcedAssertion(tokensSize > remainingTokensSize);
        return new ImmutablePair<>(
                true, (short) (tokensSize - remainingTokensSize));
    }

    /**  @return A {@link Pair}; where the right holds: the remaining
     * tokens after the first operation reduction, if an operation
     * reduction can be performed, else `null`; and the left holds the
     * actual result of the operation (whether an operation reduction
     * can be performed or not. */
    public Pair<Boolean, List<Token>> canReduceOperation(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 2) {
            return new ImmutablePair<>(false, null);
        }
        val token0 = tokens.get(0);
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 1);
        for (int i = 1; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }
        if (Token.Type.NEW == token0.getType()) {
            return canReduceNewLineOperation(remainingTokens);
        }
        if (Token.Type.PRINT == token0.getType()) {
            return canReducePrintOperation(remainingTokens);
        }
        return new ImmutablePair<>(false, null);
    }

    public Pair<Boolean, List<Token>> canReduceNewLineOperation(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 1) {
            return new ImmutablePair<>(false, null);
        }
        val token0 = tokens.get(0);
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 1);
        for (int i = 1; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }
        if (Token.Type.LINE == token0.getType()) {
            return new ImmutablePair<>(true, remainingTokens);
        }
        return new ImmutablePair<>(false, null);
    }

    /**  <p>The `print` token has been already seen by the caller of
     * this, this one only has to check that what comes next can be
     * really printed. */
    public Pair<Boolean, List<Token>> canReducePrintOperation(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 1) {
            return new ImmutablePair<>(false, null);
        }
        val token0 = tokens.get(0);
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 1);
        for (int i = 1; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }
        if (Token.Type.STRING_LITERAL == token0.getType()) {
            return new ImmutablePair<>(true, remainingTokens);
        }
        if (Token.Type.IDENTIFIER == token0.getType()) {
            //   Semantic analysis out of scope at this point.
            return new ImmutablePair<>(true, remainingTokens);
        }
        return new ImmutablePair<>(false, null);
    }

    public Pair<Boolean, Short> canReduceWhereValueBindingClauses(
            final List<Token> tokens) {

        val canReduceWhereValueBindingClauseResult =
                canReduceWhereValueBindingClause(tokens);
        val canReduceWhereValueBindingClause =
                canReduceWhereValueBindingClauseResult.getLeft();
        if (!canReduceWhereValueBindingClause) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        } else {
            val howManyTokensInWhereValueBindingClause =
                    canReduceWhereValueBindingClauseResult.getRight();
            val tokensSize = tokens.size();
            val remainingTokensTargetSize =
                    tokensSize - howManyTokensInWhereValueBindingClause;
            val remainingTokens =
                    new ArrayList<Token>(remainingTokensTargetSize);
            for (int i = 0; i < remainingTokensTargetSize; i++) {
                val someToken =
                        tokens.get(i + howManyTokensInWhereValueBindingClause);
                remainingTokens.add(someToken);
            }
            val canReduceWhereValueBindingClausesResult =
                    canReduceWhereValueBindingClauses(remainingTokens);
            val canReduceWhereValueBindingClauses =
                    canReduceWhereValueBindingClausesResult.getLeft();
            if (canReduceWhereValueBindingClauses) {
                val howMany = canReduceWhereValueBindingClauseResult.getRight();
                return new ImmutablePair<>(
                        true,
                        (short) (howMany + howManyTokensInWhereValueBindingClause));
            } else {
                return new ImmutablePair<>(
                        true, howManyTokensInWhereValueBindingClause);
            }
        }
    }

    public Pair<Boolean, Short> canReduceWhereValueBindingClause(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 0);
        val token0 = tokens.get(0);
        if (Token.Type.WHERE != token0.getType()) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }
        // where identifier foo identifier ends is bound to [whatever]
        // 1     2                              3  4     5  6 7 8 ...
        forcedAssertion(tokensSize > 5);
        val token1 = tokens.get(1);
        forcedAssertion(Token.Type.IDENTIFIER == token1.getType());
        val token2 = tokens.get(2);
        forcedAssertion(Token.Type.IS == token2.getType());
        val token3 = tokens.get(3);
        forcedAssertion(Token.Type.BOUND == token3.getType());
        val token4 = tokens.get(4);
        forcedAssertion(Token.Type.TO == token4.getType());
        val tokensToAdvanceNow = 5;
        val remainingTokensTargetSize = tokensSize - tokensToAdvanceNow;
        val remainingTokens = new ArrayList<Token>(remainingTokensTargetSize);
        for (int i = 0; i < remainingTokensTargetSize; i++) {
            val someToken = tokens.get(i + tokensToAdvanceNow);
            remainingTokens.add(someToken);
        }
        val canReduceExpressionResult = canReduceExpression(remainingTokens);
        val canReduceExpression = canReduceExpressionResult.getLeft();
        if (canReduceExpression) {
            val howManyTokens = canReduceExpressionResult.getRight();
            forcedAssertion(remainingTokensTargetSize < Short.MAX_VALUE / 2);
            return new ImmutablePair<>(true, (short) (howManyTokens + 5));
        }
        return new ImmutablePair<>(false, Short.MIN_VALUE);
    }

    public Pair<Boolean, Short> canReduceExpression(final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 1) {
            return new ImmutablePair<>(false, Short.MIN_VALUE);
        }
        val token0 = tokens.get(0);
        if (Token.Type.NATURAL_LITERAL == token0.getType()) {  /* XXX replace for expression detection */
            forcedAssertion(Token.Type.NATURAL_LITERAL == token0.getType());  /* XXX replace for expression detection */
        } else if (Token.Type.IDENTIFIER == token0.getType()) {  /* XXX replace for expression detection */
            forcedAssertion(Token.Type.IDENTIFIER == token0.getType());  /* XXX replace for expression detection */
        } else {  /* XXX replace for expression detection */
            forcedAssertion(Token.Type.CONDITIONAL == token0.getType());  /* XXX replace for expression detection */
            val canReduceConditionalExpressionResult =  /* XXX replace for expression detection */
                    canReduceConditionalExpression(tokens);  /* XXX replace for expression detection */
            if (canReduceConditionalExpressionResult.getLeft()) {  /* XXX replace for expression detection */
                return canReduceConditionalExpressionResult;  /* XXX replace for expression detection */
            }  /* XXX replace for expression detection */
        }  /* XXX replace for expression detection */
        return new ImmutablePair<>(true, (short) 1);
    }

    public Pair<Boolean, Short> canReduceConditionalExpression(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 13) {
            return new ImmutablePair<>(false, (short) 0);
        }

        val token0 = tokens.get(0);
        if (Token.Type.CONDITIONAL != token0.getType()) {
            return new ImmutablePair<>(false, (short) 0);
        }

        val token1 = tokens.get(1);
        forceAssert(Token.Type.IF == token1.getType());

        val token2 = tokens.get(2);
        forceAssert(Token.Type.NATURAL_LITERAL == token2.getType());

        val token3 = tokens.get(3);
        forceAssert(Token.Type.IS == token3.getType());

        val token4 = tokens.get(4);
        forceAssert(Token.Type.LESS == token4.getType());

        val token5 = tokens.get(5);
        forceAssert(Token.Type.THAN == token5.getType());

        val token6 = tokens.get(6);
        forceAssert(Token.Type.NATURAL_LITERAL == token6.getType());

        val token7 = tokens.get(7);
        forceAssert(Token.Type.THEN == token7.getType());

        val token8 = tokens.get(8);
        forceAssert(Token.Type.NATURAL_LITERAL == token8.getType());

        val token9 = tokens.get(9);
        forceAssert(Token.Type.ELSE == token9.getType());

        val token10 = tokens.get(10);
        forceAssert(Token.Type.NATURAL_LITERAL == token10.getType());

        val token11 = tokens.get(11);
        forceAssert(Token.Type.CONDITIONAL == token11.getType());

        val token12 = tokens.get(12);
        forceAssert(Token.Type.ENDS == token12.getType());

        return new ImmutablePair<>(true, (short) 13);
    }

    public boolean canReduceFunctionTail(
            final List<Token> tokens, final String expectedFunctionName) {

        forcedAssertion(expectedFunctionName != null);
        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 1);
        val token0 = tokens.get(0);
        forcedAssertion(Token.Type.END == token0.getType());
        val token1 = tokens.get(1);
        forcedAssertion(Token.Type.FUNCTION == token1.getType());
        val token2 = tokens.get(2);
        forcedAssertion(Token.Type.IDENTIFIER == token2.getType());
        val actualFunctionName = token2.getIdentifierName();
        forcedAssertion(expectedFunctionName.equals(actualFunctionName));
        return true;
    }

    //@SuppressWarnings({"java:S1126", "java:S1134"}) /* Return of boolean expressions should not be wrapped into an "if-then-else" statement. */ /* Track uses of "FIX-ME" tags (without the dash). */
    public boolean canReduceApplication(final List<Token> tokens) {

        val tokensSize = tokens.size();
        // application identifier hello, world identifier ends
        // 1           2
        // is a command line interface application
        // 3  4 5       6    7         8
        // and the entry point is function identifier main identifier ends
        // 9   10  11    12    13 14       15
        // end application identifier hello, world identifier ends
        // 16  17          18
        if (tokensSize < 18) {
            return false;
        }

        val token0 = tokens.get(0);
        forcedAssertion(Token.Type.APPLICATION == token0.getType());
        val token1 = tokens.get(1);
        forcedAssertion(Token.Type.IDENTIFIER == token1.getType());
        val firstApplicationName = token1.getIdentifierName();
        val token2 = tokens.get(2);
        forcedAssertion(Token.Type.IS == token2.getType());
        val token3 = tokens.get(3);
        forcedAssertion(Token.Type.A == token3.getType());
        val token4 = tokens.get(4);
        forcedAssertion(Token.Type.COMMAND == token4.getType());
        val token5 = tokens.get(5);
        forcedAssertion(Token.Type.LINE == token5.getType());
        val token6 = tokens.get(6);
        forcedAssertion(Token.Type.INTERFACE == token6.getType());
        val token7 = tokens.get(7);
        forcedAssertion(Token.Type.APPLICATION == token7.getType());
        val token8 = tokens.get(8);
        forcedAssertion(Token.Type.AND == token8.getType());
        val token9 = tokens.get(9);
        forcedAssertion(Token.Type.THE == token9.getType());
        val token10 = tokens.get(10);
        forcedAssertion(Token.Type.ENTRY == token10.getType());
        val token11 = tokens.get(11);
        forcedAssertion(Token.Type.POINT == token11.getType());
        val token12 = tokens.get(12);
        forcedAssertion(Token.Type.IS == token12.getType());
        val token13 = tokens.get(13);
        forcedAssertion(Token.Type.FUNCTION == token13.getType());
        val token14 = tokens.get(14);
        forcedAssertion(Token.Type.IDENTIFIER == token14.getType());
        val entryPointFunctionName = token14.getIdentifierName();
        forceAssert(entryPointFunctionName != null);
        val token15 = tokens.get(15);
        forcedAssertion(Token.Type.END == token15.getType());
        val token16 = tokens.get(16);
        forcedAssertion(Token.Type.APPLICATION == token16.getType());
        val token17 = tokens.get(17);
        forcedAssertion(Token.Type.IDENTIFIER == token17.getType());
        val secondApplicationName = token17.getIdentifierName();
        forcedAssertion(firstApplicationName.equals(secondApplicationName));
        return true;
    }

    /**  <p>Expects either `and OPERATION [...]` or `and then
     * OPERATION` */
    public Triple<Boolean, Operations.RunTimeInterleave, List<Token>>
    canReduceAnOperationsConnectingClause(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 0);
        val token0 = tokens.get(0);
        if (Token.Type.AND != token0.getType()) {
            return new ImmutableTriple<>(false, null, null);
        }
        forcedAssertion(tokensSize > 1);
        val token1 = tokens.get(1);
        final List<Token> remainingTokens;
        if (Token.Type.THEN == token1.getType()) {
            remainingTokens = new ArrayList<>(tokensSize - 2);
            for (int i = 0; i < tokensSize - 2; i++) {
                val someToken = tokens.get(i + 2);
                remainingTokens.add(someToken);
            }
            return new ImmutableTriple<>(
                    true, Operations.RunTimeInterleave.SEQUENTIAL,
                    remainingTokens);
        } else {
            remainingTokens = new ArrayList<>(tokensSize - 1);
            for (int i = 0; i < tokensSize - 1; i++) {
                val someToken = tokens.get(i + 1);
                remainingTokens.add(someToken);
            }
            return new ImmutableTriple<>(
                    true, Operations.RunTimeInterleave.PARALLEL,
                    remainingTokens);
        }
    }

    //@SuppressWarnings({"java:S1126", "java:S1134"}) /* Return of boolean expressions should not be wrapped into an "if-then-else" statement. */ /* Track uses of "FIX-ME" tags (without the dash). */
    public boolean canReduceExecutionRequest(final List<Token> tokens) {

        val tokensSize = tokens.size();
        // run command line interface application
        // 1   2       3    4         5
        // identifier hello, world identifier ends
        // 6
        if (tokensSize < 6) {
            return false;
        }

        val token0 = tokens.get(0);
        forcedAssertion(Token.Type.RUN == token0.getType());
        val token1 = tokens.get(1);
        forcedAssertion(Token.Type.COMMAND == token1.getType());
        val token2 = tokens.get(2);
        forcedAssertion(Token.Type.LINE == token2.getType());
        val token3 = tokens.get(3);
        forcedAssertion(Token.Type.INTERFACE == token3.getType());
        val token4 = tokens.get(4);
        forcedAssertion(Token.Type.APPLICATION == token4.getType());
        val token5 = tokens.get(5);
        forcedAssertion(Token.Type.IDENTIFIER == token5.getType());
        return true;
    }

    public Pair<ParseTree, List<Token>> reduce(
            final List<Token> tokens, final Reduction reduction)
                    throws LanguageNotRecognizedException {

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
            final List<Token> tokens) throws LanguageNotRecognizedException {

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
        val tokensAfterInterleavingModeClause = reduceInterleavingModeClause(
                tokensAfterSideEffectsClauseReduction);
        val tokensAfterAndDoesClauseReduction = reduceFunctionAndDoesClause(
                tokensAfterInterleavingModeClause);
        val reduceFunctionBodyClauseReturnedPair = reduceFunctionBodyClause(
                tokensAfterAndDoesClauseReduction);
        val functionOperations =
                reduceFunctionBodyClauseReturnedPair.getLeft();
        val functionWhereClauses =
                reduceFunctionBodyClauseReturnedPair.getMiddle();
        val remainingTokensAfterFunctionBody =
                reduceFunctionBodyClauseReturnedPair.getRight();
        val remainingTokensAfterFunctionReduction = reduceFunctionTail(
                remainingTokensAfterFunctionBody, expectedFunctionName);
        val functionName = expectedFunctionName;
        return new ImmutablePair<>(
                new NamedFunction(
                        functionName, functionOperations, functionWhereClauses),
                remainingTokensAfterFunctionReduction);
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

    public List<Token> reduceInterleavingModeClause(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 4);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.AND);
        val token1 = tokens.get(1);
        forcedAssertion(token1.getType() == Token.Type.EXECUTES);
        val token2 = tokens.get(2);
        forcedAssertion(token2.getType() == Token.Type.STATEMENTS);
        val token3 = tokens.get(3);
        forcedAssertion(token3.getType() == Token.Type.SEQUENTIALLY);
        final List<Token> returning = new ArrayList<>(tokensSize - 4);
        for (int i = 0; i < tokensSize - 4; i++) {
            returning.add(tokens.get(i + 4));
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

    public
    Triple<Operations, WhereValueBindings, List<Token>>
    reduceFunctionBodyClause(final List<Token> tokens)
            throws LanguageNotRecognizedException {

        val reduceOperationsResult = reduceOperations(tokens);
        val operationsReduced = reduceOperationsResult.getLeft();
        val remainingTokensAfterOperationsReduction =
                reduceOperationsResult.getRight();
        val tryReduceWhereValueBindingsResult = tryReduceWhereValueBindings(
                remainingTokensAfterOperationsReduction);
        val reducedWhereValueBindings =
                tryReduceWhereValueBindingsResult.getLeft();
        final WhereValueBindings whereValueBindings;
        final List<Token> remainingTokensAfterFunctionBody;
        if (reducedWhereValueBindings) {
            whereValueBindings = tryReduceWhereValueBindingsResult.getMiddle();
            remainingTokensAfterFunctionBody =
                    tryReduceWhereValueBindingsResult.getRight();
        } else {
            whereValueBindings = null;
            remainingTokensAfterFunctionBody =
                    remainingTokensAfterOperationsReduction;
        }
        return new ImmutableTriple<>(
                operationsReduced, whereValueBindings,
                remainingTokensAfterFunctionBody);
    }

    public Pair<Operations, List<Token>> reduceOperations(
            final List<Token> tokens) throws LanguageNotRecognizedException {

        val operations = new ArrayList<Operation>(4 /* Just a random guess. */);
        var operationReductionResult = reduceOperation(tokens);
        var operation = operationReductionResult.getLeft();
        var remainingTokens = operationReductionResult.getRight();
        Triple<Boolean, List<Token>, RunTimeInterleave>
        tryReduceOperationsConnectiveResult;
        try {
            tryReduceOperationsConnectiveResult = tryReduceOperationsConnective(
                    remainingTokens, RunTimeInterleave.UNKNOWN);
        } catch (final LanguageNotRecognizedException lnre) {
            /* XXX never entered here! this block can only be entered when not passing an unknown run time interleave. */
            log.severe(lnre.toString());
            throw lnre;
        }
        var connectiveFoundAndReduced =
                tryReduceOperationsConnectiveResult.getLeft();
        final RunTimeInterleave runTimeInterleave;
        if (connectiveFoundAndReduced) {
            operations.add(operation);
            runTimeInterleave =
                    tryReduceOperationsConnectiveResult.getRight();
            remainingTokens = tryReduceOperationsConnectiveResult.getMiddle();
        } else {
            return new ImmutablePair<>(
                    Operations.single(operation), remainingTokens);
        }

        do {
            operationReductionResult = reduceOperation(remainingTokens);
            operation = operationReductionResult.getLeft();
            operations.add(operation);
            remainingTokens = operationReductionResult.getRight();
            try {
                tryReduceOperationsConnectiveResult =
                        tryReduceOperationsConnective(
                                remainingTokens, runTimeInterleave);
            } catch (final LanguageNotRecognizedException lnre) {
                log.severe(lnre.toString());
                throw lnre;
            }
            connectiveFoundAndReduced =
                    tryReduceOperationsConnectiveResult.getLeft();
            if (connectiveFoundAndReduced) {
                remainingTokens = tryReduceOperationsConnectiveResult.getMiddle();
            } else {
                if (runTimeInterleave == RunTimeInterleave.PARALLEL) {
                    return new ImmutablePair<>(
                            Operations.parallel(operations), remainingTokens);
                } else {
                    forcedAssertion(
                            runTimeInterleave == RunTimeInterleave.SEQUENTIAL);
                    return new ImmutablePair<>(
                            Operations.sequential(operations), remainingTokens);
                }
            }
        } while (!remainingTokens.isEmpty());

        throw new IllegalStateException("illegal state reached?");
    }

    public Pair<Operation, List<Token>> reduceOperation(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 0);
        val token0 = tokens.get(0);
        if (Token.Type.PRINT == token0.getType()) {
            return reducePrintOperation(tokens);
        } else {
            forcedAssertion(Token.Type.NEW == token0.getType());
            return reduceNewLineOperation(tokens);
        }
    }

    public Pair<Operation, List<Token>> reducePrintOperation(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 1);
        val token0 = tokens.get(0);
        forcedAssertion(Token.Type.PRINT == token0.getType());
        val token1 = tokens.get(1);

        if (Token.Type.IDENTIFIER == token1.getType()) {
            forcedAssertion(Token.Type.IDENTIFIER == token1.getType());
        } else {
            forcedAssertion(Token.Type.STRING_LITERAL == token1.getType());
        }

        final Operation operation = new PrintOperation(token1);
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 2);
        for (int i = 0; i < tokensSize - 2; i++) {
            val someToken = tokens.get(i + 2);
            remainingTokens.add(someToken);
        }
        return new ImmutablePair<>(operation, remainingTokens);
    }

    public Pair<Operation, List<Token>> reduceNewLineOperation(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 1);
        val token0 = tokens.get(0);
        forcedAssertion(Token.Type.NEW == token0.getType());
        val token1 = tokens.get(1);
        forcedAssertion(Token.Type.LINE == token1.getType());

        final Operation operation = new NewLineOperation();
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 2);
        for (int i = 0; i < tokensSize - 2; i++) {
            val someToken = tokens.get(i + 2);
            remainingTokens.add(someToken);
        }
        return new ImmutablePair<>(operation, remainingTokens);
    }

    public
    Triple<Boolean, List<Token>, RunTimeInterleave>
    tryReduceOperationsConnective(
            final List<Token> tokens,
            final RunTimeInterleave expectedRunTimeInterleave)
                    throws LanguageNotRecognizedException {

        final boolean anyInterleavingWillDo =
                RunTimeInterleave.UNKNOWN == expectedRunTimeInterleave;

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 1);
        val token0 = tokens.get(0);
        if (Token.Type.AND != token0.getType()) {
            return new ImmutableTriple<>(
                    false, tokens, expectedRunTimeInterleave);
        }
        forcedAssertion(Token.Type.AND == token0.getType());
        if (anyInterleavingWillDo) {
            return tryReduceOperationsConnectiveAnyInterleavingWillDo(tokens);
        }

        val token1 = tokens.get(1);
        if (Token.Type.THEN == token1.getType()) {
            return tryReduceOperationsSequentialConnective(
                    tokens, expectedRunTimeInterleave);
        } else {
            if (RunTimeInterleave.PARALLEL == expectedRunTimeInterleave) {
                final List<Token> remainingTokens =
                        new ArrayList<>(tokensSize - 1);
                for (int i = 0; i < tokensSize - 1; i++) {
                    val someToken = tokens.get(i + 1);
                    remainingTokens.add(someToken);
                }
                return new ImmutableTriple<>(true, remainingTokens, null);
            } else {
                throw new LanguageNotRecognizedException(FIXME);
            }
        }
    }

    public
    Triple<Boolean, List<Token>, RunTimeInterleave>
    tryReduceOperationsConnectiveAnyInterleavingWillDo(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 1);
        val token1 = tokens.get(1);
        if (Token.Type.THEN == token1.getType()) {
            final List<Token> remainingTokens =
                    new ArrayList<>(tokensSize - 2);
            for (int i = 0; i < tokensSize - 2; i++) {
                val someToken = tokens.get(i + 2);
                remainingTokens.add(someToken);
            }
            return new ImmutableTriple<>(
                    true, remainingTokens, RunTimeInterleave.SEQUENTIAL);
        } else {
            final List<Token> remainingTokens =
                    new ArrayList<>(tokensSize - 1);
            for (int i = 0; i < tokensSize - 1; i++) {
                val someToken = tokens.get(i + 1);
                remainingTokens.add(someToken);
            }
            return new ImmutableTriple<>(
                    true, remainingTokens, RunTimeInterleave.PARALLEL);
        }
    }

    public
    Triple<Boolean, List<Token>, RunTimeInterleave>
    tryReduceOperationsSequentialConnective(
            final List<Token> tokens,
            final RunTimeInterleave expectedRunTimeInterleave)
                    throws LanguageNotRecognizedException {

        val tokensSize = tokens.size();
        if (RunTimeInterleave.SEQUENTIAL == expectedRunTimeInterleave) {
            final List<Token> remainingTokens =
                    new ArrayList<>(tokensSize - 2);
            for (int i = 0; i < tokensSize - 2; i++) {
                val someToken = tokens.get(i + 2);
                remainingTokens.add(someToken);
            }
            return new ImmutableTriple<>(true, remainingTokens, null);
        } else {
            throw new LanguageNotRecognizedException(FIXME);
        }
    }

    public
    Triple<Boolean, WhereValueBindings, List<Token>>
    tryReduceWhereValueBindings(final List<Token> tokens) {

        boolean whereValueBindingFound;
        var remainingTokens = tokens;
        val whereValueBindings =
                new ArrayList<WhereValueBinding>(4 /* Maybe more than 4... */);
        WhereValueBinding whereValueBinding;
        do {
            Triple<Boolean, WhereValueBinding, List<Token>>
            tryReduceWhereValueBindingResult =
                    tryReduceWhereValueBinding(remainingTokens);
            whereValueBindingFound = tryReduceWhereValueBindingResult.getLeft();
            whereValueBinding = tryReduceWhereValueBindingResult.getMiddle();
            whereValueBindings.add(whereValueBinding);
            remainingTokens = tryReduceWhereValueBindingResult.getRight();
        } while (whereValueBindingFound);
        return new ImmutableTriple<>(
                true, new WhereValueBindings(whereValueBindings),
                remainingTokens);
    }

    public
    Triple<Boolean, WhereValueBinding, List<Token>>
    tryReduceWhereValueBinding(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 1);
        val token0 = tokens.get(0);
        if (Token.Type.WHERE != token0.getType()) {
            return new ImmutableTriple<>(false, null, tokens);
        }
        val token1 = tokens.get(1);
        forcedAssertion(Token.Type.IDENTIFIER == token1.getType());
        val token2 = tokens.get(2);
        forcedAssertion(Token.Type.IS == token2.getType());
        val token3 = tokens.get(3);
        forcedAssertion(Token.Type.BOUND == token3.getType());
        val token4 = tokens.get(4);
        forcedAssertion(Token.Type.TO == token4.getType());
        val tokensForExpression = new ArrayList<Token>(tokensSize - 5);
        for (int i = 0; i < tokensSize - 5; i++) {
            val someToken = tokens.get(i + 5);
            tokensForExpression.add(someToken);
        }
        val tryReduceExpressionResult =
                tryReduceExpression(tokensForExpression);
        forceAssert(tryReduceExpressionResult.getLeft());
        return new ImmutableTriple<>(
                true,
                new WhereValueBinding(
                        token1, tryReduceExpressionResult.getMiddle()),
                tryReduceExpressionResult.getRight());
    }

    @SuppressWarnings({"java:S1452"})  // Remove generic wildcard type.
    public
    Triple<Boolean, ? extends Expression, ? extends List<Token>>
    tryReduceExpression(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forceAssert(tokensSize > 0);
        val token0 = tokens.get(0);
        if (Token.Type.NATURAL_LITERAL == token0.getType()) {
            val remainingTokens = new ArrayList<Token>(tokensSize - 1);
            for (int i = 0; i < tokensSize - 1; i++) {
                val someToken = tokens.get(i + 1);
                remainingTokens.add(someToken);
            }
            return new ImmutableTriple<>(
                    true,
                    NaturalLiteralExpression.fromNaturalLiteralToken(token0),
                    remainingTokens);
        }
        if (Token.Type.IDENTIFIER == token0.getType()) {
            val remainingTokens = new ArrayList<Token>(tokensSize - 1);
            for (int i = 0; i < tokensSize - 1; i++) {
                val someToken = tokens.get(i + 1);
                remainingTokens.add(someToken);
            }
            return new ImmutableTriple<>(
                    true, IdentifierExpression.fromIdentifierToken(token0),
                    remainingTokens);
        }
        forceAssert(Token.Type.CONDITIONAL == token0.getType());
        forceAssert(tokensSize > 8);
        return tryReduceConditionalExpression(tokens);
    }

    @SuppressWarnings({"java:S1452"})  // Remove generic wildcard type.
    public
    Triple<Boolean, ConditionalExpression, ? extends List<Token>>
    tryReduceConditionalExpression(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forceAssert(tokensSize > 12);  // XXX
        val token0 = tokens.get(0);
        forceAssert(Token.Type.CONDITIONAL == token0.getType());
        val token1 = tokens.get(1);
        forceAssert(Token.Type.IF == token1.getType());
        val tokensForCondition = new ArrayList<Token>(tokensSize - 2);
        for (int i = 0; i < tokensSize - 2; i++) {
            val someToken = tokens.get(i + 2);
            tokensForCondition.add(someToken);
        }
        val tryReduceConditionResult = tryReduceCondition(tokensForCondition);
        forceAssert(tryReduceConditionResult.getLeft());
        val condition = tryReduceConditionResult.getMiddle();
        val tokensAfterCondition = tryReduceConditionResult.getRight();
        val tokensAfterConditionSize = tokensAfterCondition.size();
        forceAssert(tokensAfterConditionSize > 1);
        val firstTokenAfterCondition = tokensAfterCondition.get(0);
        forceAssert(Token.Type.THEN == firstTokenAfterCondition.getType());
        val tokensForExpressionThen =
                new ArrayList<Token>(tokensAfterConditionSize - 1);
        for (int i = 0; i < tokensAfterConditionSize - 1; i++) {
            val someToken = tokensAfterCondition.get(i + 1);
            tokensForExpressionThen.add(someToken);
        }
        val tryReduceExpressionThenResult =
                tryReduceExpression(tokensForExpressionThen);
        forceAssert(tryReduceExpressionThenResult.getLeft());
        val expressionThen = tryReduceExpressionThenResult.getMiddle();
        val tokensAfterExpressionThen =
                tryReduceExpressionThenResult.getRight();
        val tokensAfterExpressionThenSize = tokensAfterExpressionThen.size();
        forceAssert(tokensAfterExpressionThenSize > 3);
        val firstTokenAfterExpressionThen = tokensAfterExpressionThen.get(0);
        forceAssert(Token.Type.ELSE == firstTokenAfterExpressionThen.getType());
        val tokensForExpressionElse =
                new ArrayList<Token>(tokensAfterExpressionThenSize - 1);
        for (int i = 0; i < tokensAfterExpressionThenSize - 1; i++) {
            val someToken = tokensAfterExpressionThen.get(i + 1);
            tokensForExpressionElse.add(someToken);
        }
        val tryReduceExpressionElseResult =
                tryReduceExpression(tokensForExpressionElse);
        forceAssert(tryReduceExpressionElseResult.getLeft());
        val expressionElse = tryReduceExpressionElseResult.getMiddle();
        val tokensAfterExpressionElse =
                tryReduceExpressionElseResult.getRight();
        val tokensAfterExpressionElseSize = tokensAfterExpressionElse.size();
        forceAssert(tokensAfterExpressionElseSize > 1);
        val firstTokenAfterExpressionElse = tokensAfterExpressionElse.get(0);
        forceAssert(Token.Type.CONDITIONAL ==
                firstTokenAfterExpressionElse.getType());
        val secondTokenAfterExpressionElse = tokensAfterExpressionElse.get(1);
        forceAssert(Token.Type.ENDS ==
                secondTokenAfterExpressionElse.getType());
        val remainingTokens =
                new ArrayList<Token>(tokensAfterExpressionElseSize - 2);
        for (int i = 0; i < tokensAfterExpressionElseSize - 2; i++) {
            val someToken = tokensAfterExpressionElse.get(i + 2);
            remainingTokens.add(someToken);
        }
        return new ImmutableTriple<>(
                true, new ConditionalExpression(
                        condition, expressionThen, expressionElse),
                remainingTokens);
    }

    @SuppressWarnings({"java:S1452"})  // Remove generic wildcard type.
    public
    Triple<Boolean, ? extends Condition, ? extends List<Token>>
    tryReduceCondition(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forceAssert(tokensSize > 4);
        val token0 = tokens.get(0);
        forceAssert(Token.Type.NATURAL_LITERAL == token0.getType());  // XXX
        val token1 = tokens.get(1);
        forceAssert(Token.Type.IS == token1.getType());  // XXX
        val token2 = tokens.get(2);
        forceAssert(Token.Type.LESS == token2.getType());  // XXX
        val token3 = tokens.get(3);
        forceAssert(Token.Type.THAN == token3.getType());  // XXX
        val token4 = tokens.get(4);
        forceAssert(Token.Type.NATURAL_LITERAL == token4.getType());  // XXX
        val remainingTokens = new ArrayList<Token>(tokensSize - 5);
        for (int i = 0; i < tokensSize - 5; i++) {
            val someToken = tokens.get(i + 5);
            remainingTokens.add(someToken);
        }
        return new ImmutableTriple<>(
                true, new LessThanCondition(token0, token4), remainingTokens);
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
                reduceApplicationTailResults.getLeft();
        val expectedApplicationNameSecondPositionCharSequence =
                expectedApplicationNameSecondPosition.getIdentifierName();
        forcedAssertion(
                expectedApplicationNameTokenFirstPositionCharSequence.equals(
                        expectedApplicationNameSecondPositionCharSequence));
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
