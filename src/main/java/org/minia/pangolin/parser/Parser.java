package org.minia.pangolin.parser;

import lombok.extern.java.Log;
import lombok.val;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.javatuples.Quartet;
import org.minia.pangolin.scanner.Scanner;
import org.minia.pangolin.scanner.Token;
import org.minia.pangolin.syntaxtree.Application;
import org.minia.pangolin.syntaxtree.Condition;
import org.minia.pangolin.syntaxtree.ConditionalExpression;
import org.minia.pangolin.syntaxtree.EmptyExpressionType;
import org.minia.pangolin.syntaxtree.ExecutionRequest;
import org.minia.pangolin.syntaxtree.Expression;
import org.minia.pangolin.syntaxtree.ExpressionType;
import org.minia.pangolin.syntaxtree.ExpressionTypeFactory;
import org.minia.pangolin.syntaxtree.ExecuteStatement;
import org.minia.pangolin.syntaxtree.NamedFunctionCallExpression;
import org.minia.pangolin.syntaxtree.IdentifierExpression;
import org.minia.pangolin.syntaxtree.LessThanCondition;
import org.minia.pangolin.syntaxtree.NamedFunction;
import org.minia.pangolin.syntaxtree.NamedFunctionBody;
import org.minia.pangolin.syntaxtree.NaturalLiteralExpression;
import org.minia.pangolin.syntaxtree.NewLineStatement;
import org.minia.pangolin.syntaxtree.PrintStatement;
import org.minia.pangolin.syntaxtree.Statement;
import org.minia.pangolin.syntaxtree.Statements;
import org.minia.pangolin.syntaxtree.Statements.RunTimeInterleave;
import org.minia.pangolin.syntaxtree.TopLevelNode;
import org.minia.pangolin.syntaxtree.WhereValueBinding;
import org.minia.pangolin.syntaxtree.WhereValueBindings;
import org.minia.pangolin.syntaxtree.WhereValueBindingsFactory;
import org.minia.pangolin.Program;

import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.util.Util.forceAssert;
import static org.minia.pangolin.util.Util.forcedAssertion;

@Log public final class Parser {

    private static final String FIXME = "FIXME";

    private enum Reduction {
        NAMED_FUNCTION,
        APPLICATION,
        EXECUTION_REQUEST,

        /**  No reduction can be performed. */
        NONE
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
        Quartet<
                Boolean, Parser.Reduction, ? extends TopLevelNode, List<Token>
        > tryReduceResults = tryReduce(tokens);
        boolean canReduce = tryReduceResults.getValue0();
        List<Token> tokensAfterReduction = null;
        if (canReduce) {
            tokensAfterReduction = tryReduceResults.getValue3();
        }
        val parsedTrees = new ArrayList<ParseTree>(16);
        while (canReduce) {
            val topLevelNode = tryReduceResults.getValue2();
            tokensAfterReduction = tryReduceResults.getValue3();
            val parseTree = new ParseTree(topLevelNode);
            parsedTrees.add(parseTree);
            if (tokensAfterReduction.isEmpty()) {
                return parsedTrees;
            }
            tryReduceResults = tryReduce(tokensAfterReduction);
            canReduce = tryReduceResults.getValue0();
        }
        throw new LanguageNotRecognizedException(concat3(
                "No reduction could be made, and yet these tokens remained ",
                "to be parsed: ", tokensAfterReduction));
    }

    String concat2(final Object o0, final Object o1) {
        forceAssert(String.class.equals(o0.getClass()));
        if (o1 == null) {
            return o0.toString();
        }
        if (String.class.equals(o1.getClass())) {
            val s0 = (String) o0;
            return s0 + o1;
        } else {
            val s1 = o1.toString();
            return o0 + s1;
        }
    }

    private String concat3(final Object o0, final Object o1, final Object o2) {
        return concat2(concat2(o0, o1), o2);
    }

    /**  S1452: Remove usage of generic wildcard type. */
    @SuppressWarnings({"java:S1452"})
    public
    Quartet<Boolean, Reduction, ? extends TopLevelNode, List<Token>>
    tryReduce(final List<Token> tokens) throws LanguageNotRecognizedException {
        val tryReduceNamedFunctionResult = tryReduceNamedFunction(tokens);
        val namedFunctionWasReduced = tryReduceNamedFunctionResult.getLeft();
        if (namedFunctionWasReduced) {
            val namedFunction = tryReduceNamedFunctionResult.getMiddle();
            val tokensAfterNamedFunction =
                    tryReduceNamedFunctionResult.getRight();
            return new Quartet<>(
                    true, Reduction.NAMED_FUNCTION, namedFunction,
                    tokensAfterNamedFunction);
        }
        val tryReduceApplicationResult = tryReduceApplication(tokens);
        val applicationWasReduced = tryReduceApplicationResult.getLeft();
        if (applicationWasReduced) {
            val application = tryReduceApplicationResult.getMiddle();
            val tokensAfterApplication = tryReduceApplicationResult.getRight();
            return new Quartet<>(
                    true, Reduction.APPLICATION, application,
                    tokensAfterApplication);
        }
        val tryReduceExecutionRequestResult = tryReduceExecutionRequest(tokens);
        val executionRequestWasReduced =
                tryReduceExecutionRequestResult.getLeft();
        if (executionRequestWasReduced) {
            val executionRequest = tryReduceExecutionRequestResult.getMiddle();
            val tokensAfterExecutionRequest =
                    tryReduceExecutionRequestResult.getRight();
            return new Quartet<>(
                    true, Reduction.EXECUTION_REQUEST, executionRequest,
                    tokensAfterExecutionRequest);
        }
        return new Quartet<>(false, Reduction.NONE, null, null);
    }

