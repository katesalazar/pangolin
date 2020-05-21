package org.minia.pangolin;

import lombok.extern.java.Log;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@Log
public class App {

    private static final String FIGLET_DEFAULT = "\n" +
            "                               _ _       \n" +
            " _ __   __ _ _ __   __ _  ___ | (_)_ __  \n" +
            "| '_ \\ / _` | '_ \\ / _` |/ _ \\| | | '_ \\ \n" +
            "| |_) | (_| | | | | (_| | (_) | | | | | |\n" +
            "| .__/ \\__,_|_| |_|\\__, |\\___/|_|_|_| |_|\n" +
            "|_|                |___/                 ";

    private static final String FIGLET_THREEPOINT = "\n" +
            " _  _  _  _  _ |. _\n" +
            "|_)(_|| |(_|(_)||| |\n" +
            "|         _|\n";

    public static void main(final String[] args) {
        log.info(FIGLET_THREEPOINT);

        // REMOVE THIS LATER, it is used to ensure access to classes is NOT working
        Pair tuple = new ImmutablePair(0,0);
        System.out.println(tuple);
    }
}
