package org.minia.pangolin;

import lombok.Getter;

public class Document {

    @Getter
    final CharSequence raw;

    public Document(final CharSequence cs) {
        raw = cs;
    }
}
