package org.minia.pangolin;

import lombok.extern.java.Log;
import lombok.val;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

@Log
public class App {

    private static final String FIGLET = "\n" +
            "                               _ _       \n" +
            " _ __   __ _ _ __   __ _  ___ | (_)_ __  \n" +
            "| '_ \\ / _` | '_ \\ / _` |/ _ \\| | | '_ \\ \n" +
            "| |_) | (_| | | | | (_| | (_) | | | | | |\n" +
            "| .__/ \\__,_|_| |_|\\__, |\\___/|_|_|_| |_|\n" +
            "|_|                |___/                 ";

    private static boolean noBanner(final String[] args) {
        for (val arg: args) {
            if ("--no-banner".equals(arg)) {
                System.out.println("I am here");
                return true;
            }
        }
        System.out.println("I am here");
        return false;
    }

    private static boolean runApp(final String app) {
        val pathToMain = app + "/main.minia";
        FileReader fileReader;
        try {
            fileReader = new FileReader(pathToMain);
        } catch (FileNotFoundException fnfe) {
            log.log(Level.ALL, fnfe.toString());
            System.err.println(fnfe);
            System.out.println("I am here");
            return false;
        }
        val bufferedReader = new BufferedReader(fileReader);
        String line;
        val lines = new StringBuilder(16);
        try {
            while (null != (line = bufferedReader.readLine())) {
                lines.append(line);
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
            System.out.println("I am here");
            return false;
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ignore) {}
        }
        val document = new Document(lines);
        val program = new Program(document);
        val parser = new Parser(program);
        ParseTree parseTree = parser.parse();
        System.out.println("I am here");
        return false;  /* FIXME */
    }

    private static boolean process(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("run".equals(args[i]) &&
                    i < args.length - 2 &&
                    "app".equals(args[i + 1])) {
                return runApp(args[i + 2]);
            }
        }
        return false;
    }

    public static void main(final String[] args) {
        if (noBanner(args)) {
            log.info(FIGLET);
        }
        if (!process(args)) {
            //System.exit(1); FIXME would break sonarcloud deployment I think
        }
    }
}
