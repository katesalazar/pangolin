package org.minia.pangolin;

import lombok.Getter;

public class ParseTree {

    enum Type { EMPTY,  NAMED_FUNCTION }

    @Getter
    private final Program program;

    @Getter
    private final Type type;

    public ParseTree(final Program program) {
        this.program = program;
        type = null;
    }

    public ParseTree(final Program program, final Type type) {
        this.program = program;
        this.type = type;
    }
}