    /**  <p>`S:3776`: Cognitive complexity of methods should not be too
     * high. */
    @SuppressWarnings({"java:S3776"})
    public
    Triple<Boolean, NamedFunction, List<Token>>
    tryReduceNamedFunction(final List<Token> tokens)
            throws LanguageNotRecognizedException {

        val tryReduceNamedFunctionHeadStartResult =
                tryReduceNamedFunctionHeadStart(tokens);
        val canReduceNamedFunctionHeadStart =
                tryReduceNamedFunctionHeadStartResult.getLeft();
        if (!canReduceNamedFunctionHeadStart) {
            return new ImmutableTriple<>(false, null, null);
        }
        val expectedIdentifierAtNamedFunctionTail =
                tryReduceNamedFunctionHeadStartResult.getMiddle();
        val remainingTokensAfterNamedFunctionHeadStart =
                tryReduceNamedFunctionHeadStartResult.getRight();

        val tryReduceNamedFunctionTypeResult = tryReduceNamedFunctionType(
                remainingTokensAfterNamedFunctionHeadStart);
        val canReduceNamedFunctionType =
                tryReduceNamedFunctionTypeResult.getLeft();
        forceAssert(canReduceNamedFunctionType);
        val remainingTokensAfterNamedFunctionType =
                tryReduceNamedFunctionTypeResult.getRight();

        val tryReduceNamedFunctionReceivesClauseResult =
                tryReduceNamedFunctionReceivesClause(
                        remainingTokensAfterNamedFunctionType);
        val canReduceNamedFunctionReceivesClause =
                tryReduceNamedFunctionReceivesClauseResult.getLeft();
        forceAssert(canReduceNamedFunctionReceivesClause);
        val remainingTokensAfterNamedFunctionReceivesClause =
                tryReduceNamedFunctionReceivesClauseResult.getRight();

        val expectedIdentifierAtFunctionTail =
                expectedIdentifierAtNamedFunctionTail.getIdentifierName();

        val tryReduceNamedFunctionReturnsClauseResult =
                tryReduceNamedFunctionReturnsClause(
                        remainingTokensAfterNamedFunctionReceivesClause);
        val canReduceNamedFunctionReturnsClause =
                tryReduceNamedFunctionReturnsClauseResult.getLeft();
        forceAssert(canReduceNamedFunctionReturnsClause);
        val whatTheFunctionReturns =
                tryReduceNamedFunctionReturnsClauseResult.getMiddle();
        val remainingTokensAfterNamedFunctionReturnsClause =
                tryReduceNamedFunctionReturnsClauseResult.getRight();

        val returnsNothingAtAll = EmptyExpressionType.class.equals(
                whatTheFunctionReturns.getClass());

        val remainingTokensAfterNamedFunctionReturnsClauseSize =
                remainingTokensAfterNamedFunctionReturnsClause.size();

        boolean singleStatement = false;
        boolean sequential = false;

        if (returnsNothingAtAll) {

            forceAssert(remainingTokensAfterNamedFunctionReturnsClauseSize > 8);

            //   Would expect `SO`.
            val token0 = remainingTokensAfterNamedFunctionReturnsClause.get(0);
            if (token0.getType() != Token.Type.SO) {
                return new ImmutableTriple<>(false, null, null);
            }

            //   Would expect `IT`.
            val token1 = remainingTokensAfterNamedFunctionReturnsClause.get(1);
            if (token1.getType() != Token.Type.IT) {
                return new ImmutableTriple<>(false, null, null);
            }

            //   Would expect `CAUSES`.
            val token2 = remainingTokensAfterNamedFunctionReturnsClause.get(2);
            if (token2.getType() != Token.Type.CAUSES) {
                return new ImmutableTriple<>(false, null, null);
            }

            //   Would expect `SIDE`.
            val token3 = remainingTokensAfterNamedFunctionReturnsClause.get(3);
            if (token3.getType() != Token.Type.SIDE) {
                return new ImmutableTriple<>(false, null, null);
            }

            //   Would expect `EFFECTS`.
            val token4 = remainingTokensAfterNamedFunctionReturnsClause.get(4);
            if (token4.getType() != Token.Type.EFFECTS) {
                return new ImmutableTriple<>(false, null, null);
            }

            //   Would expect `AND`.
            val token5 = remainingTokensAfterNamedFunctionReturnsClause.get(5);
            if (token5.getType() != Token.Type.AND) {
                return new ImmutableTriple<>(false, null, null);
            }

            //   Would expect `EXECUTES`.
            val token6 = remainingTokensAfterNamedFunctionReturnsClause.get(6);
            if (token6.getType() != Token.Type.EXECUTES) {
                return new ImmutableTriple<>(false, null, null);
            }

            //   Would expect `STATEMENTS` or `A` [single statement].
            val token7 = remainingTokensAfterNamedFunctionReturnsClause.get(7);
            if (token7.getType() == Token.Type.A) {
                singleStatement = true;
            }

            //   Would expect `SEQUENTIALLY` or `CONCURRENTLY` (if not
            // `singleStatement`) or `SINGLE` (if `singleStatement`).
            val token8 = remainingTokensAfterNamedFunctionReturnsClause.get(8);
            if (singleStatement) {
                forceAssert(token8.getType() == Token.Type.SINGLE);
            } else {
                if (token8.getType() == Token.Type.SEQUENTIALLY) {
                    sequential = true;
                } else {
                    forceAssert(token8.getType() == Token.Type.CONCURRENTLY);
                }
            }
        }

        if (returnsNothingAtAll) {
            if (singleStatement) {
                forceAssert(
                        remainingTokensAfterNamedFunctionReturnsClauseSize > 9);
            } else {
                forceAssert(remainingTokensAfterNamedFunctionReturnsClauseSize >
                        10);
            }
        } else {
            forceAssert(remainingTokensAfterNamedFunctionReturnsClauseSize > 1);
        }

        if (returnsNothingAtAll) {
            if (singleStatement) {
                //   Would expect `STATEMENT`.
                val token9 = remainingTokensAfterNamedFunctionReturnsClause.get(9);
                if (token9.getType() != Token.Type.STATEMENT) {
                    return new ImmutableTriple<>(false, null, null);
                }

                //   Would expect `AND`.
                val token10 =
                        remainingTokensAfterNamedFunctionReturnsClause.get(10);
                if (token10.getType() != Token.Type.AND) {
                    return new ImmutableTriple<>(false, null, null);
                }

                //   Would expect `DOES`.
                val token11 =
                        remainingTokensAfterNamedFunctionReturnsClause.get(11);
                if (token11.getType() != Token.Type.DOES) {
                    return new ImmutableTriple<>(false, null, null);
                }
            } else {
                //   Would expect `AND`.
                val token9 = remainingTokensAfterNamedFunctionReturnsClause.get(9);
                if (token9.getType() != Token.Type.AND) {
                    return new ImmutableTriple<>(false, null, null);
                }

                //   Would expect `DOES`.
                val token10 =
                        remainingTokensAfterNamedFunctionReturnsClause.get(10);
                if (token10.getType() != Token.Type.DOES) {
                    return new ImmutableTriple<>(false, null, null);
                }
            }
        } else {
            //   Would expect `AND`.
            val token0 = remainingTokensAfterNamedFunctionReturnsClause.get(0);
            if (token0.getType() != Token.Type.AND) {
                return new ImmutableTriple<>(false, null, null);
            }

            //   Would expect `DOES`.
            val token1 =
                    remainingTokensAfterNamedFunctionReturnsClause.get(1);
            if (token1.getType() != Token.Type.DOES) {
                return new ImmutableTriple<>(false, null, null);
            }
        }

        val a = singleStatement ? 12 : 11;

        val nestedCallTokensSize =
                remainingTokensAfterNamedFunctionReturnsClauseSize -
                        (
                                returnsNothingAtAll ?
                                        a :
                                        2 /* `and does` */
                        );
        final List<Token> nestedCallTokens =
                new ArrayList<>(nestedCallTokensSize);
        for (int i = 0; i < nestedCallTokensSize; i++) {
            nestedCallTokens.add(
                    remainingTokensAfterNamedFunctionReturnsClause.get(
                            (
                                    returnsNothingAtAll ?
                                            a :
                                            2 /* `and does` */
                            ) + i
                    )
            );
        }

        val tryReduceFunctionBodyResults = tryReduceFunctionBody(
                nestedCallTokens, !returnsNothingAtAll, singleStatement,
                sequential);
        val canReduceFunctionBody = tryReduceFunctionBodyResults.getLeft();
        forceAssert(canReduceFunctionBody);
        val functionBody = tryReduceFunctionBodyResults.getMiddle();
        val tokensAfterFunctionBodyReduction =
                tryReduceFunctionBodyResults.getRight();
        val tryReduceFunctionTailResult = tryReduceFunctionTail(
                tokensAfterFunctionBodyReduction,
                expectedIdentifierAtFunctionTail.toString());
        val canReduceFunctionTail = tryReduceFunctionTailResult.getLeft();
        forceAssert(canReduceFunctionTail);
        val tokensAfterFunctionTailReduction =
                tryReduceFunctionTailResult.getRight();
        val namedFunction = new NamedFunction(
                expectedIdentifierAtFunctionTail, functionBody);
        return new ImmutableTriple<>(
                true, namedFunction, tokensAfterFunctionTailReduction);
    }

