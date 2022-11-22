package com.dynamsoft.ddn;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 1) {
			System.out.println("Please input an image file.");
			return;
		}
		String fileName = args[0];
		String license = "DLS2eyJoYW5kc2hha2VDb2RlIjoiMjAwMDAxLTE2NDk4Mjk3OTI2MzUiLCJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSIsInNlc3Npb25QYXNzd29yZCI6IndTcGR6Vm05WDJrcEQ5YUoifQ==";
		String template = Template.binary;

		if (args.length == 2) {
			String templateFile =  args[1];
			FileReader reader = null;
			try {
				reader = new FileReader(templateFile);
				char[] chars = new char[1024];
				int len = 0;
				StringBuilder sb = new StringBuilder();
				while ((len = reader.read(chars)) != -1) {
					sb.append(new String(chars, 0, len));
				}
				template = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (args.length == 3) {
			license = args[2];
		}

		try {
			int ret = NativeDocumentScanner.setLicense(license);
			NativeDocumentScanner scanner = new NativeDocumentScanner();
			System.out.println("Version number: " + scanner.getVersion());
			ret = scanner.setParameters(template);
			ArrayList<DocumentResult> results = (ArrayList<DocumentResult>)scanner.detectFile(fileName);
			if (results != null) {
				BufferedImage image = null;
				try
				{
					image = ImageIO.read(new File(fileName));
					Graphics2D graphics2D = image.createGraphics();

					for (DocumentResult result : results) {
						System.out.println("Confidence: " + result.confidence);
						System.out.println("x1: " + result.x1 + ", y1: " + result.y1);
						System.out.println("x2: " + result.x2 + ", y2: " + result.y2);
						System.out.println("x3: " + result.x3 + ", y3: " + result.y3);
						System.out.println("x4: " + result.x4 + ", y4: " + result.y4);

						graphics2D.setColor(Color.RED);
						graphics2D.setStroke(new BasicStroke(3));
						graphics2D.drawLine(result.x1, result.y1, result.x2, result.y2);
						graphics2D.drawLine(result.x2, result.y2, result.x3, result.y3);
						graphics2D.drawLine(result.x3, result.y3, result.x4, result.y4);
						graphics2D.drawLine(result.x4, result.y4, result.x1, result.y1);

						NormalizedImage normalizedImage = scanner.normalizeFile(fileName, result.x1, result.y1, result.x2, result.y2, result.x3, result.y3, result.x4, result.y4);
						if (normalizedImage != null) {
							normalizedImage.saveImage("jpg", "normalized.jpg");
							System.out.println("Saved normalized image to normalized.jpg");
						}
					}
					
					graphics2D.dispose();
					// Utils.display(image);

					boolean success = ImageIO.write(image, "png", new java.io.File("document-edge-detection.png"));
					if (success) {
						System.out.println("Successfully saved the image to document-edge-detection.png");
					}
					else {
						System.out.println("Failed to save the image.");
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			scanner.destroyInstance();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
