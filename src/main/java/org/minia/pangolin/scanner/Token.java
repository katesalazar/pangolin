package org.minia.pangolin.scanner;

import lombok.Getter;

import static org.minia.pangolin.util.Util.forcedAssertion;

public class Token {

    /**  Lower case `a` word, as a {@link String}. */
    static final String LC_A = "a";

    /**  Lower case `and` word, as a {@link String}. */
    static final String LC_AND = "and";

    /**  Lower case `all` word, as a {@link String}. */
    static final String LC_ALL = "all";

    /**  Lower case `application` word, as a {@link String}. */
    static final String LC_APPLICATION = "application";

    /**  Lower case `at` word, as a {@link String}. */
    static final String LC_AT = "at";

    /**  Lower case `causes` word, as a {@link String}. */
    static final String LC_CAUSES = "causes";

    /**  Lower case `command` word, as a {@link String}. */
    static final String LC_COMMAND = "command";

    /**  Lower case `does` word, as a {@link String}. */
    static final String LC_DOES = "does";

    /**  Lower case `effects` word, as a {@link String}. */
    static final String LC_EFFECTS = "effects";

    /**  Lower case `end` word, as a {@link String}. */
    static final String LC_END = "end";

    /**  Lower case `entry` word, as a {@link String}. */
    static final String LC_ENTRY = "entry";

    /**  Lower case `function` word, as a {@link String}. */
    static final String LC_FUNCTION = "function";

    /**  Lower case `interface` word, as a {@link String}. */
    static final String LC_INTERFACE = "interface";

    /**  Lower case `is` word, as a {@link String}. */
    static final String LC_IS = "is";

    /**  Lower case `it` word, as a {@link String}. */
    static final String LC_IT = "it";

    /**  Lower case `line` word, as a {@link String}. */
    static final String LC_LINE = "line";

    /**  Lower case `new` word, as a {@link String}. */
    static final String LC_NEW = "new";

    /**  Lower case `nothing` word, as a {@link String}. */
    static final String LC_NOTHING = "nothing";

    /**  Lower case `print` word, as a {@link String}. */
    static final String LC_PRINT = "print";

    /**  Lower case `point` word, as a {@link String}. */
    static final String LC_POINT = "point";

    /**  Lower case `receives` word, as a {@link String}. */
    static final String LC_RECEIVES = "receives";

    /**  Lower case `returns` word, as a {@link String}. */
    static final String LC_RETURNS = "returns";

    /**  Lower case `run` word, as a {@link String}. */
    static final String LC_RUN = "run";

    /**  Lower case `side` word, as a {@link String}. */
    static final String LC_SIDE = "side";

    /**  Lower case `so` word, as a {@link String}. */
    static final String LC_SO = "so";

    /**  Lower case `the` word, as a {@link String}. */
    static final String LC_THE = "the";

    /**  Lower case `then` word, as a {@link String}. */
    static final String LC_THEN = "then";

    public enum Type {
        A, ALL, AND, APPLICATION, AT, CAUSES, COMMAND, DOES, EFFECTS,
        END, ENTRY, FUNCTION, IDENTIFIER, INTERFACE, IS, IT, LINE, NEW,
        NOTHING, POINT, PRINT, RECEIVES, RETURNS, RUN, SIDE, SO,
        STRING_LITERAL, THE, THEN
    }

    @Getter
    private final Type type;

    /**  <p>If this {@link Token} is an identifier (i.e. it is of the
     * `IDENTIFIER` type), then this holds the name of that identifier,
     * i.e. the identifier itself in the sense of typical programming
     * languages. */
    @Getter private final CharSequence identifierName;

    /**  <p>If this {@link Token} is a string literal (i.e. it is of the
     * `STRING_LITERAL` type), then this holds the contents of that
     * string literal. */
    private final CharSequence stringLiteralContent;

    /**  General {@link Token} constructor. */
    public Token(final Type type) {
        this.type = type;
        identifierName = null;
        stringLiteralContent = null;
    }

    /**  Particular {@link Token} constructor when the {@link
     *  Token.Type} of the {@link Token} is `IDENTIFIER` or
     *  `STRING_LITERAL``. */
    public Token(final Type type, final CharSequence cs) {
        if (type != Type.IDENTIFIER) {
            forcedAssertion(type == Type.STRING_LITERAL);
        }
        this.type = type;
        if (type == Type.IDENTIFIER) {
            identifierName = cs;
            stringLiteralContent = null;
        } else {
            identifierName = null;
            stringLiteralContent = cs;
        }
    }

    public boolean notAnIdentifier() {
        return type != Type.IDENTIFIER;
    }

    public static String stringFor(final Token token) {
        switch (token.type) {
            case A:
                return LC_A;
            case ALL:
                return LC_ALL;
            case AND:
                return LC_AND;
            case APPLICATION:
                return LC_APPLICATION;
            case AT:
                return LC_AT;
            case CAUSES:
                return LC_CAUSES;
            case COMMAND:
                return LC_COMMAND;
            case DOES:
                return LC_DOES;
            case EFFECTS:
                return LC_EFFECTS;
            case END:
                return LC_END;
            case ENTRY:
                return LC_ENTRY;
            case FUNCTION:
                return LC_FUNCTION;
            case INTERFACE:
                return LC_INTERFACE;
            case IS:
                return LC_IS;
            case IT:
                return LC_IT;
            case LINE:
                return LC_LINE;
            case NEW:
                return LC_NEW;
            case NOTHING:
                return LC_NOTHING;
            case POINT:
                return LC_POINT;
            case PRINT:
                return LC_PRINT;
            case RECEIVES:
                return LC_RECEIVES;
            case RETURNS:
                return LC_RETURNS;
            case RUN:
                return LC_RUN;
            case SIDE:
                return LC_SIDE;
            case SO:
                return LC_SO;
            case THE:
                return LC_THE;
            case THEN:
                return LC_THEN;
            default:
                throw new IllegalArgumentException("FIXME");
        }
    }
}
