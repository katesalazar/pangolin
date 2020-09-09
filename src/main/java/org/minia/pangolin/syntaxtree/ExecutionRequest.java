package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public class ExecutionRequest {

    @Getter private final CharSequence
    applicationRequestedToBeExecutedName;

    public ExecutionRequest(
            final CharSequence applicationRequestedToBeExecutedName) {

        this.applicationRequestedToBeExecutedName =
                applicationRequestedToBeExecutedName;
    }
}