    /**  @return a {@link Triple} where: The left part holds whether the
     * named function head reduction can be applied or not. The middle
     * part holds the name of the named function, as its {@link Token}.
     * The right part holds the remaining tokens after this reduction is
     * applied. */
    public Triple<Boolean, Token, List<Token>> tryReduceNamedFunctionHeadStart(
            final List<Token> tokens) {

        val tokensSize = tokens.size();

        if (tokensSize < 4) {
            return new ImmutableTriple<>(false, null, null);
        }

        //   Would expect `FUNCTION`.
        val token0 = tokens.get(0);
        if (token0.getType() != Token.Type.FUNCTION) {
            return new ImmutableTriple<>(false, null, tokens);
        }

        //   Would expect `IDENTIFIER`.
        val token1 = tokens.get(1);
        if (token1.notAnIdentifier()) {
            return new ImmutableTriple<>(false, null, tokens);
        }

        //   Would expect `IS`.
        val token2 = tokens.get(2);
        if (token2.getType() != Token.Type.IS) {
            return new ImmutableTriple<>(false, token1, tokens);
        }

        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 3);
        for (int i = 3; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }

        return new ImmutableTriple<>(true, token1, remainingTokens);
    }

    /**  @return a {@link Triple} where: The left part holds whether the
     * named function type reduction can be applied or not. The middle
     * part holds the type of the named function. The right part holds
     * the remaining tokens after this reduction is applied. */
    public
    Triple<Boolean, NamedFunction.Type, List<Token>>
    tryReduceNamedFunctionType(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forceAssert(tokensSize > 1);

        //   Would expect `A`.
        val token0 = tokens.get(0);
        if (token0.getType() != Token.Type.A) {
            return new ImmutableTriple<>(false, null, tokens);
        }

        boolean commandLineInterfaceFunction = false;
        boolean pureFunction = false;

        //   Would expect `COMMAND` or `PURE`.
        val token1 = tokens.get(1);
        if (token1.getType() == Token.Type.COMMAND) {
            commandLineInterfaceFunction = true;
        } else if (token1.getType() == Token.Type.PURE) {
            pureFunction = true;
        } else {
            return new ImmutableTriple<>(false, null, tokens);
        }

        final List<Token> remainingTokens;

        if (commandLineInterfaceFunction) {
            forceAssert(tokensSize > 5);

            return tryReduceCommandLineInterfaceNamedFunctionType(
                    tokens);
        } else {
            forceAssert(pureFunction);

            forceAssert(tokensSize > 2);

            //   Would expect `FUNCTION`.
            val token2 = tokens.get(2);
            if (token2.getType() != Token.Type.FUNCTION) {
                return new ImmutableTriple<>(
                        false, NamedFunction.Type.CLI, tokens);
            }

            remainingTokens = discardThreeFrom(tokens);

            return new ImmutableTriple<>(
                    true, NamedFunction.Type.PURE, remainingTokens);
        }
    }

    public
    Triple<Boolean, NamedFunction.Type, List<Token>>
    tryReduceCommandLineInterfaceNamedFunctionType(
            final List<Token> tokens) {

        final List<Token> remainingTokens;

        //   Would expect `LINE`.
        val token2 = tokens.get(2);
        if (token2.getType() != Token.Type.LINE) {
            return new ImmutableTriple<>(
                    false, NamedFunction.Type.CLI, tokens);
        }

        //   Would expect `INTERFACE`.
        val token3 = tokens.get(3);
        if (token3.getType() != Token.Type.INTERFACE) {
            return new ImmutableTriple<>(
                    false, NamedFunction.Type.CLI, tokens);
        }

        //   Would expect `APPLICATION`.
        val token4 = tokens.get(4);
        if (token4.getType() != Token.Type.APPLICATION) {
            return new ImmutableTriple<>(
                    false, NamedFunction.Type.CLI, tokens);
        }

        //   Would expect `FUNCTION`.
        val token5 =  tokens.get(5);
        if (token5.getType() != Token.Type.FUNCTION) {
            return new ImmutableTriple<>(
                    false, NamedFunction.Type.CLI, tokens);
        }

        remainingTokens = discardSixFrom(tokens);

        return new ImmutableTriple<>(
                true, NamedFunction.Type.CLI, remainingTokens);
    }

    /**  @return a {@link Triple} where: The left part holds whether the
     * named function 'function receives clause' reduction can be
     * applied or not. The middle part holds what the function receives,
     * if anything. The right part holds the remaining tokens after this
     * reduction is applied. */
    public
    Triple<Boolean, ExpressionType, List<Token>>
    tryReduceNamedFunctionReceivesClause(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forceAssert(tokensSize > 3);

        boolean receivesNothingAtAll = false;
        boolean receivesSomething = false;
        ExpressionType whatReceives = new ExpressionTypeFactory().unresolved();

        //   Would expect `AND`.
        val token0 = tokens.get(0);
        if (token0.getType() != Token.Type.AND) {
            return new ImmutableTriple<>(false, whatReceives, tokens);
        }

        //   Would expect `RECEIVES`.
        val token1 = tokens.get(1);
        if (token1.getType() != Token.Type.RECEIVES) {
            return new ImmutableTriple<>(false, whatReceives, tokens);
        }

        //   Would expect `NOTHING` or `A`.
        val token2 = tokens.get(2);
        if (token2.getType() == Token.Type.NOTHING) {
            receivesNothingAtAll = true;
            whatReceives = new ExpressionTypeFactory().empty();
        } else if (token2.getType() == Token.Type.A) {
            receivesSomething = true;
        } else {
            return new ImmutableTriple<>(false, whatReceives, tokens);
        }

        if (receivesNothingAtAll) {
            forceAssert(tokensSize > 4);

            //   Would expect `AT`.
            val token3 = tokens.get(3);
            if (token3.getType() != Token.Type.AT) {
                return new ImmutableTriple<>(false, whatReceives, tokens);
            }

            //   Would expect `ALL`.
            val token4 = tokens.get(4);
            if (token4.getType() != Token.Type.ALL) {
                return new ImmutableTriple<>(false, whatReceives, tokens);
            }

            final List<Token> remainingTokens = new ArrayList<>(tokensSize - 5);
            for (int i = 5; i < tokensSize; i++) {
                val someToken = tokens.get(i);
                remainingTokens.add(someToken);
            }

            return new ImmutableTriple<>(true, whatReceives, remainingTokens);
        } else {
            forceAssert(receivesSomething);

            //   Would expect `NATURAL`.
            val token3 = tokens.get(3);
            if (token3.getType() != Token.Type.NATURAL) {
                return new ImmutableTriple<>(false, whatReceives, tokens);
            }

            whatReceives = new ExpressionTypeFactory().natural();

            final List<Token> remainingTokens = discardFourFrom(tokens);

            return new ImmutableTriple<>(true, whatReceives, remainingTokens);
        }
    }

    /**  @return a {@link Triple} where: The left part holds whether the
     * named function 'function returns clause' reduction can be applied
     * or not. The middle part holds what the function returns, if
     * anything. The right part holds the remaining tokens after this
     * reduction is applied. */
    public
    Triple<Boolean, ExpressionType, List<Token>>
    tryReduceNamedFunctionReturnsClause(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forceAssert(tokensSize > 3);

        boolean returnsNothingAtAll = false;
        boolean returnsSomething = false;
        ExpressionType whatReturns = new ExpressionTypeFactory().unresolved();

        //   Would expect `AND`.
        val token0 = tokens.get(0);
        if (token0.getType() != Token.Type.AND) {
            return new ImmutableTriple<>(false, whatReturns, tokens);
        }

        //   Would expect `RETURNS`.
        val token1 = tokens.get(1);
        if (token1.getType() != Token.Type.RETURNS) {
            return new ImmutableTriple<>(false, whatReturns, tokens);
        }

        //   Would expect `NOTHING` or `A`.
        val token2 = tokens.get(2);
        if (token2.getType() == Token.Type.NOTHING) {
            returnsNothingAtAll = true;
            whatReturns = new ExpressionTypeFactory().empty();
        } else if (token2.getType() == Token.Type.A) {
            returnsSomething = true;
        } else {
            return new ImmutableTriple<>(false, whatReturns, tokens);
        }

        if (returnsNothingAtAll) {
            forceAssert(tokensSize > 4);

            //   Would expect `AT`.
            val token3 = tokens.get(3);
            if (token3.getType() != Token.Type.AT) {
                return new ImmutableTriple<>(false, whatReturns, tokens);
            }

            //   Would expect `ALL`.
            val token4 = tokens.get(4);
            if (token4.getType() != Token.Type.ALL) {
                return new ImmutableTriple<>(false, whatReturns, tokens);
            }

            final List<Token> remainingTokens = new ArrayList<>(tokensSize - 5);
            for (int i = 5; i < tokensSize; i++) {
                val someToken = tokens.get(i);
                remainingTokens.add(someToken);
            }

            return new ImmutableTriple<>(true, whatReturns, remainingTokens);
        } else {
            forceAssert(returnsSomething);

            //   Would expect `NATURAL`.
            val token3 = tokens.get(3);
            if (token3.getType() != Token.Type.NATURAL) {
                return new ImmutableTriple<>(false, whatReturns, tokens);
            }

            whatReturns = new ExpressionTypeFactory().natural();

            final List<Token> remainingTokens = discardFourFrom(tokens);

            return new ImmutableTriple<>(true, whatReturns, remainingTokens);
        }
    }

    private static List<Token> discardThreeFrom(final List<Token> tokens) {

        val tokensSize = tokens.size();
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 3);
        for (int i = 3; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }
        return remainingTokens;
    }

    private static List<Token> discardFourFrom(final List<Token> tokens) {

        val tokensSize = tokens.size();
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 4);
        for (int i = 4; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }
        return remainingTokens;
    }

    private static List<Token> discardSixFrom(final List<Token> tokens) {

        val tokensSize = tokens.size();
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 6);
        for (int i = 6; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }
        return remainingTokens;
    }

    /**  `java:S1452`: Remove usage of generic wildcard type.
     *   @param tokens A list of tokens remaining to be processed.
     *   @param pure Some particular stuff can be expected in a pure
     * function (e.g. just an expression composed of other expressions
     * or from an atom (e.g. a natural literal, a string literal...)),
     * while entirely different stuff can be expected in a non pure
     * function (e.g. console prints, new lines, sequential or parallel
     * execution...).
     *   @param singleStatement If not `pure`, `true` if there is only
     * one statement, else `false`.
     *   @param sequential If not `pure` and not `singleStatement`,
     * `true` if statements are sequential, else `false`.
     *   @return A {@link Pair} holding whether can do a function body
     * reduction or not (at {@link Pair#getLeft()}) and, in case that is
     * true, how many tokens would that reduction consume (at {@link
     * Pair#getRight()}). */
    @SuppressWarnings({"java:S1452"}) public
    Triple<Boolean, ? extends NamedFunctionBody, ? extends List<Token>>
    tryReduceFunctionBody(
            final List<Token> tokens, boolean pure, boolean singleStatement,
            boolean sequential) throws LanguageNotRecognizedException {

        return pure ?
                tryReducePureFunctionBody(tokens) :
                tryReduceNonPureFunctionBody(
                        tokens, singleStatement, sequential);
    }

    public
    Triple<Boolean, NamedFunctionBody, List<Token>>
    tryReducePureFunctionBody(final List<Token> tokens) {
        val tokensSize = tokens.size();
        forceAssert(tokensSize > 0);
        val token0 = tokens.get(0);
        if (token0.getType() != Token.Type.RETURN) {
            return new ImmutableTriple<>(false, null, null);
        }
        val nestedExpressionTokensSize = tokensSize - 1;
        final List<Token> nestedExpressionTokens =
                new ArrayList<>(nestedExpressionTokensSize);
        for (int i = 0; i < nestedExpressionTokensSize; i++) {
            val someToken = tokens.get(i + 1);
            nestedExpressionTokens.add(someToken);
        }
        val tryReducePureExpressionResult =
                tryReducePureExpression(nestedExpressionTokens);
        val tryReducePureExpression = tryReducePureExpressionResult.getLeft();
        forceAssert(tryReducePureExpression);
        val pureExpression = tryReducePureExpressionResult.getMiddle();
        val tokensAfterPureExpressionReduction =
                tryReducePureExpressionResult.getRight();
        val tryReduceWhereValueBindingClauses =
                tryReduceWhereValueBindingClauses(
                        tokensAfterPureExpressionReduction);
        val canReduceWhereValueBindingClauses =
                tryReduceWhereValueBindingClauses.getLeft();
        if (canReduceWhereValueBindingClauses) {
            val whereValueBindingClauses =
                    tryReduceWhereValueBindingClauses.getMiddle();
            val tokensAfterWhereValueBindingClauses =
                    tryReduceWhereValueBindingClauses.getRight();
            return new ImmutableTriple<>(
                    true,
                    NamedFunctionBody.fromPureExpression(
                            pureExpression
                    ).where(whereValueBindingClauses),
                    tokensAfterWhereValueBindingClauses);
        } else {
            return new ImmutableTriple<>(
                    true, NamedFunctionBody.fromPureExpression(pureExpression),
                    tokensAfterPureExpressionReduction);
        }
    }

    private
    Triple<Boolean, ? extends Expression, ? extends List<Token>>
    tryReducePureExpression(final List<Token> tokens) {
        val tokensSize = tokens.size();
        forceAssert(tokensSize > 1);
        val token0 = tokens.get(0);
        val token0Type = token0.getType();
        if (token0Type == Token.Type.RETURN) {
            val tokensForMaybeExpressionSize = tokensSize - 1;
            val tokensForMaybeExpression =
                    new ArrayList<Token>(tokensForMaybeExpressionSize);
            for (int i = 0; i < tokensForMaybeExpressionSize; i++) {
                tokensForMaybeExpression.add(tokens.get(i + 1));
            }
            val tryReducePureExpression =
                    tryReducePureExpression(tokensForMaybeExpression);
            val canReducePureExpression = tryReducePureExpression.getLeft();
            if (canReducePureExpression) {
                return new ImmutableTriple<>(
                        true, tryReducePureExpression.getMiddle(),
                        tryReducePureExpression.getRight());
            }
        } else {
            forceAssert(token0Type == Token.Type.IDENTIFIER);
            val tokensForMaybeExpressionSize = tokensSize - 1;
            val tokensForMaybeExpression =
                    new ArrayList<Token>(tokensForMaybeExpressionSize);
            for (int i = 0; i < tokensForMaybeExpressionSize; i++) {
                tokensForMaybeExpression.add(tokens.get(i + 1));
            }
            return new ImmutableTriple<>(
                    true,
                    IdentifierExpression.fromIdentifierToken(token0).makePure(),
                    tokensForMaybeExpression);
        }
        return new ImmutableTriple<>(false, null, null);
    }

    /**  `java:S1452`: Remove usage of generic wildcard type.
     *   @param tokens A list of tokens remaining to be processed.
     *   @param singleStatement `true` if there is only one statement,
     * else `false`.
     *   @param sequential If not `singleStatement`, `true` if
     * statements are sequential, else `false`.
     *   @return A {@link Pair} holding whether can do a function body
     * reduction or not (at {@link Pair#getLeft()}) and, in case that is
     * true, how many tokens would that reduction consume (at {@link
     * Pair#getRight()}). */
    @SuppressWarnings({"java:S1452"}) public
    Triple<Boolean, NamedFunctionBody, ? extends List<Token>>
    tryReduceNonPureFunctionBody(
            final List<Token> tokens, boolean singleStatement,
            boolean sequential) throws LanguageNotRecognizedException {

        val tokensSize = tokens.size();
        Triple<
            Boolean, ? extends Statement, List<Token>
        > tryReduceStatementResult = tryReduceStatement(tokens);
        boolean canReduceStatement = tryReduceStatementResult.getLeft();
        if (!canReduceStatement) {
            return new ImmutableTriple<>(false, null, null);
        }
        Statement statement = tryReduceStatementResult.getMiddle();
        val statementsList = new ArrayList<Statement>(16);
        statementsList.add(statement);
        List<Token> remainingTokens = tryReduceStatementResult.getRight();
        int remainingTokensSize = remainingTokens.size();
        Triple<
                Boolean, RunTimeInterleave, List<Token>
        > tryReduceAnStatementsConnectingClauseResult =
                tryReduceAnStatementsConnectingClause(remainingTokens);
        boolean canReduceAnStatementsConnectingClause =
                tryReduceAnStatementsConnectingClauseResult.getLeft();
        val expectedRunTimeInterleave =
                tryReduceAnStatementsConnectingClauseResult.getMiddle();
        while (canReduceAnStatementsConnectingClause) {
            remainingTokens =
                    tryReduceAnStatementsConnectingClauseResult.getRight();
            tryReduceStatementResult = tryReduceStatement(remainingTokens);
            canReduceStatement = tryReduceStatementResult.getLeft();
            if (!canReduceStatement) {
                throw new LanguageNotRecognizedException(FIXME);
            }
            statement = tryReduceStatementResult.getMiddle();
            statementsList.add(statement);
            remainingTokens = tryReduceStatementResult.getRight();
            remainingTokensSize = remainingTokens.size();
            tryReduceAnStatementsConnectingClauseResult =
                    tryReduceAnStatementsConnectingClause(remainingTokens);
            canReduceAnStatementsConnectingClause =
                    tryReduceAnStatementsConnectingClauseResult.getLeft();
            if (canReduceAnStatementsConnectingClause) {
                val actualRunTimeInterleave =
                        tryReduceAnStatementsConnectingClauseResult.getMiddle();
                forcedAssertion(expectedRunTimeInterleave ==
                        actualRunTimeInterleave);
            }
        }

        //   No further statements? Any `where` value binding clauses?

        val tryReduceWhereValueBindingClausesResult =
                tryReduceWhereValueBindingClauses(remainingTokens);
        val canReduceWhereValueBindingClauses =
                tryReduceWhereValueBindingClausesResult.getLeft();
        final WhereValueBindings whereValueBindings;
        if (canReduceWhereValueBindingClauses) {
            whereValueBindings =
                    tryReduceWhereValueBindingClausesResult.getMiddle();
            val tokensAfterWhereValueBindingClauses =
                    tryReduceWhereValueBindingClausesResult.getRight();
            remainingTokens = tokensAfterWhereValueBindingClauses;
            remainingTokensSize = tokensAfterWhereValueBindingClauses.size();
        } else {
            whereValueBindings = null;
        }

        //   No [more] `where` value binding clauses? Assume the
        // function tail is coming up next. That is, return true.

        forcedAssertion(tokensSize < Short.MAX_VALUE + 1);
        forcedAssertion(remainingTokensSize < Short.MAX_VALUE + 1);
        forcedAssertion(tokensSize > remainingTokensSize);

        final Statements statements;
        if (statementsList.size() == 1) {
            forceAssert(singleStatement);
            statements = Statements.single(statement);
        } else {
            forceAssert(!singleStatement);
            if (sequential) {
                statements = Statements.sequential(statementsList);
            } else {
                statements = Statements.parallel(statementsList);
            }
        }

        return new ImmutableTriple<>(
                true,
                whereValueBindings == null ?
                        NamedFunctionBody.fromStatements(statements) :
                        NamedFunctionBody.fromStatementsAndWhereValueBindings(
                                statements, whereValueBindings),
                remainingTokens);
    }

    /**  <p>`java:S1452`: Remove usage of generic wildcard type.
     *   @return A {@link Pair}; where the right holds: the remaining
     * tokens after the first statement reduction, if a statement
     * reduction can be performed, else `null`; and the left holds the
     * actual result of the statement (whether a statement reduction
     * can be performed or not. */
    @SuppressWarnings({"java:S1452"})
    public Triple<Boolean, ? extends Statement, List<Token>>
    tryReduceStatement(final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 2) {
            return new ImmutableTriple<>(false, null, null);
        }
        val token0 = tokens.get(0);
        if (Token.Type.NEW == token0.getType()) {
            final List<Token> remainingTokens = new ArrayList<>(tokensSize - 1);
            for (int i = 1; i < tokensSize; i++) {
                val someToken = tokens.get(i);
                remainingTokens.add(someToken);
            }
            return tryReduceNewLineStatement(remainingTokens);
        }
        if (Token.Type.PRINT == token0.getType()) {
            final List<Token> remainingTokens = new ArrayList<>(tokensSize - 1);
            for (int i = 1; i < tokensSize; i++) {
                val someToken = tokens.get(i);
                remainingTokens.add(someToken);
            }
            return tryReducePrintStatement(remainingTokens);
        }
        if (Token.Type.EXECUTE == token0.getType()) {
            final List<Token> remainingTokens = new ArrayList<>(tokensSize - 1);
            for (int i = 1; i < tokensSize; i++) {
                val someToken = tokens.get(i);
                remainingTokens.add(someToken);
            }
            return tryReduceExecuteStatement(remainingTokens);
        }
        return new ImmutableTriple<>(false, null, null);
    }

    public Triple<Boolean, NewLineStatement, List<Token>>
    tryReduceNewLineStatement(final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 1) {
            return new ImmutableTriple<>(false, null, null);
        }
        val token0 = tokens.get(0);
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 1);
        for (int i = 1; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }
        if (Token.Type.LINE == token0.getType()) {
            return new ImmutableTriple<>(
                    true, new NewLineStatement(), remainingTokens);
        }
        return new ImmutableTriple<>(false, null, null);
    }

    /**  <p>The `print` token has been already seen by the caller of
     * this, this one only has to check that what comes next can be
     * really printed. */
    public Triple<Boolean, PrintStatement, List<Token>> tryReducePrintStatement(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 1) {
            return new ImmutableTriple<>(false, null, null);
        }
        val token0 = tokens.get(0);
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 1);
        for (int i = 1; i < tokensSize; i++) {
            val someToken = tokens.get(i);
            remainingTokens.add(someToken);
        }
        if (Token.Type.STRING_LITERAL == token0.getType()) {
            return new ImmutableTriple<>(
                    true, new PrintStatement(token0), remainingTokens);
        }
        if (Token.Type.IDENTIFIER == token0.getType()) {
            //   Semantic analysis out of scope at this point.
            return new ImmutableTriple<>(
                    true, new PrintStatement(token0), remainingTokens);
        }
        return new ImmutableTriple<>(false, null, null);
    }

    /**  <p>The `execute` token has been already seen by the caller of
     * this, this one only has to check that what comes next can be
     * really executed. */
    public Triple<Boolean, ExecuteStatement, List<Token>>
    tryReduceExecuteStatement(final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize < 5) {
            // `call function ident inner ident ends`
            //  1    2        3
            // `passing no arguments at all
            //  4       5  6         7  8
            return new ImmutableTriple<>(false, null, null);
        }

        //   Currently the only expressions that can follow the
        // `execute` keyword are function call expressions
        // (syntactically, semantically actually only non-pure function
        // call expressions (that is, function calls that cause side
        // effects or resort on some other functions which cause side
        // effects)) can follow.

        val token0 = tokens.get(0);

        forceAssert(Token.Type.CALL == token0.getType());

        val tryReduceFunctionCallExpressionResult =
                tryReduceFunctionCall(tokens);
        val canReduceFunctionCallExpression =
                tryReduceFunctionCallExpressionResult.getLeft();
        forceAssert(canReduceFunctionCallExpression);
        val functionCallExpression =
                tryReduceFunctionCallExpressionResult.getMiddle();
        val remainingTokensAfterFunctionCallExpression =
                tryReduceFunctionCallExpressionResult.getRight();

        //   Semantic analysis out of scope at this point.
        return new ImmutableTriple<>(
                true, new ExecuteStatement(functionCallExpression),
                remainingTokensAfterFunctionCallExpression);
    }

    public Triple<Boolean, WhereValueBindings, List<Token>>
    tryReduceWhereValueBindingClauses(final List<Token> tokens) {

        val tryReduceWhereValueBindingClauseResult =
                tryReduceWhereValueBindingClause(tokens);
        val canReduceWhereValueBindingClause =
                tryReduceWhereValueBindingClauseResult.getLeft();
        if (canReduceWhereValueBindingClause) {
            val firstWhereValueBinding =
                    tryReduceWhereValueBindingClauseResult.getMiddle();
            val tokensAfterWhereValueBindingClause =
                    tryReduceWhereValueBindingClauseResult.getRight();
            val tryReduceWhereValueBindingClausesResult =
                    tryReduceWhereValueBindingClauses(
                            tokensAfterWhereValueBindingClause);
            val canReduceMoreWhereValueBindingClauses =
                    tryReduceWhereValueBindingClausesResult.getLeft();
            if (canReduceMoreWhereValueBindingClauses) {
                val subsequentWhereValueBindings =
                        tryReduceWhereValueBindingClausesResult.getMiddle();
                val tokensAfterAllWhereValueBindings =
                        tryReduceWhereValueBindingClausesResult.getRight();
                return new ImmutableTriple<>(
                        true, new WhereValueBindingsFactory().prepend(
                                firstWhereValueBinding,
                                subsequentWhereValueBindings),
                        tokensAfterAllWhereValueBindings);
            }
            return new ImmutableTriple<>(
                    true, new WhereValueBindings(firstWhereValueBinding),
                    tokensAfterWhereValueBindingClause);
        }
        return new ImmutableTriple<>(false, null, null);
    }

    public Triple<Boolean, WhereValueBinding, List<Token>>
    tryReduceWhereValueBindingClause(final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 0);
        val token0 = tokens.get(0);
        if (Token.Type.WHERE != token0.getType()) {
            return new ImmutableTriple<>(false, null, null);
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
        val tokensForExpressionSize = tokensSize - tokensToAdvanceNow;
        val tokensForExpression = new ArrayList<Token>(tokensForExpressionSize);
        for (int i = 0; i < tokensForExpressionSize; i++) {
            val someToken = tokens.get(i + tokensToAdvanceNow);
            tokensForExpression.add(someToken);
        }
        val tryReduceExpressionResult =
                tryReduceExpression(tokensForExpression);
        val canReduceExpression = tryReduceExpressionResult.getLeft();
        if (canReduceExpression) {
            val expression = tryReduceExpressionResult.getMiddle();
            val remainingTokens = tryReduceExpressionResult.getRight();
            return new ImmutableTriple<>(
                    true, new WhereValueBinding(token1, expression),
                    remainingTokens);
        }
        return new ImmutableTriple<>(false, null, null);
    }

    public
    Pair<Boolean, List<Token>>
    tryReduceFunctionTail(
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
        val remainingTokensSize = tokensSize - 3;
        final List<Token> remainingTokens =
                new ArrayList<>(remainingTokensSize);
        for (int i = 0; i < remainingTokensSize; i++) {
            val token = tokens.get(i + 3);
            remainingTokens.add(token);
        }
        return new ImmutablePair<>(true, remainingTokens);
    }

    /**  <p>Expects either `and STATEMENT [...]` or `and then
     * STATEMENT` */
    public Triple<Boolean, Statements.RunTimeInterleave, List<Token>>
    tryReduceAnStatementsConnectingClause(final List<Token> tokens) {

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
                    true, Statements.RunTimeInterleave.SEQUENTIAL,
                    remainingTokens);
        } else {
            remainingTokens = new ArrayList<>(tokensSize - 1);
            for (int i = 0; i < tokensSize - 1; i++) {
                val someToken = tokens.get(i + 1);
                remainingTokens.add(someToken);
            }
            return new ImmutableTriple<>(
                    true, Statements.RunTimeInterleave.PARALLEL,
                    remainingTokens);
        }
    }

    public Triple<Boolean, ExecutionRequest, List<Token>>
    tryReduceExecutionRequest(final List<Token> tokens) {

        val tokensSize = tokens.size();
        // run command line interface application
        // 1   2       3    4         5
        // identifier hello, world identifier ends
        // 6
        if (tokensSize < 6) {
            return new ImmutableTriple<>(false, null, null);
        }

        val token0 = tokens.get(0);
        if (Token.Type.RUN != token0.getType()) {
            return new ImmutableTriple<>(false, null, null);
        }
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

        val remainingTokensSize = tokensSize - 6;
        final List<Token> remainingTokens =
                new ArrayList<>(remainingTokensSize);
        for (int i = 0; i < remainingTokensSize; i++) {
            val token = tokens.get(i + 6);
            remainingTokens.add(token);
        }

        return new ImmutableTriple<>(
                true, new ExecutionRequest(token5.getIdentifierName()),
                remainingTokens);
    }

    private enum ReceiveOrReturn { RECEIVE, RETURN; }

    public List<Token> reduceFunctionReceivesOrReturnsClause(
            final List<Token> tokens,
            final ReceiveOrReturn receiveOrReturn) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 5);
        val token0 = tokens.get(0);
        forcedAssertion(token0.getType() == Token.Type.AND);
        val token1 = tokens.get(1);
        if (ReceiveOrReturn.RECEIVE == receiveOrReturn) {
            forcedAssertion(token1.getType() == Token.Type.RECEIVES);
        } else {
            forcedAssertion(token1.getType() == Token.Type.RETURNS);
        }

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

    public List<Token> reduceFunctionReceivesClause(
            final List<Token> tokens) {

        return reduceFunctionReceivesOrReturnsClause(
                tokens, ReceiveOrReturn.RECEIVE);
    }

    public List<Token> reduceFunctionReturnsClause(
            final List<Token> tokens) {

        return reduceFunctionReceivesOrReturnsClause(
                tokens, ReceiveOrReturn.RETURN);
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
    Triple<Statements, WhereValueBindings, List<Token>>
    reduceFunctionBodyClause(final List<Token> tokens)
            throws LanguageNotRecognizedException {

        val reduceStatementsResult = reduceStatements(tokens);
        val statementsReduced = reduceStatementsResult.getLeft();
        val remainingTokensAfterStatementsReduction =
                reduceStatementsResult.getRight();
        val tryReduceWhereValueBindingsResult = tryReduceWhereValueBindings(
                remainingTokensAfterStatementsReduction);
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
                    remainingTokensAfterStatementsReduction;
        }
        return new ImmutableTriple<>(
                statementsReduced, whereValueBindings,
                remainingTokensAfterFunctionBody);
    }

    public Pair<Statements, List<Token>> reduceStatements(
            final List<Token> tokens) throws LanguageNotRecognizedException {

        val statements = new ArrayList<Statement>(4 /* Just a random guess. */);
        Pair<Statement, List<Token>> statementReductionResult =
                reduceStatement(tokens);
        Statement statement = statementReductionResult.getLeft();
        List<Token> remainingTokens = statementReductionResult.getRight();
        Triple<Boolean, List<Token>, RunTimeInterleave>
        tryReduceStatementsConnectiveResult;
        try {
            tryReduceStatementsConnectiveResult = tryReduceStatementsConnective(
                    remainingTokens, RunTimeInterleave.UNKNOWN);
        } catch (final LanguageNotRecognizedException lnre) {
            /* XXX never entered here! this block can only be entered when not passing an unknown run time interleave. */
            log.severe(lnre.toString());
            throw lnre;
        }
        boolean connectiveFoundAndReduced =
                tryReduceStatementsConnectiveResult.getLeft();
        final RunTimeInterleave runTimeInterleave;
        if (connectiveFoundAndReduced) {
            statements.add(statement);
            runTimeInterleave = tryReduceStatementsConnectiveResult.getRight();
            remainingTokens = tryReduceStatementsConnectiveResult.getMiddle();
        } else {
            return new ImmutablePair<>(
                    Statements.single(statement), remainingTokens);
        }

        do {
            statementReductionResult = reduceStatement(remainingTokens);
            statement = statementReductionResult.getLeft();
            statements.add(statement);
            remainingTokens = statementReductionResult.getRight();
            try {
                tryReduceStatementsConnectiveResult =
                        tryReduceStatementsConnective(
                                remainingTokens, runTimeInterleave);
            } catch (final LanguageNotRecognizedException lnre) {
                log.severe(lnre.toString());
                throw lnre;
            }
            connectiveFoundAndReduced =
                    tryReduceStatementsConnectiveResult.getLeft();
            if (connectiveFoundAndReduced) {
                remainingTokens = tryReduceStatementsConnectiveResult.getMiddle();
            } else {
                if (runTimeInterleave == RunTimeInterleave.PARALLEL) {
                    return new ImmutablePair<>(
                            Statements.parallel(statements), remainingTokens);
                } else {
                    forcedAssertion(
                            runTimeInterleave == RunTimeInterleave.SEQUENTIAL);
                    return new ImmutablePair<>(
                            Statements.sequential(statements), remainingTokens);
                }
            }
        } while (!remainingTokens.isEmpty());

        throw new IllegalStateException("illegal state reached?");
    }

    public Pair<Statement, List<Token>> reduceStatement(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 0);
        val token0 = tokens.get(0);
        if (Token.Type.PRINT == token0.getType()) {
            return reducePrintStatement(tokens);
        } else {
            forcedAssertion(Token.Type.NEW == token0.getType());
            return reduceNewLineStatement(tokens);
        }
    }

    public Pair<Statement, List<Token>> reducePrintStatement(
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

        final Statement statement = new PrintStatement(token1);
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 2);
        for (int i = 0; i < tokensSize - 2; i++) {
            val someToken = tokens.get(i + 2);
            remainingTokens.add(someToken);
        }
        return new ImmutablePair<>(statement, remainingTokens);
    }

    public Pair<Statement, List<Token>> reduceNewLineStatement(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        forcedAssertion(tokensSize > 1);
        val token0 = tokens.get(0);
        forcedAssertion(Token.Type.NEW == token0.getType());
        val token1 = tokens.get(1);
        forcedAssertion(Token.Type.LINE == token1.getType());

        final Statement statement = new NewLineStatement();
        final List<Token> remainingTokens = new ArrayList<>(tokensSize - 2);
        for (int i = 0; i < tokensSize - 2; i++) {
            val someToken = tokens.get(i + 2);
            remainingTokens.add(someToken);
        }
        return new ImmutablePair<>(statement, remainingTokens);
    }

    public
    Triple<Boolean, List<Token>, RunTimeInterleave>
    tryReduceStatementsConnective(
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
            return tryReduceStatementsConnectiveAnyInterleavingWillDo(tokens);
        }

        val token1 = tokens.get(1);
        if (Token.Type.THEN == token1.getType()) {
            return tryReduceStatementsSequentialConnective(
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
    tryReduceStatementsConnectiveAnyInterleavingWillDo(
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
    tryReduceStatementsSequentialConnective(
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
        List<Token> remainingTokens = tokens;
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
        if (Token.Type.CONDITIONAL == token0.getType()) {
            forceAssert(tokensSize > 8);
            return tryReduceConditionalExpression(tokens);
        }
        forceAssert(Token.Type.CALL == token0.getType());
        return tryReduceFunctionCall(tokens);
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

    @SuppressWarnings({"java:S1452"})  // Remove generic wildcard type.
    public
    Triple<Boolean, NamedFunctionCallExpression, ? extends List<Token>>
    tryReduceFunctionCall(final List<Token> tokens) {
        val tokensSize = tokens.size();
        /*   `call function ID passing no arguments at all` has a min
         * length of 8 */
        forceAssert(tokensSize > 7);
        val token0 = tokens.get(0);
        forceAssert(token0.getType() == Token.Type.CALL);
        val token1 = tokens.get(1);
        forceAssert(token1.getType() == Token.Type.FUNCTION);
        val token2 = tokens.get(2);
        forceAssert(token2.getType() == Token.Type.IDENTIFIER);
        val token3 = tokens.get(3);
        forceAssert(token3.getType() == Token.Type.PASSING);
        val token4 = tokens.get(4);
        forceAssert(token4.getType() == Token.Type.NO);
        val token5 = tokens.get(5);
        forceAssert(token5.getType() == Token.Type.ARGUMENTS);
        val token6 = tokens.get(6);
        forceAssert(token6.getType() == Token.Type.AT);
        val token7 = tokens.get(7);
        forceAssert(token7.getType() == Token.Type.ALL);
        val remainingTokensSize = tokensSize - 8;
        val remainingTokens = new ArrayList<Token>(remainingTokensSize);
        for (int i = 0; i < remainingTokensSize; i++) {
            remainingTokens.add(tokens.get(i + 8));
        }
        return new ImmutableTriple<>(
                true,
                NamedFunctionCallExpression.fromNamedFunctionIdentifier(
                        token2),
                remainingTokens);
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

    public Triple<Boolean, Application, List<Token>> tryReduceApplication(
            final List<Token> tokens) {

        val tryReduceApplicationHeadResult = tryReduceApplicationHead(tokens);
        val canReduceApplicationHead = tryReduceApplicationHeadResult.getLeft();
        if (!canReduceApplicationHead) {
            return new ImmutableTriple<>(false, null, null);
        }
        val expectedApplicationNameTokenFirstPosition =
                tryReduceApplicationHeadResult.getMiddle();
        val tokensAfterApplicationHead =
                tryReduceApplicationHeadResult.getRight();
        val expectedApplicationNameTokenFirstPositionCharSequence =
                expectedApplicationNameTokenFirstPosition.getIdentifierName();
        val reduceApplicationEntryPointClauseResults =
                reduceApplicationEntryPointClause(tokensAfterApplicationHead);
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
        return new ImmutableTriple<>(
                true,
                new Application(
                        applicationName, entryPointNamedFunctionName),
                remainingTokensAfterApplicationReduction);
    }

    /**  @return A {@link Pair} where the left side holds the name of
     * the application, expected to be found later as well, and for that
     * reason is returned; and the right side holds the reduced list of
     * tokens. */
    public Triple<Boolean, Token, List<Token>> tryReduceApplicationHead(
            final List<Token> tokens) {

        val tokensSize = tokens.size();
        if (tokensSize <= 8) {
            return new ImmutableTriple<>(false, null, null);
        }
        val token0 = tokens.get(0);
        if (token0.getType() != Token.Type.APPLICATION) {
            return new ImmutableTriple<>(false, null, null);
        }
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
        return new ImmutableTriple<>(true, token1, returningRight);
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
