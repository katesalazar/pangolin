package org.minia.pangolin.runtimegraph;

import org.minia.pangolin.parser.LanguageNotRecognizedException;

import java.util.List;

public class ExecutionRequest {

    private final Application application;

    private ExecutionRequest() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    private ExecutionRequest(final Application application) {
        super();
        this.application = application;
    }

    public static ExecutionRequest from(
            final org.minia.pangolin.syntaxtree.ExecutionRequest executionRequest,
            final List<Application> applications)
                    throws LanguageNotRecognizedException {

        for (final Application application: applications) {
            if (application != null && application.getName() != null &&
                    application.getName().equals(
                            executionRequest.getApplicationRequestedToBeExecutedName())) {
                return new ExecutionRequest(application);
            }
        }
        throw new LanguageNotRecognizedException("FIXME");
    }

    public void run() {
        application.run();
    }
}
