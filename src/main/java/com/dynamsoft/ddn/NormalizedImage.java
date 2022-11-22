package com.dynamsoft.ddn;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class NormalizedImage {
    public int width;
    public int height;
    public int stride;
    public int format;
    public byte[] data;
    public int orientation;
    public int length;

    public NormalizedImage(int width, int height, int stride, int format, byte[] data, int orientation, int length) {
        this.width = width;
        this.height = height;
        this.stride = stride;
        this.format = format;
        this.data = data;
        this.orientation = orientation;
        this.length = length;
    }

    public void saveImage(String formatName, String fileName) {
        BufferedImage image = null;
        byte[] imageData = null;
        int[] pixels = new int[width * height];
        
        if (format == ImagePixelFormat.IPF_RGB_888) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageData = data;

            for (int i = 0; i < width * height; i++) {
                int r = imageData[i * 3] & 0xFF;
                int g = imageData[i * 3 + 1] & 0xFF;
                int b = imageData[i * 3 + 2] & 0xFF;
                pixels[i] = (r << 16) | (g << 8) | b;
            }
        }
        else if (format == ImagePixelFormat.IPF_GRAYSCALED) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            imageData = data;

            for (int i = 0; i < width * height; i++) {
                int gray = imageData[i] & 0xFF;
                pixels[i] = (gray << 16) | (gray << 8) | gray;
            }
        }
        else if (format == ImagePixelFormat.IPF_BINARY) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            imageData = binary2Grayscale();

            for (int i = 0; i < width * height; i++) {
                int gray = imageData[i] & 0xFF;
                pixels[i] = (gray << 16) | (gray << 8) | gray;
            }
        }
        
        image.setRGB(0, 0, width, height, pixels, 0, width);
        try {
            ImageIO.write(image, formatName, new java.io.File(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        // image.getRaster().setDataElements(0, 0, width, height, data);
        // try {
        //     ImageIO.write(image, formatName, new java.io.File(fileName));
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        // image.getRaster().setDataElements(0, 0, width, height, data);
        // try {
        //     ImageIO.write(image, formatName, new java.io.File(fileName));
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    public class ImagePixelFormat
    {
        public final static int IPF_BINARY = 0;
        public final static int IPF_BINARYINVERTED = 1;
        public final static int IPF_GRAYSCALED = 2;
        public final static int IPF_NV21 = 3;
        public final static int IPF_RGB_565 = 4;
        public final static int IPF_RGB_555 = 5;
        public final static int IPF_RGB_888 = 6;
        public final static int IPF_ARGB_8888 = 7;
        public final static int IPF_RGB_161616 = 8;
        public final static int IPF_ARGB_16161616 = 9; 
        public final static int IPF_ABGR_8888 = 10;
        public final static int IPF_ABGR_16161616 = 11; 
        public final static int IPF_BGR_88 = 12;
    }

//     public void Save(string filename)
//     {
//         if (_dataPtr != IntPtr.Zero)
//         {
//             NormalizedImageResult? image = (NormalizedImageResult?)Marshal.PtrToStructure(_dataPtr, typeof(NormalizedImageResult));
//             if (image != null)
//             {
//                 int ret = DDN_SaveImageDataToFile(image.Value.ImageData, filename);

//             }

//         }
//     }

    private byte[] binary2Grayscale()
    {
        byte[] output = new byte[width * height];
        int index = 0;

        int skip = stride * 8 - width;
        int shift = 0;
        int n = 1;

        for (byte b : data)
        {
            int byteCount = 7;
            while (byteCount >= 0)
            {
                int tmp = (b & (1 << byteCount)) >> byteCount;

                if (shift < stride * 8 * n - skip) {
                    if (tmp == 1)
                        output[index] = (byte)255;
                    else
                        output[index] = 0;
                    index += 1;
                }

                byteCount -= 1;
                shift += 1;
            }

            if (shift == stride * 8 * n) {
                n += 1;
            }
        }
        return output;
    }
}
