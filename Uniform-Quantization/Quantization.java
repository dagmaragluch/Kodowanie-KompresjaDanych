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
        width = pixels.length;
        height = pixels[0].length;

        RED_BITS = bitsForRed;
        GREEN_BITS = bitsForGreen;
        BLUE_BITS = bitsForBlue;

    }

    /**
     * @return - compressed, quantized image (values of Pixel - int)
     */
    public Pixel[][] imageQuantization() {

        Pixel[][] newImage = new Pixel[width][height];

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                Pixel oldPixel = pixels[column][row];
                int compressedRed = colorQuantization(oldPixel.getRed(), RED_BITS);
                int compressedGreen = colorQuantization(oldPixel.getGreen(), GREEN_BITS);
                int compressedBlue = colorQuantization(oldPixel.getBlue(), BLUE_BITS);
                Pixel compressedPixel = new Pixel(compressedRed, compressedGreen, compressedBlue);

                newImage[column][row] = compressedPixel;
            }
        }
        return newImage;
    }

    public int colorQuantization(int colorValue, int bitsForColor) {
        int k = (int) Math.pow(2, bitsForColor);
        int step = MAX_VALUE_OF_COLOR / k;  //how many intervals
        int midpointOfInterval = step / 2;

        if (bitsForColor != 8) {
            for (int i = 0; i < k; i++) {
                if (colorValue > i * step && colorValue < (i + 1) * step) {
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
                diffAll = Pixel.sumPixel(oldImage[column][row]) - Pixel.sumPixel(newImage[column][row]);
                sumAll = sumAll + diffAll * diffAll;

                diffRed = oldImage[column][row].getRed() - newImage[column][row].getRed();
                sumRed = sumRed + diffRed * diffRed;

                diffGreen = oldImage[column][row].getGreen() - newImage[column][row].getGreen();
                sumGreen = sumGreen + diffGreen * diffGreen;

                diffBlue = oldImage[column][row].getBlue() - newImage[column][row].getBlue();
                sumBlue = sumBlue + diffBlue * diffBlue;

            }
        }
        float mseAll = sumAll / N;
        float mseRed = sumRed / N;
        float mseGreen = sumGreen / N;
        float mseBlue = sumBlue / N;

        System.out.println("mse    = " + Math.sqrt(mseAll));
        System.out.println("mse(r) = " + Math.sqrt(mseRed));
        System.out.println("mse(g) = " + Math.sqrt(mseGreen));
        System.out.println("mse(b) = " + Math.sqrt(mseBlue));

        //SNR
        sumAll = 0;
        sumRed = 0;
        sumGreen = 0;
        sumBlue = 0;
        float temp;

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                temp = Pixel.sumPixel(newImage[column][row]);
                sumAll = sumAll + temp * temp;

                temp = newImage[column][row].getRed();
                sumRed = sumRed + temp * temp;

                temp = newImage[column][row].getGreen();
                sumGreen = sumGreen + temp * temp;

                temp = newImage[column][row].getBlue();
                sumBlue = sumBlue + temp * temp;
            }
        }

        sumAll = sumAll / N;
        sumRed = sumRed / N;
        sumGreen = sumGreen / N;
        sumBlue = sumBlue / N;

        float snrAll = sumAll / mseAll;
        float snrRed = sumRed / mseRed;
        float snrGreen = sumGreen / mseGreen;
        float snrBlue = sumBlue / mseBlue;

        System.out.println("SNR    = " + snrAll + "  (" + 10 * Math.log10(snrAll) + " dB)");
        System.out.println("SNR(r) = " + snrRed + "  (" + 10 * Math.log10(snrRed) + " dB)");
        System.out.println("SNR(g) = " + snrGreen + "  (" + 10 * Math.log10(snrGreen) + " dB)");
        System.out.println("SNR(b) = " + snrBlue + "  (" + 10 * Math.log10(snrBlue) + " dB)");
    }


}
