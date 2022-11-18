package com.dynamsoft.ddn;

public class NativeDocumentScanner {
	
	private long nativePtr = 0;

	static {
		try {
			if (NativeLoader.load()) {
				System.out.println("Successfully loaded Dynamsoft Barcode Reader.");
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
		nativeInitLicense(license);
	}
	
	public void detectFile(String fileName) {
		nativeDetectFile(nativePtr, fileName);
	}

	public String getVersion() {
		return nativeGetVersion();
	}

	private native static int nativeInitLicense(long nativePtr, String license);
	
	private native long nativeCreateInstance();
	
	private native void nativeDestroyInstance(long nativePtr);
	
	private native void nativeDetectFile(long nativePtr, String fileName);

	private native String nativeGetVersion();
}
