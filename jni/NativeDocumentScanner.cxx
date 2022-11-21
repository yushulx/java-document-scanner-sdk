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
		printf("DC_InitLicense: %s\n", errorMsgBuffer);
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
	JNIEXPORT jobject JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeDetectFile(JNIEnv *env, jobject, jlong ptr, jstring fileName)
	{
		jobject arrayList = NULL;
		if (ptr)
		{
			jclass documentResultClass = env->FindClass("com/dynamsoft/ddn/DocumentResult");
			if (NULL == documentResultClass)
				printf("FindClass failed\n");

			jmethodID documentResultConstructor = env->GetMethodID(documentResultClass, "<init>", "(IIIIIIIII)V");
			if (NULL == documentResultConstructor)
				printf("GetMethodID failed\n");

			jclass arrayListClass = env->FindClass("java/util/ArrayList");
			if (NULL == arrayListClass)
				printf("FindClass failed\n");

			jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
			if (NULL == arrayListConstructor)
				printf("GetMethodID failed\n");

			jmethodID arrayListAdd = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
			if (NULL == arrayListAdd)
				printf("GetMethodID failed\n");

			void *handler = (void *)ptr;
			const char *pszFileName = env->GetStringUTFChars(fileName, NULL);
			printf("Detecting %s", pszFileName);

			DetectedQuadResultArray *pResults = NULL;
    
			int ret = DDN_DetectQuadFromFile(handler, pszFileName, "", &pResults);
			if (ret)
			{
				printf("Detection error: %s\n", DC_GetErrorString(ret));
			}

			if (pResults)
			{
				int count = pResults->resultsCount;
				arrayList = env->NewObject(arrayListClass, arrayListConstructor);
			
				for (int i = 0; i < count; i++)
				{
					DetectedQuadResult *quadResult = pResults->detectedQuadResults[i];
					int confidence = quadResult->confidenceAsDocumentBoundary;
					DM_Point *points = quadResult->location->points;
					int x1 = points[0].coordinate[0];
					int y1 = points[0].coordinate[1];
					int x2 = points[1].coordinate[0];
					int y2 = points[1].coordinate[1];
					int x3 = points[2].coordinate[0];
					int y3 = points[2].coordinate[1];
					int x4 = points[3].coordinate[0];
					int y4 = points[3].coordinate[1];
					
					jobject object = env->NewObject(documentResultClass, documentResultConstructor, confidence, x1, y1, x2, y2, x3, y3, x4, y4);

					env->CallBooleanMethod(arrayList, arrayListAdd, object);
				}
			}

			if (pResults != NULL)
        		DDN_FreeDetectedQuadResultArray(&pResults);

			env->ReleaseStringUTFChars(fileName, pszFileName);
		}

		return arrayList;
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

	/*
	* Class:     com_dynamsoft_ddn_NativeDocumentScanner
	* Method:    nativeNormalizeFile
	* Signature: (JLjava/lang/String;)Lcom/dynamsoft/ddn/NormalizedImage;
	*/
	JNIEXPORT jobject JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeNormalizeFile(JNIEnv *, jobject, jlong, jstring)
	{
		return NULL;
	}

	/*
	* Class:     com_dynamsoft_ddn_NativeDocumentScanner
	* Method:    nativeSetParameters
	* Signature: (JLjava/lang/String;)I
	*/
	JNIEXPORT jint JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeSetParameters(JNIEnv *env, jobject, jlong ptr, jstring parameters)
	{
		if (ptr)
		{
			void *handler = (void *)ptr;
			const char *params = env->GetStringUTFChars(parameters, NULL);
			char errorMsgBuffer[512];
			int ret = DDN_InitRuntimeSettingsFromString(handler, params, errorMsgBuffer, 512);
			printf("Init runtime settings: %s\n", errorMsgBuffer);

			env->ReleaseStringUTFChars(parameters, params);
			return ret;
		}
		
		return -1;
	}

	/*
	* Class:     com_dynamsoft_ddn_NativeDocumentScanner
	* Method:    nativeSaveImage
	* Signature: (JLcom/dynamsoft/ddn/NormalizedImage;Ljava/lang/String;)I
	*/
	JNIEXPORT jint JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeSaveImage(JNIEnv *, jobject, jlong, jobject, jstring)
	{
		return 0;
	}

	/*
	* Class:     com_dynamsoft_ddn_NativeDocumentScanner
	* Method:    nativeFreeImage
	* Signature: (JLcom/dynamsoft/ddn/NormalizedImage;)V
	*/
	JNIEXPORT void JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeFreeImage(JNIEnv *, jobject, jlong, jobject)
	{
	}
	
#ifdef __cplusplus
}
#endif