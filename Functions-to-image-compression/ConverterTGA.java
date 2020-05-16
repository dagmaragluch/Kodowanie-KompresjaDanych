
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Read TGA file and convert it to two dimensional array of Pixels
 */
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
                for (int i = 0; i <= nb; i++) {
                    if (idx < pixels.length)
                        pixels[idx++] = v;
                }
            }
            n -= nb + 1;
        }

        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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
     * <p>
     * example binaryString: 11111111 01001010 00010111 00101010
     * 1st byte: value of alpha (in this exercise always = 11111111)
     * 2nd byte: value of red color
     * 3th byte: value of green
     * 4th byte: value of blue
     */
    public static Pixel convertIntegerToColor(int number) {

        String binaryString = Integer.toBinaryString(number);

        int red = Integer.parseInt(binaryString.substring(8, 16), 2);
        int green = Integer.parseInt(binaryString.substring(16, 24), 2);
        int blue = Integer.parseInt(binaryString.substring(24, 32), 2);

        return new Pixel(red, green, blue);
    }

    public static int convertColorToInteger(Pixel pixel) {

        String alpha = "11111111";
        String red = Integer.toBinaryString(pixel.getRed());
        red = String.format("%08d", Integer.parseInt(red));
        String green = Integer.toBinaryString(pixel.getGreen());
        green = String.format("%08d", Integer.parseInt(green));
        String blue = Integer.toBinaryString(pixel.getBlue());
        blue = String.format("%08d", Integer.parseInt(blue));

        String binaryPixelValue = alpha + red + green + blue;
//        System.out.println(binaryPixelValue);

        int pixelValue = (int) Long.parseLong(binaryPixelValue, 2);
//        System.out.println(pixelValue);

        return pixelValue;
    }

    public static BufferedImage getNewImage(BufferedImage oldImage, Pixel[][] newPixelsArray) {

//        List<Pixel> newPixels = twoDArrayToList(newPixelsArray);
        DataBuffer dataBuffer = (DataBufferInt) oldImage.getRaster().getDataBuffer();

//        BufferedImage newImage =
//        int[] pixelsInt = ((DataBufferInt) oldImage.getRaster().getDataBuffer().setElem();


        int numberOfColumns = oldImage.getWidth();
        int numberOfRows = oldImage.getHeight();

        int i = 0;
        for (int row = 0; row < numberOfColumns; row++) {
            for (int column = 0; column < numberOfRows; column++) {
                int newValue = convertColorToInteger(newPixelsArray[row][column]);
                dataBuffer.setElem(i, newValue);
                i++;
            }
        }

        return oldImage;
    }


    public static <T> List<T> twoDArrayToList(T[][] twoDArray) {
        List<T> list = new ArrayList<T>();
        for (T[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }


}
