package org.minia.pangolin;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Program {

    @Getter
    private final List<Document> documents;

    public Program(final Document document) {
        documents = new ArrayList<>(1);
        documents.add(document);
    }
}
