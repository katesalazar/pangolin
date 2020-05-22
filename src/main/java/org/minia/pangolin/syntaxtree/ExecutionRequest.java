package org.minia.pangolin.syntaxtree;

public class ExecutionRequest {

    private final CharSequence applicationRequestedToBeExecuted;

    public ExecutionRequest(
            final CharSequence applicationRequestedToBeExecuted) {

        this.applicationRequestedToBeExecuted =
                applicationRequestedToBeExecuted;
    }
}
