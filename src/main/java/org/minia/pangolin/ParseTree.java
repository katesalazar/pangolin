package org.minia.pangolin;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ParseTree {

    @Getter
    private final Program raw;

    @Getter
    private final List<NamedFunction> namedFunctions;

    public ParseTree(final Program program) {
        raw = program;
        namedFunctions = new ArrayList<>(16);
    }
}
