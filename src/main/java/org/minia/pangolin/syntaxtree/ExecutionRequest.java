package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public class ExecutionRequest {

    @Getter
    private final CharSequence applicationRequestedToBeExecuted;

    public ExecutionRequest(
            final CharSequence applicationRequestedToBeExecuted) {

        this.applicationRequestedToBeExecuted =
                applicationRequestedToBeExecuted;
    }
}
