package com.dynamsoft.ddn;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NativeLoader {
	
	
	private static synchronized boolean loadNativeLibrary(String path, String name) {
		if (name.contains("DynamsoftCorex") || name.contains("DynamsoftDocumentNormalizer") || name.contains("ddn")) {
			File libPath = new File(path, name);
			if (libPath.exists()) {
				try {
					System.load(new File(path, name).getAbsolutePath());
					return true;
				} catch (UnsatisfiedLinkError e) {
					System.err.println(e);
					return false;
				}

			} else
				return false;
		}
		else {
			return false;
		}
	}

	private static boolean extractResourceFiles(String ddnNativeLibraryPath, String ddnNativeLibraryName,
			String tempFolder) throws IOException {
		String[] filenames = null;
		if (Utils.isWindows()) {
			filenames = new String[] {"api-ms-win-core-file-l1-2-0.dll",
			"api-ms-win-core-file-l2-1-0.dll",
			"api-ms-win-core-localization-l1-2-0.dll",
			"api-ms-win-core-processthreads-l1-1-1.dll",
			"api-ms-win-core-synch-l1-2-0.dll",
			"api-ms-win-core-timezone-l1-1-0.dll",
			"api-ms-win-crt-conio-l1-1-0.dll",
			"api-ms-win-crt-convert-l1-1-0.dll",
			"api-ms-win-crt-environment-l1-1-0.dll",
			"api-ms-win-crt-filesystem-l1-1-0.dll",
			"api-ms-win-crt-heap-l1-1-0.dll",
			"api-ms-win-crt-locale-l1-1-0.dll",
			"api-ms-win-crt-math-l1-1-0.dll",
			"api-ms-win-crt-multibyte-l1-1-0.dll",
			"api-ms-win-crt-runtime-l1-1-0.dll",
			"api-ms-win-crt-stdio-l1-1-0.dll",
			"api-ms-win-crt-string-l1-1-0.dll",
			"api-ms-win-crt-time-l1-1-0.dll",
			"api-ms-win-crt-utility-l1-1-0.dll",
			"concrt140.dll",
			"DynamicImagex64.dll",
			"DynamicPdfCorex64.dll",
			"DynamicPdfx64.dll",
			"DynamsoftCorex64.dll",
			"DynamsoftDocumentNormalizerx64.dll",
			"DynamsoftImageProcessingx64.dll",
			"DynamsoftIntermediateResultx64.dll",
			"msvcp140.dll",
			"msvcp140_1.dll",
			"msvcp140_2.dll",
			"ucrtbase.dll",
			"vccorlib140.dll",
			"vcomp140.dll",
			"vcruntime140.dll"};
		}
		else if (Utils.isLinux()) {
			filenames = new String[] {"libDynamicImage.so", "libDynamicPdf.so", "libDynamicPdfCore.so", "libDynamsoftCore.so", "libDynamsoftDocumentNormalizer.so", "libDynamsoftImageProcessing.so", "libDynamsoftIntermediateResult.so"};
		}
		
		boolean ret = true;
		
		for (String file : filenames) {
			ret &= extractAndLoadLibraryFile(ddnNativeLibraryPath, file, tempFolder);
		}
		
		return ret;
	}

	public static boolean load() throws Exception {

		// Load the os-dependent library from the jar file
		String ddnNativeLibraryName = System.mapLibraryName("ddn");
		if (ddnNativeLibraryName != null && ddnNativeLibraryName.endsWith("dylib")) {
			ddnNativeLibraryName = ddnNativeLibraryName.replace("dylib", "jnilib");
		}

		String ddnNativeLibraryPath = "/com/dynamsoft/ddn/native";
		if (Utils.isWindows()) {
			ddnNativeLibraryPath = "/com/dynamsoft/ddn/native/win";
		}
		else if (Utils.isLinux()) {
			ddnNativeLibraryPath = "/com/dynamsoft/ddn/native/linux";
		}

		if (NativeDocumentScanner.class.getResource(ddnNativeLibraryPath + "/" + ddnNativeLibraryName) == null) {
			throw new Exception("Error loading native library: " + ddnNativeLibraryPath + "/" + ddnNativeLibraryName);
		}

		// Temporary library folder
		String tempFolder = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();

		// Extract resource files
		return extractResourceFiles(ddnNativeLibraryPath, ddnNativeLibraryName, tempFolder);
	}

	static String md5sum(InputStream input) throws IOException {
		BufferedInputStream in = new BufferedInputStream(input);

		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			DigestInputStream digestInputStream = new DigestInputStream(in, digest);
			for (; digestInputStream.read() >= 0;) {

			}
			ByteArrayOutputStream md5out = new ByteArrayOutputStream();
			md5out.write(digest.digest());
			return md5out.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 algorithm is not available: " + e);
		} finally {
			in.close();
		}
	}

	private static boolean extractAndLoadLibraryFile(String libFolderForCurrentOS, String libraryFileName,
			String targetFolder) {
		String nativeLibraryFilePath = libFolderForCurrentOS + "/" + libraryFileName;

		String extractedLibFileName = libraryFileName;
		File extractedLibFile = new File(targetFolder, extractedLibFileName);

		try {
			if (extractedLibFile.exists()) {
				// test md5sum value
				String md5sum1 = md5sum(NativeDocumentScanner.class.getResourceAsStream(nativeLibraryFilePath));
				String md5sum2 = md5sum(new FileInputStream(extractedLibFile));

				if (md5sum1.equals(md5sum2)) {
					return loadNativeLibrary(targetFolder, extractedLibFileName);
				} else {
					// remove old native library file
					boolean deletionSucceeded = extractedLibFile.delete();
					if (!deletionSucceeded) {
						throw new IOException(
								"failed to remove existing native library file: " + extractedLibFile.getAbsolutePath());
					}
				}
			}

			// Extract file into the current directory
			InputStream reader = NativeDocumentScanner.class.getResourceAsStream(nativeLibraryFilePath);
			FileOutputStream writer = new FileOutputStream(extractedLibFile);
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, bytesRead);
			}

			writer.close();
			reader.close();

			if (!System.getProperty("os.name").contains("Windows")) {
				try {
					Runtime.getRuntime().exec(new String[] { "chmod", "755", extractedLibFile.getAbsolutePath() })
							.waitFor();
				} catch (Throwable e) {
				}
			}

			return loadNativeLibrary(targetFolder, extractedLibFileName);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		}

	}
}
