import java.awt.image.BufferedImage;
import java.io.IOException;

public class Quantization {

    ConverterTGA converterTGA = new ConverterTGA();

    public final int MAX_VALUE_OF_COLOR = 256;

    public BufferedImage image;
    public Pixel[][] pixels;
    public int width;
    public int height;

    public int RED_BITS;
    public int GREEN_BITS;
    public int BLUE_BITS;

    public Quantization(String fileName, int bitsForRed, int bitsForGreen, int bitsForBlue) {
        image = initImage(fileName);
        initValues(bitsForRed, bitsForGreen, bitsForBlue);
    }

    private BufferedImage initImage(String fileName) {
        try {
            return (BufferedImage) converterTGA.getImage(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initValues(int bitsForRed, int bitsForGreen, int bitsForBlue) {
        pixels = converterTGA.getPixels();
        height = pixels.length;
        width = pixels[0].length;

        RED_BITS = bitsForRed;
        GREEN_BITS = bitsForGreen;
        BLUE_BITS = bitsForBlue;

    }

    /**
     * @return - compressed, quantized image (values of Pixel - int)
     */
    public Pixel[][] imageQuantization() {

        Pixel[][] newImage = new Pixel[height][width];

        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {

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
        int k = (int) Math.pow(2, bitsForColor);   //how many intervals
        int step = MAX_VALUE_OF_COLOR / k;
        int midpointOfInterval = step / 2;

        if (bitsForColor != 8) {
            for (int i = 0; i < k; i++) {
                if (colorValue >= i * step && colorValue < (i + 1) * step) {
                    return midpointOfInterval;
                }
                midpointOfInterval += step;
            }
            return midpointOfInterval;

        } else {
            return colorValue;
        }

    }


    public void mseAndSNR(Pixel[][] oldImage, Pixel[][] newImage) {

        int N = width * height;
        float diffAll;
        float diffRed;
        float diffGreen;
        float diffBlue;
        float sumAll = 0;
        float sumRed = 0;
        float sumGreen = 0;
        float sumBlue = 0;

        //mse
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                diffRed = oldImage[row][column].getRed() - newImage[row][column].getRed();
                sumRed = sumRed + diffRed * diffRed;

                diffGreen = oldImage[row][column].getGreen() - newImage[row][column].getGreen();
                sumGreen = sumGreen + diffGreen * diffGreen;

                diffBlue = oldImage[row][column].getBlue() - newImage[row][column].getBlue();
                sumBlue = sumBlue + diffBlue * diffBlue;

                diffAll = diffRed * diffRed + diffGreen * diffGreen + diffBlue * diffBlue;
                sumAll = sumAll + diffAll;
            }
        }

        float mseAll = sumAll / (3 * N);
        float mseRed = sumRed / N;
        float mseGreen = sumGreen / N;
        float mseBlue = sumBlue / N;

        System.out.println("mse    = " + mseAll);
        System.out.println("mse(r) = " + mseRed);
        System.out.println("mse(g) = " + mseGreen);
        System.out.println("mse(b) = " + mseBlue);

        //SNR
        sumRed = 0;
        sumGreen = 0;
        sumBlue = 0;
        float temp;

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                temp = newImage[row][column].getRed();
                sumRed = sumRed + temp * temp;

                temp = newImage[row][column].getGreen();
                sumGreen = sumGreen + temp * temp;

                temp = newImage[row][column].getBlue();
                sumBlue = sumBlue + temp * temp;
            }
        }

        sumAll = sumRed + sumGreen + sumBlue;
        sumAll = sumAll / (3 * N);
        sumRed = sumRed / N;
        sumGreen = sumGreen / N;
        sumBlue = sumBlue / N;

        float snrAll = sumAll / mseAll;
        float snrRed = sumRed / mseRed;
        float snrGreen = sumGreen / mseGreen;
        float snrBlue = sumBlue / mseBlue;

        System.out.println("SNR    = " + snrAll + "  (" + 10 * (float) Math.log10(snrAll) + " dB)");
        System.out.println("SNR(r) = " + snrRed + "  (" + 10 * (float) Math.log10(snrRed) + " dB)");
        System.out.println("SNR(g) = " + snrGreen + "  (" + 10 * (float) Math.log10(snrGreen) + " dB)");
        System.out.println("SNR(b) = " + snrBlue + "  (" + 10 * (float) Math.log10(snrBlue) + " dB)");
    }


}
