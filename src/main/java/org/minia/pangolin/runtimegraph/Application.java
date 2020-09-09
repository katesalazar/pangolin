package org.minia.pangolin.runtimegraph;

import lombok.Getter;
import org.minia.pangolin.parser.LanguageNotRecognizedException;

import java.util.List;

public class Application {

    @Getter private final CharSequence name;

    private final NamedFunction entryPointNamedFunction;

    private Application() {
        super();
        throw new UnsupportedOperationException("FIXME");
    }

    private Application(
            final CharSequence name,
            final NamedFunction entryPointNamedFunction) {
        super();
        this.name = name;
        this.entryPointNamedFunction = entryPointNamedFunction;
    }

    public static Application from(
            final org.minia.pangolin.syntaxtree.Application application,
            final List<NamedFunction> namedFunctions)
                    throws LanguageNotRecognizedException {

        for (final NamedFunction namedFunction: namedFunctions) {
            if (namedFunction.getName().equals(
                    application.getEntryPointFunctionName())) {
                return new Application(
                        application.getApplicationName(), namedFunction);
            }
        }
        throw new LanguageNotRecognizedException("FIXME");
    }

    public void run() {
        entryPointNamedFunction.run();
    }
}
