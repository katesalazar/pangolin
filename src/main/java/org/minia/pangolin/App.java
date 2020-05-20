package org.minia.pangolin;

import lombok.extern.java.Log;
import lombok.val;

@Log
public class App 
{
    private static final String FIGLET = "\n" +
            "                               _ _       \n" +
            " _ __   __ _ _ __   __ _  ___ | (_)_ __  \n" +
            "| '_ \\ / _` | '_ \\ / _` |/ _ \\| | | '_ \\ \n" +
            "| |_) | (_| | | | | (_| | (_) | | | | | |\n" +
            "| .__/ \\__,_|_| |_|\\__, |\\___/|_|_|_| |_|\n" +
            "|_|                |___/                 ";

    private static ParseTree parse(final Program program) {
        return new ParseTree(program);
    }

    private static void test0() {
        val document = new Document("comment this is a comment comment ends");
        val program = new Program(document);
        val parseTree = parse(program);
    }

    private static void runTests() {
        test0();
    }

    public static void main(final String[] args) {
        log.info(FIGLET);
        runTests();
    }
}
