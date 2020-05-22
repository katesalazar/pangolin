package org.minia.pangolin.util;

public class Util {

    Util() { super(); }

    public static void forceAssert(final boolean b) {
        if (!b) {
            throw new AssertionError("assertion failed");
        }
    }

    public static void forcedAssertion(final boolean b) {
        forceAssert(b);
    }
}
