package org.minia.pangolin.syntaxtree;

import lombok.Getter;

public class Application {

    @Getter private final CharSequence applicationName;

    @Getter private final CharSequence entryPointFunctionName;

    public Application(
            final CharSequence applicationName,
            final CharSequence entryPointFunctionName) {

        this.applicationName = applicationName;
        this.entryPointFunctionName = entryPointFunctionName;
    }
}
