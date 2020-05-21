package org.minia.pangolin;

public class Util {

    Util() { super(); }

    public static void forceAssert(boolean b) {
        if (!b) {
            throw new AssertionError("assertion failed");
        }
    }
}
