public class UniformQuantizer {

    public final int MAX_VALUE_OF_COLOR = 256;
    public int BITS_FOR_COLOR;
    int numberOfIntervals;
    int step;


    public UniformQuantizer(int bitsForColor) {
        BITS_FOR_COLOR = bitsForColor;
        numberOfIntervals = (int) Math.pow(2, BITS_FOR_COLOR);
        step = MAX_VALUE_OF_COLOR / numberOfIntervals;
    }

    /**
     * @return - compressed, quantized image (values of Pixel - int)
     */
    public Pixel[][] imageQuantization(Pixel[][] image) {
        int height = image.length;
        int width = image[0].length;

        Pixel[][] quantizedImage = new Pixel[height][width];

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                Pixel oldPixel = image[row][column];
                int compressedRed = colorQuantization(oldPixel.getRed(), numberOfIntervals, step);
                int compressedGreen = colorQuantization(oldPixel.getGreen(), numberOfIntervals, step);
                int compressedBlue = colorQuantization(oldPixel.getBlue(), numberOfIntervals, step);
                Pixel compressedPixel = new Pixel(compressedRed, compressedGreen, compressedBlue);

                quantizedImage[row][column] = compressedPixel;
            }
        }
        return quantizedImage;
    }

    public int colorQuantization(int colorValue, int numberOfIntervals, int step) {
        int midpointOfInterval = step / 2;

        for (int i = 0; i < numberOfIntervals; i++) {
            if (colorValue >= i * step && colorValue < (i + 1) * step) {
                return midpointOfInterval;
            }
            midpointOfInterval += step;
        }
        return midpointOfInterval;
    }

    /**
     * @param diff - different modulo 256 (!)
     * @return interval number
     */
    public int quantizeDifferent(int diff) {
        int interval = 0;

        if (diff < 0)         // diff % 256 for negative number
            diff = MAX_VALUE_OF_COLOR + diff;

        for (int i = 0; i < MAX_VALUE_OF_COLOR; i += step) {
            if (diff >= i && diff < i + step) {
                return interval;
            }
            interval++;
        }
        return interval;
    }

    /**
     * @param bin interval number (range: [0..2^k]) as binary string
     * @return midpoint of interval (int)
     */
    public int intervalNumberToMidpoint(String bin) {

        int interval = Integer.parseInt(bin, 2);
        int startValue = step / 2;

        return (interval * step) + startValue;
    }


}
