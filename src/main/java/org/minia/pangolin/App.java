package org.minia.pangolin;

import lombok.extern.java.Log;

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

    public static void main(final String[] args) {
        log.info(FIGLET);
    }
}
