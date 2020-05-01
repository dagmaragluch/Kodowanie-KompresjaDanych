import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConverterTGA {

    public static Image getImage(String fileName) throws IOException {
        File f = new File(fileName);
        byte[] buf = new byte[(int) f.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
        bis.read(buf);
        bis.close();
        return decode(buf);
    }

    private static int offset;

    private static int btoi(byte b) {
        int a = b;
        return (a < 0 ? 256 + a : a);
    }

    private static int read(byte[] buf) {
        return btoi(buf[offset++]);
    }

    public static Image decode(byte[] buf) throws IOException {
        offset = 0;

        // Reading header
        for (int i = 0; i < 12; i++)
            read(buf);
        int width = read(buf) + (read(buf) << 8);
        int height = read(buf) + (read(buf) << 8);
        read(buf);
        read(buf);

        // Reading data
        int n = width * height;
        int[] pixels = new int[n];
        int idx = 0;

        while (n > 0) {
            int nb = read(buf);
            if ((nb & 0x80) == 0) {
                for (int i = 0; i <= nb; i++) {
                    int b = read(buf);
                    int g = read(buf);
                    int r = read(buf);
                    if (idx < pixels.length)
                        pixels[idx++] = 0xff000000 | (r << 16) | (g << 8) | b;
                }
            } else {
                nb &= 0x7f;
                int b = read(buf);
                int g = read(buf);
                int r = read(buf);
                int v = 0xff000000 | (r << 16) | (g << 8) | b;
                for (int i = 0; i <= nb; i++)
                    pixels[idx++] = v;
            }
            n -= nb + 1;
        }

        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bimg.setRGB(0, 0, width, height, pixels, 0, width);
        return bimg;
    }


    public static int[][] getPixel2DArray(BufferedImage img) throws IOException {
        int[] pixelsInt = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        System.out.println(pixelsInt.length);

        int weight = img.getWidth();
        int height = img.getHeight();
//        System.out.println("WEIGHT = " + weight + "   HEIGHT = " + height);

        int[][] pixels = new int[weight][height];

        int i = 0;
        for (int row = 0; row < weight; row++) {
            for (int column = 0; column < height; column++) {
//                System.err.println("pixels[" + row + "][" + column + "] = pixelsInt[" + i + "]   -->  " + pixelsInt[i]);
                pixels[row][column] = pixelsInt[i];
                i++;
            }
        }
        return pixels;
    }


    public static void main(String[] args) throws IOException {
        BufferedImage img = (BufferedImage) getImage("C:\\Users\\gluch\\Desktop\\kkd\\testy4\\example0.tga");
        getPixel2DArray(img);


        Integer a = -11921622;
        byte b = a.byteValue();
        System.out.println(b);
        System.out.println(Integer.toBinaryString(b));
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(-7751775));
        System.out.println(Integer.toBinaryString(-10323864));
    }


}
