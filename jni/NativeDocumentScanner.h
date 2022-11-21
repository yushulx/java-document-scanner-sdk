/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_dynamsoft_ddn_NativeDocumentScanner */

#ifndef _Included_com_dynamsoft_ddn_NativeDocumentScanner
#define _Included_com_dynamsoft_ddn_NativeDocumentScanner
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_dynamsoft_ddn_NativeDocumentScanner
 * Method:    nativeInitLicense
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeInitLicense
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_dynamsoft_ddn_NativeDocumentScanner
 * Method:    nativeCreateInstance
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeCreateInstance
  (JNIEnv *, jobject);

/*
 * Class:     com_dynamsoft_ddn_NativeDocumentScanner
 * Method:    nativeDestroyInstance
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeDestroyInstance
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_dynamsoft_ddn_NativeDocumentScanner
 * Method:    nativeDetectFile
 * Signature: (JLjava/lang/String;)Ljava/util/ArrayList;
 */
JNIEXPORT jobject JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeDetectFile
  (JNIEnv *, jobject, jlong, jstring);

/*
 * Class:     com_dynamsoft_ddn_NativeDocumentScanner
 * Method:    nativeGetVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeGetVersion
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
