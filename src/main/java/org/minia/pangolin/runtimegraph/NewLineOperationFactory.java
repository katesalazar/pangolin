package org.minia.pangolin.runtimegraph;

public class NewLineOperationFactory {

    NewLineOperationFactory() {
        super();
    }

    @SuppressWarnings({"java:S1172"}) // "parameter never used"
    public NewLineOperation from(
            final org.minia.pangolin.syntaxtree.NewLineOperation newLineOperation) {

        return new NewLineOperation();
    }
}
