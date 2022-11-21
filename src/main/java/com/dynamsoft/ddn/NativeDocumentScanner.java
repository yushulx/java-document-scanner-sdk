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

	private native static int nativeInitLicense(String license);
	
	private native long nativeCreateInstance();
	
	private native void nativeDestroyInstance(long nativePtr);
	
	private native ArrayList<DocumentResult> nativeDetectFile(long nativePtr, String fileName);

	private native String nativeGetVersion();
}
