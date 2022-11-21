#include "NativeDocumentScanner.h"
#include "DynamsoftCore.h"
#include "DynamsoftDocumentNormalizer.h"

#ifdef __cplusplus
extern "C"
{
#endif

	/*
	* Class:     com_dynamsoft_ddn_NativeDocumentScanner
	* Method:    nativeInitLicense
	* Signature: (JLjava/lang/String;)I
	*/
	JNIEXPORT jint JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeInitLicense(JNIEnv *env, jclass, jstring license)
	{
		const char *pszLicense = env->GetStringUTFChars(license, NULL);
		char errorMsgBuffer[512];
		// Click https://www.dynamsoft.com/customer/license/trialLicense/?product=ddn to get a trial license.
		int ret = DC_InitLicense(pszLicense, errorMsgBuffer, 512);
		env->ReleaseStringUTFChars(license, pszLicense);
		return ret;
	}

	/*
	* Class:     com_dynamsoft_ddn_NativeDocumentScanner
	* Method:    nativeCreateInstance
	* Signature: (J)V
	*/
	JNIEXPORT jlong JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeCreateInstance(JNIEnv *, jobject)
	{
		return (jlong)DDN_CreateInstance();
	}

	/*
	* Class:     com_dynamsoft_ddn_NativeDocumentScanner
	* Method:    nativeDestroyInstance
	* Signature: (J)V
	*/
	JNIEXPORT void JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeDestroyInstance(JNIEnv *, jobject, jlong handler)
	{
		if (handler)
		{
			DDN_DestroyInstance((void *)handler);
		}
	}

	/*
	* Class:     com_dynamsoft_ddn_NativeDocumentScanner
	* Method:    nativeDetectFile
	* Signature: (JLjava/lang/String;)V
	*/
	JNIEXPORT void JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeDetectFile(JNIEnv *env, jobject, jlong ptr, jstring fileName)
	{
		if (ptr)
		{
			void *handler = (void *)ptr;
			const char *pszFileName = env->GetStringUTFChars(fileName, NULL);
			printf("Detecting %s", pszFileName);

			// DBR_DecodeFile(handler, pszFileName, "");

			// TextResultArray *paryResult = NULL;
			// DBR_GetAllTextResults(handler, &paryResult);

			// int count = paryResult->resultsCount;
			// for (int index = 0; index < paryResult->resultsCount; index++)
			// {
			// 	printf("Barcode %d:\n", index + 1);
			// 	printf("    Type: %s\n", paryResult->results[index]->barcodeFormatString);
			// 	printf("    Text: %s\n", paryResult->results[index]->barcodeText);
			// }

			// // Release memory
			// DBR_FreeTextResults(&paryResult);

			env->ReleaseStringUTFChars(fileName, pszFileName);
		}
	}

	/*
	 * Class:     com_dynamsoft_ddn_NativeDocumentScanner
	 * Method:    nativeGetVersion
	 * Signature: ()Ljava/lang/String;
	 */
	JNIEXPORT jstring JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeGetVersion(JNIEnv *env, jobject) 
	{
		const char *version = DDN_GetVersion();
		return env->NewStringUTF(version);
	}

#ifdef __cplusplus
}
#endif