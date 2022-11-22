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
	JNIEXPORT jobject JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeNormalizeFile(JNIEnv *env, jobject, jlong ptr, jstring fileName, jint x1, jint y1, jint x2, jint y2, jint x3, jint y3, jint x4, jint y4)
	{
		if (ptr)
		{

			jclass normalizedImageClass = env->FindClass("com/dynamsoft/ddn/NormalizedImage");
			if (NULL == normalizedImageClass)
				printf("FindClass failed\n");

			jmethodID normalizedImageConstructor = env->GetMethodID(normalizedImageClass, "<init>", "(IIII[BII)V");
			if (NULL == normalizedImageConstructor)
				printf("GetMethodID failed\n");

			const char *pszFileName = env->GetStringUTFChars(fileName, NULL);
			
			void *handler = (void *)ptr;

			Quadrilateral quad;
			quad.points[0].coordinate[0] = x1;
			quad.points[0].coordinate[1] = y1;
			quad.points[1].coordinate[0] = x2;
			quad.points[1].coordinate[1] = y2;
			quad.points[2].coordinate[0] = x3;
			quad.points[2].coordinate[1] = y3;
			quad.points[3].coordinate[0] = x4;
			quad.points[3].coordinate[1] = y4;

			NormalizedImageResult* normalizedResult = NULL;

			int errorCode = DDN_NormalizeFile(handler, pszFileName, "", &quad, &normalizedResult);
			if (errorCode != DM_OK)
				printf("%s\r\n", DC_GetErrorString(errorCode));

			ImageData *imageData = normalizedResult->image;
			
			int width = imageData->width;
			int height = imageData->height;
			int stride = imageData->stride;
			int format = (int)imageData->format;
			unsigned char* data = imageData->bytes;
			int orientation = imageData->orientation;
			int length = imageData->bytesLength;

			jbyteArray byteArray = env->NewByteArray(length);
			env->SetByteArrayRegion(byteArray, 0, length, (jbyte *)data);
			jobject object = env->NewObject(normalizedImageClass, normalizedImageConstructor, width, height, stride, format, byteArray, orientation, length);
			env->ReleaseStringUTFChars(fileName, pszFileName);

			if (normalizedResult != NULL)
				DDN_FreeNormalizedImageResult(&normalizedResult);

			return object;
		}
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
	* Signature: (Lcom/dynamsoft/ddn/NormalizedImage;Ljava/lang/String;)I
	*/
	JNIEXPORT jint JNICALL Java_com_dynamsoft_ddn_NativeDocumentScanner_nativeSaveImage(JNIEnv *env, jobject, jobject obj, jstring fileName)
	{
		jclass normalizedImageClass = env->FindClass("com/dynamsoft/ddn/NormalizedImage");
		if (NULL == normalizedImageClass)
			printf("FindClass failed\n");

		jfieldID fid = env->GetFieldID(normalizedImageClass, "width", "I");
		if (NULL == fid)
			printf("Get width failed\n");

		jint width = env->GetIntField(obj, fid);

		fid = env->GetFieldID(normalizedImageClass, "height", "I");
		if (NULL == fid)
			printf("Ge height failed\n");

		jint height = env->GetIntField(obj, fid);

		fid = env->GetFieldID(normalizedImageClass, "stride", "I");
		if (NULL == fid)
			printf("Get stride failed\n");

		jint stride = env->GetIntField(obj, fid);

		fid = env->GetFieldID(normalizedImageClass, "format", "I");
		if (NULL == fid)
			printf("Get format failed\n");

		jint format = env->GetIntField(obj, fid);

		fid = env->GetFieldID(normalizedImageClass, "data", "[B");
		if (NULL == fid)
			printf("Get data failed\n");

		jbyteArray byteArray = (jbyteArray)env->GetObjectField(obj, fid);
		jbyte *bytes = env->GetByteArrayElements(byteArray, NULL);

		fid = env->GetFieldID(normalizedImageClass, "orientation", "I");
		if (NULL == fid)
			printf("Get orientation failed\n");

		jint orientation = env->GetIntField(obj, fid);

		fid = env->GetFieldID(normalizedImageClass, "length", "I");
		if (NULL == fid)
			printf("Get length failed\n");

		jint length = env->GetIntField(obj, fid);

		ImageData data;
		data.bytes = (unsigned char *)bytes;
		data.width = width;
		data.height = height;
		data.stride = stride;
		data.format = (ImagePixelFormat)format;
		data.orientation = orientation;
		data.bytesLength = length;

		const char *pszFileName = env->GetStringUTFChars(fileName, NULL);

		NormalizedImageResult normalizedResult;
		normalizedResult.image = &data;
		int ret = NormalizedImageResult_SaveToFile(&normalizedResult, pszFileName);
		if (ret != DM_OK)
			printf("NormalizedImageResult_SaveToFile: %s\r\n", DC_GetErrorString(ret));

		env->ReleaseStringUTFChars(fileName, pszFileName);
		env->ReleaseByteArrayElements(byteArray, bytes, 0);

		return ret;
	}
	
#ifdef __cplusplus
}
#endif