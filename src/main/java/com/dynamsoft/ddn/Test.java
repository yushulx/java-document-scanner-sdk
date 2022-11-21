package com.dynamsoft.ddn;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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
							System.out.println("Normalized image width: " + normalizedImage.width);
						}
					}
					
					graphics2D.dispose();
					// Utils.display(image);

					boolean success = ImageIO.write(image, "png", new java.io.File("document-edge-detection.png"));
					if (success) {
						System.out.println("Successfully saved the image.");
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
