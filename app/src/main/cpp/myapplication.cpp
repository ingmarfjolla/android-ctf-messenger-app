// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("myapplication");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("myapplication")
//      }
//    }

#include <jni.h>
#include "secrets/secret.h"
#include "secrets/password.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_myapplication_LoginActivity_getNativeKeyPart(JNIEnv *env,
                                                                                 jobject thiz) {
    return env->NewStringUTF(getString());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_myapplication_LoginActivity_getNativePassword(JNIEnv *env,
                                                                     jobject thiz) {
    return env->NewStringUTF(getPassword());
}