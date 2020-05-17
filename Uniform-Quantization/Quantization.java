import java.awt.image.BufferedImage;
import java.io.IOException;

public class Quantization {

    ConverterTGA converterTGA = new ConverterTGA();

    public final int RED_BITS = 1;
    public final int GREEN_BITS = 1;
    public final int BLUE_BITS = 1;
    public final int MAX_VALUE_OF_COLOR = 256;


    public BufferedImage image;
    public Pixel[][] pixels;
    public int numberOfColumns;
    public int numberOfRows;


    public Quantization(String fileName) {
        image = initImage(fileName);
        initValues();
    }

    private BufferedImage initImage(String fileName) {
        try {
            return (BufferedImage) converterTGA.getImage(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initValues() {
        pixels = converterTGA.getPixels();
        numberOfColumns = pixels.length;
        numberOfRows = pixels[0].length;
    }

    /**
     * @return - compressed, quantized image (values of Pixel in int)
     */
    public Pixel[][] imageQuantization() {

        Pixel[][] newImage = new Pixel[numberOfColumns][numberOfRows];

        for (int row = 0; row < numberOfColumns; row++) {
            for (int column = 0; column < numberOfRows; column++) {

                Pixel oldPixel = pixels[row][column];
                int compressedRed = colorQuantization(oldPixel.getRed(), RED_BITS);
                int compressedGreen = colorQuantization(oldPixel.getGreen(), GREEN_BITS);
                int compressedBlue = colorQuantization(oldPixel.getBlue(), BLUE_BITS);
                Pixel compressedPixel = new Pixel(compressedRed, compressedGreen, compressedBlue);

                newImage[row][column] = compressedPixel;
            }
        }
        return newImage;
    }

    public int colorQuantization(int colorValue, int bitsForColor) {
        int k = (int) Math.pow(2, bitsForColor);
        int step = MAX_VALUE_OF_COLOR / k;  //how many intervals
        int midpointOfInterval = step / 2;

        for (int i = 0; i < k; i++) {
            if (colorValue > i * step && colorValue < (i + 1) * step) {
                return midpointOfInterval;
            }
            midpointOfInterval += step;
        }
        return midpointOfInterval;
    }

}
