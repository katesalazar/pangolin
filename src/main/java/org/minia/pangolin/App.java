package org.minia.pangolin;

import lombok.extern.java.Log;
import lombok.val;
import org.minia.pangolin.parser.LanguageNotRecognizedException;
import org.minia.pangolin.parser.ParseTree;
import org.minia.pangolin.parser.Parser;
import org.minia.pangolin.runtimegraph.Application;
import org.minia.pangolin.runtimegraph.ExecutionRequest;
import org.minia.pangolin.runtimegraph.NamedFunction;
import org.minia.pangolin.semantics.UnboundIdentifierException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.minia.pangolin.util.Util.forcedAssertion;

@Log
public class App {

    /* XXX */
    private static final String HELLO_WORLD = "hello, world";

    private static final String FIGLET_THREEPOINT = "\n" +
            " _  _  _  _  _ |. _\n" +
            "|_)(_|| |(_|(_)||| |\n" +
            "|         _|\n";

    private static boolean canPrintBanner(final String[] args) {
        for (val arg: args) {
            if ("--no-banner".equals(arg)) {
                return false;
            }
        }
        return true;
    }

    App() {
        super();
    }

    boolean runApp(final String app)
            throws LanguageNotRecognizedException {

        val pathToMain = app + "/main.minia";
        FileReader fileReader;
        try {
            fileReader = new FileReader(pathToMain);
        } catch (FileNotFoundException fnfe) {
            log.severe(fnfe.toString());
            return false;
        }

        String line;
        val lines = new StringBuilder(1 << 12);
        try (val bufferedReader = new BufferedReader(fileReader)) {
            while (null != (line = bufferedReader.readLine())) {
                lines.append(line).append('\n');
            }
        } catch (IOException ioe) {
            log.severe(ioe.toString());
            return false;
        }
        val document = new Document(lines);
        val program = new Program(document);
        val parser = new Parser(program);
        val parsedTrees = parser.parse();

        final List<NamedFunction> namedFunctions = new ArrayList<>(16);
        final List<Application> applications = new ArrayList<>(16);
        final List<ExecutionRequest> executionRequests =
                new ArrayList<>(16);

        for (final ParseTree parseTree : parsedTrees) {
            if (ParseTree.Type.NAMED_FUNCTION == parseTree.getType()) {
                try {
                    namedFunctions.add(
                            parseTree.namedFunctionForRunTimeGraph());
                } catch (final UnboundIdentifierException uie) {
                    log.severe(uie.getMessage());
                    return false;
                }
            } else if (ParseTree.Type.APPLICATION ==
                    parseTree.getType()) {
                applications.add(parseTree.applicationForRunTimeGraph(
                        namedFunctions));
            } else {
                forcedAssertion(ParseTree.Type.EXECUTION_REQUEST ==
                        parseTree.getType());
                executionRequests.add(
                        parseTree.executionRequestForRunTimeGraph(
                                applications));
            }
        }

        for (final ExecutionRequest executionRequest:
                executionRequests) {
            executionRequest.run();
        }
        return true;  // XXX
    }

    @SuppressWarnings("java:S106") /* Do not use `System.out`. */
    private static void print() {
        System.out.println((CharSequence) HELLO_WORLD);
    }

    /**  <p>`@SuppressWarnings("java:S106")` will suppress the warning
     * by which a standard output print is adviced to be turned to a
     * logging call. Here the standard output print is on purpose.
     *   @return A boolean `true` value, for this should not fail. */
    @SuppressWarnings("java:S106") /* Do not use `System.out`. */
    private static boolean greet() {
        System.out.println("Wish you have a wonderful day!");
        return true;
    }

    /**  <p>Receives the `args` from {@link #main} and processes.
     *   @param args They come from {@link #main}.
     *   @return Boolean `true` or `false` whether it runs with no error
     * or an error happened, respectively. */
    private boolean process(final String[] args) {

        for (int i = 0; i < args.length; i++) {
            if ("run".equals(args[i]) &&
                    i < args.length - 2 &&
                    "app".equals(args[i + 1])) {
                try {
                    return runApp(args[i + 2]);
                } catch (final LanguageNotRecognizedException lnre) {
                    log.severe(lnre.getMessage());
                }
            }
            if ("greet".equals(args[i])) {
                return greet();
            }
        }
        return false;
    }

    /**  <p>Proxy `main`. Proxies {@link #main}. */
    public int fakeableMain(final String[] args) {

        if (canPrintBanner(args)) {
            log.info(FIGLET_THREEPOINT);
        }
        if (process(args)) {
            return 0;
        }
        return 1;
    }

    /**  <p>True `main`. Uses {@link #fakeableMain} as a tests-friendly
     * proxy. */
    public static void main(final String[] args) {

        val exitStatus = new App().fakeableMain(args);
        if (exitStatus == 1) {
            System.exit(exitStatus);
        }
    }
}
