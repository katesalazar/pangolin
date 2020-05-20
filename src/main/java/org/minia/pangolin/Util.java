package org.minia.pangolin;

public class Util {

    public static void forceAssert(boolean b) {
        if (!b) {
            throw new AssertionError("assertion failed");
        }
    }
}
