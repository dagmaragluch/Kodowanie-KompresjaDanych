import java.awt.image.BufferedImage;
import java.io.IOException;

public class Predictions {

    public final Pixel BLACK = new Pixel(0, 0, 0);

    ConverterTGA converterTGA = new ConverterTGA();
    public BufferedImage image;
    public Pixel[][] pixels;
    public int numberOfColumns;
    public int numberOfRows;


    public Predictions(String fileName) {
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
     * Calculate prediction from input image
     * @param methodNumber - number of method used to calculation
     * @return array of subtractions of Pixels and Predictions (X-X')
     */
    public Pixel[][] getImageOfPredictions(int methodNumber) {

        Pixel[][] newPixels = new Pixel[numberOfColumns][numberOfRows];

        for (int row = 0; row < numberOfColumns; row++) {
            for (int column = 0; column < numberOfRows; column++) {
                //X - X'
                newPixels[row][column] = Pixel.minus(pixels[row][column],  getPrediction(row, column, methodNumber));
            }
        }
        return newPixels;
    }


    /**
     * Functions to get neighbour pixel X: N, W or NW
     *
     *    NW |  N
     *    --------
     *    W |  X
     */
    public Pixel getN(int rowX, int columnX) {

        if (rowX != 0) {
            return pixels[rowX - 1][columnX];
        } else {
            return BLACK;
        }
    }

    public Pixel getW(int rowX, int columnX) {

        if (columnX != 0) {
            return pixels[rowX][columnX - 1];
        } else {
            return BLACK;
        }
    }

    public Pixel getNW(int rowX, int columnX) {

        if (rowX != 0 && columnX != 0) {
            return pixels[rowX - 1][columnX - 1];
        } else {
            return BLACK;
        }
    }

    /**
     * Get Prediction of pixel using 1 from 8 methods:
     * 1) X' = W
     * 2) X' = N
     * 3) X' = NW
     * 4) X' = N + W − NW
     * 5) X' = N + (W − NW)/2
     * 6) X' = W + (N − NW)/2
     * 7) X' = (N + W)/2
     * 8) X' = new standard
     *
     * @param r - row
     * @param c - column
     * @return prediction
     */
    public Pixel getPrediction(int r, int c, int methodNumber) {

        switch (methodNumber) {
            case 1:
                return getW(r, c);
            case 2:
                return getN(r, c);
            case 3:
                return getNW(r, c);
            case 4:
                Pixel temp = Pixel.plus(getN(r, c), getW(r, c));
                return Pixel.minus(temp, getNW(r, c));
            case 5:
                Pixel temp1 = Pixel.div2(Pixel.minus(getW(r, c), getNW(r, c)));
                return Pixel.plus(getN(r, c), temp1);
            case 6:
                Pixel temp2 = Pixel.div2(Pixel.minus(getN(r, c), getNW(r, c)));
                return Pixel.plus(getW(r, c), temp2);
            case 7:
                Pixel temp3 = Pixel.plus(getN(r, c), getW(r, c));
                return Pixel.div2(temp3);
            case 8:
                return newStandard(r, c);
            default:
                return null;
        }
    }

    /**
     * Another, new method to calculating prediction
     *
     * @param r - row
     * @param c - column
     * @return prediction
     */
    public Pixel newStandard(int r, int c) {
        Pixel N = getN(r, c);
        Pixel W = getW(r, c);
        Pixel NW = getNW(r, c);
        Pixel maxWandN = Pixel.max(W, N);
        Pixel minWandN = Pixel.min(W, N);

        //if NW >= max(W, N)
        if (Pixel.isGreaterOrEqualTo(NW, maxWandN)) {
            return maxWandN;
        } else
            //if min(W, N) >= NW
            if (Pixel.isGreaterOrEqualTo(minWandN, NW)) {
                return minWandN;
            } else {
                Pixel temp = Pixel.plus(W, N);
                return Pixel.minus(temp, NW);
            }
    }

}
