import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Read TGA file and convert it to two dimensional array of Pixels
 */
public class ConverterTGA {

    private Pixel[][] pixelsArray;

    public Pixel[][] getPixels() {
        return pixelsArray;
    }

    public ConverterTGA() {
        this.pixelsArray = null;
    }

    public Image getImage(String fileName) throws IOException {
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

    public Image decode(byte[] buf) {
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
        int idx = n - 1;

        Pixel[][] pixels2 = new Pixel[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int b = read(buf);
                int g = read(buf);
                int r = read(buf);
                pixels[idx] = (r << 16) | (g << 8) | b;
                idx--;
                pixels2[j][i] = new Pixel(r, g, b);

            }
        }

        this.pixelsArray = pixels2;

        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bimg.setRGB(0, 0, width, height, pixels, 0, width);
        return bimg;
    }


    /**
     * First get long integer array from BufferedImage (1 pixel = 1 integer)
     * than write it as Pixels into two dimension array;
     * location of pixel is marked by row and column numbers - as they were in image
     */
    public static Pixel[][] getPixel2DArray(BufferedImage img) {
        int[] pixelsInt = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        int numberOfColumns = img.getWidth();
        int numberOfRows = img.getHeight();

        Pixel[][] pixels = new Pixel[numberOfColumns][numberOfRows];

        int i = 0;
        for (int row = 0; row < numberOfColumns; row++) {
            for (int column = 0; column < numberOfRows; column++) {
                pixels[row][column] = convertIntegerToColor(pixelsInt[i]);
                i++;
            }
        }
        return pixels;
    }

    /**
     * Convert integer value first to binary string, than to Pixel (color RGB)
     * example binaryString: 01001010 00010111 00101010
     * 1st byte: value of red color
     * 2nd byte: value of green
     * 3th byte: value of blue
     */
    public static Pixel convertIntegerToColor(int number) {

        String binaryString = Integer.toBinaryString(number);

        while (binaryString.length() < 24) {
            binaryString = "0" + binaryString;
        }

        int red = Integer.parseInt(binaryString.substring(0, 8), 2);
        int green = Integer.parseInt(binaryString.substring(8, 16), 2);
        int blue = Integer.parseInt(binaryString.substring(16, 24), 2);

        return new Pixel(red, green, blue);
    }

    public static int convertColorToInteger(Pixel pixel) {

        String red = Integer.toBinaryString(pixel.getRed());
        red = String.format("%08d", Integer.parseInt(red));
        String green = Integer.toBinaryString(pixel.getGreen());
        green = String.format("%08d", Integer.parseInt(green));
        String blue = Integer.toBinaryString(pixel.getBlue());
        blue = String.format("%08d", Integer.parseInt(blue));

        String binaryPixelValue = red + green + blue;
//        System.out.println(binaryPixelValue);

        int pixelValue = (int) Long.parseLong(binaryPixelValue, 2);
//        System.out.println(pixelValue);

        return pixelValue;
    }

    public static BufferedImage getNewImage(Pixel[][] newPixelsArray) {

        int width = newPixelsArray.length;
        int height = newPixelsArray[0].length;

        int n = width * height;
        int[] newPixels = new int[n];
        int idx = n - 1;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newPixels[idx] = convertColorToInteger(newPixelsArray[j][i]);
                idx--;
            }
        }
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        newImage.setRGB(0, 0, width, height, newPixels, 0, width);

        return newImage;
    }


}
