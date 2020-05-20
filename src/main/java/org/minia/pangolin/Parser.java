package org.minia.pangolin;

import lombok.var;

import static org.minia.pangolin.Util.forceAssert;

public class Parser {

    final Program program;

    public Parser(final Program program) {
        this.program = program;
    }

    public ParseTree parse() {
        synchronized (program) {
            forceAssert(program.getDocuments().size() == 1);
            var remainingStuff = program.getDocuments().get(0).getRaw();
            do {
                if (remainingStuff.toString().startsWith("comment ends")) {
                    remainingStuff = remainingStuff.toString().substring("comment ends".length());
                } else
                if (remainingStuff.toString().startsWith("comment ")) {
                    remainingStuff = remainingStuff.toString().substring("comment ".length());
                } else if (remainingStuff.toString().startsWith("function ")) {
                    remainingStuff = remainingStuff.toString().substring("function ".length());
                } else if (remainingStuff.toString().startsWith(" ")) {
                    remainingStuff = remainingStuff.toString().substring(1);
                } else if (remainingStuff.toString().startsWith("identifier stuff identifier ends")) {
                    remainingStuff = remainingStuff.toString().substring("identifier stuff identifier ends".length());
                } else if (remainingStuff.toString().startsWith("end function identifier stuff identifier ends")) {
                    remainingStuff = remainingStuff.toString().substring("end function identifier stuff identifier ends".length());
                } else {
                    throw new IllegalStateException("FIXME");
                }
            } while (remainingStuff.length() > 0);
        }
        return new ParseTree(program);
    }
}
