package org.minia.pangolin;

import lombok.Getter;

import static org.minia.pangolin.Util.forcedAssertion;

public class Token {

    public enum Type { END, FUNCTION, IDENTIFIER }

    @Getter
    private final Type type;

    @Getter
    private final CharSequence identifierName;

    public Token(final Type type) {
        this.type = type;
        this.identifierName = null;
    }

    public Token(final Type type, final CharSequence identifierName) {
        forcedAssertion(type == Type.IDENTIFIER);
        this.type = type;
        this.identifierName = identifierName;
    }

    public static Token newEndToken() {
        return new Token(Token.Type.END);
    }

    public static Token newFunctionToken() {
        return new Token(Token.Type.FUNCTION);
    }

    public static Token newIdentifierToken(final CharSequence identifierName) {
        return new Token(Token.Type.IDENTIFIER, identifierName);
    }

    public boolean notAnIdentifier() {
        return type != Type.IDENTIFIER;
    }
}
