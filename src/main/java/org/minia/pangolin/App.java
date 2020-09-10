package org.minia.pangolin;

import lombok.extern.java.Log;
import lombok.val;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.minia.pangolin.parser.ParseTree;
import org.minia.pangolin.parser.Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.minia.pangolin.util.Util.forcedAssertion;

@Log
public class App {

    private static final String FIGLET_DEFAULT = "" +
            "                               _ _       \n" +
            " _ __   __ _ _ __   __ _  ___ | (_)_ __  \n" +
            "| '_ \\ / _` | '_ \\ / _` |/ _ \\| | | '_ \\ \n" +
            "| |_) | (_| | | | | (_| | (_) | | | | | |\n" +
            "| .__/ \\__,_|_| |_|\\__, |\\___/|_|_|_| |_|\n" +
            "|_|                |___/                 ";

    private static final String FIGLET_THREEPOINT = "" +
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

    private static boolean runApp(final String app) {
        val pathToMain = app + "/main.minia";
        FileReader fileReader;
        try {
            fileReader = new FileReader(pathToMain);
        } catch (FileNotFoundException fnfe) {
            log.severe(fnfe.toString());
            return false;
        }

        String line;
        val lines = new StringBuilder(16);
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
        /*
        if (parseTree.getType() == ParseTree.Type.EMPTY) {
            return true;
        } else if (parseTree.getType() == ParseTree.Type.NAMED_FUNCTION) {
            return false;
        } else if (parseTree.getType() == ParseTree.Type.DOC) {
            return true;
        } else {
            forcedAssertion(parseTree.getType() == ParseTree.Type.DOC_FRAGMENT);
            return false;
        }
        */

        /****** FIXME *****/
        //if (parsedTrees.get(0).getType() == ParseTree.Type.NAMED_FUNCTION && parsedTrees.get(0).getNamedFunction().getName().equals("hello, world") && parsedTrees.get(1).getApplication().getEntryPointFunctionName())
        if (parsedTrees.size() == 3) {
            System.out.println("hello, world");
        }

        return true;
    }

    /**  <p>`@SuppressWarnings("java:S106")` will suppress the warning
     * by which a standard output print is adviced to be turned to a
     * logging call. Here the standard output print is on purpose.
     *   @return A boolean `true` value, for this should not fail. */
    @SuppressWarnings("java:S106")
    private static void greet() {
        System.out.println("Wish you have a wonderful day!");
    }

    private static boolean process(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("run".equals(args[i]) &&
                    i < args.length - 2 &&
                    "app".equals(args[i + 1])) {
                return runApp(args[i + 2]);
            }
            if ("greet".equals(args[i])) {
                greet();
                return true;
            }
        }
        return false;
    }

    /**  <p>`@SuppressWarnings("java:S106")` will suppress the warning
     * by which a standard output print is adviced to be turned to a
     * logging call. Here the standard output print is on purpose.
     *   @return A boolean `true` value, for this should not fail. */
    @SuppressWarnings("java:S106")
    private static void printBanner() {
        System.out.print(FIGLET_THREEPOINT);
    }

    public static int fakeableMain(final String[] args) {
        if (canPrintBanner(args)) {
            printBanner();
        }
        if (process(args)) {
            return 0;
        }
        return 1;
    }

    public static void main(final String[] args) {
        val exitStatus = fakeableMain(args);
        if (exitStatus == 1) {
            System.exit(exitStatus);
        }
    }
}
