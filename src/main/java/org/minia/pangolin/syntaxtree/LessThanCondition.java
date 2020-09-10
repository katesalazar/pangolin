package org.minia.pangolin.syntaxtree;

import org.minia.pangolin.scanner.Token;

public final class LessThanCondition extends Condition {

    public LessThanCondition(
            final Token leftHandSideToken, final Token rightHandSideToken) {
        super(leftHandSideToken, rightHandSideToken);
    }
}
