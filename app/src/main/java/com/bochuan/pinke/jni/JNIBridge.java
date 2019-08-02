package com.bochuan.pinke.jni;

public class JNIBridge {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String testJni();

}
