package org.minia.pangolin;

import lombok.Getter;

public class Document {

    @Getter
    private final CharSequence raw;

    public Document(final CharSequence raw) {
        this.raw = raw;
    }
}
