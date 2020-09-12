package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public class ExecutionRequest implements TopLevelNode {

    @Getter private final CharSequence
    applicationRequestedToBeExecutedName;

    public ExecutionRequest(
            final CharSequence applicationRequestedToBeExecutedName) {

        this.applicationRequestedToBeExecutedName =
                applicationRequestedToBeExecutedName;
    }
}
