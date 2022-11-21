package com.dynamsoft.ddn;
import java.util.ArrayList;
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 1) {
			System.out.println("Please input an image file.");
			return;
		}
		String fileName = args[0];
		try {
			int ret = NativeDocumentScanner.setLicense("DLS2eyJoYW5kc2hha2VDb2RlIjoiMjAwMDAxLTE2NDk4Mjk3OTI2MzUiLCJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSIsInNlc3Npb25QYXNzd29yZCI6IndTcGR6Vm05WDJrcEQ5YUoifQ==");
			NativeDocumentScanner scanner = new NativeDocumentScanner();
			System.out.println("Version number: " + scanner.getVersion());
			ret = scanner.setParameters(Template.color);
			System.out.println("Set parameters: " + ret);
			ArrayList<DocumentResult> results = (ArrayList<DocumentResult>)scanner.detectFile(fileName);
			if (results != null) {
				for (DocumentResult result : results) {
					System.out.println("Confidence: " + result.confidence);
					System.out.println("x1: " + result.x1 + ", y1: " + result.y1);
					System.out.println("x2: " + result.x2 + ", y2: " + result.y2);
					System.out.println("x3: " + result.x3 + ", y3: " + result.y3);
					System.out.println("x4: " + result.x4 + ", y4: " + result.y4);
				}
			}
			scanner.destroyInstance();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
