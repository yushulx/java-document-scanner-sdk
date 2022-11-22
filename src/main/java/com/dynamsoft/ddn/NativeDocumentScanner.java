package com.dynamsoft.ddn;

import java.util.ArrayList;

public class NativeDocumentScanner {
	
	private long nativePtr = 0;

	static {
		try {
			if (NativeLoader.load()) {
				System.out.println("Successfully loaded Dynamsoft Document Normalizer.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public NativeDocumentScanner() {
		nativePtr = nativeCreateInstance();
	}
	
	public void destroyInstance() {
		if (nativePtr != 0)
			nativeDestroyInstance(nativePtr);
	}
	
	public static int setLicense(String license) {
		return nativeInitLicense(license);
	}
	
	public ArrayList<DocumentResult> detectFile(String fileName) {
		return nativeDetectFile(nativePtr, fileName);
	}

	public String getVersion() {
		return nativeGetVersion();
	}

	public NormalizedImage normalizeFile(String fileName, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		return nativeNormalizeFile(nativePtr, fileName, x1, y1, x2, y2, x3, y3, x4, y4);	
	}

	public int setParameters(String parameters) {
		return nativeSetParameters(nativePtr, parameters);
	}

	public int saveImage(NormalizedImage image, String fileName) {
		return nativeSaveImage(image, fileName);
	}

	private native static int nativeInitLicense(String license);
	
	private native long nativeCreateInstance();
	
	private native void nativeDestroyInstance(long nativePtr);
	
	private native ArrayList<DocumentResult> nativeDetectFile(long nativePtr, String fileName);

	private native String nativeGetVersion();

	private native NormalizedImage nativeNormalizeFile(long nativePtr, String fileName, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4);

	private native int nativeSetParameters(long nativePtr, String parameters);

	private native int nativeSaveImage(NormalizedImage image, String fileName);
}
