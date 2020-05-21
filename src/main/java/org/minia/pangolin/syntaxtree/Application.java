package org.minia.pangolin.syntaxtree;

public class Application {

    private final CharSequence applicationName;

    private final CharSequence entryPointFunctionName;

    public Application(
            final CharSequence applicationName,
            final CharSequence entryPointFunctionName) {

        this.applicationName = applicationName;
        this.entryPointFunctionName = entryPointFunctionName;
    }
}
