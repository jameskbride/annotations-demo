package com.jameskbride.annotations.model;

import javax.tools.Diagnostic;

public class Validation {
    private final String message;
    private Diagnostic.Kind kind;

    public Validation(Diagnostic.Kind kind, String message) {
        this.kind = kind;
        this.message = message;
    }

    public Diagnostic.Kind getKind() {
        return kind;
    }

    public String getMessage() {
        return message;
    }
}
