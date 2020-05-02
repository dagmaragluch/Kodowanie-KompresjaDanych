import java.awt.image.BufferedImage;
import java.io.IOException;

public class Predictions {

    public final Pixel BLACK = new Pixel(0, 0, 0);

//    public String fileName;
//
//    public Predictions(String fileName) {
//        this.fileName = fileName;
//    }

    BufferedImage image;
    {
        try {
            image = (BufferedImage) ConverterTGA.getImage("C:\\Users\\gluch\\Desktop\\kkd\\testy4\\example0.tga");
//            image = (BufferedImage) ConverterTGA.getImage(this.fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final Pixel[][] pixels = ConverterTGA.getPixel2DArray(image);
    public final int numberOfColumns = pixels.length;
    public final int numberOfRows = pixels[0].length;


    public Pixel[][] getImageOfPredictions(int methodNumber) {

        Pixel[][] newPixels1 = new Pixel[numberOfColumns][numberOfRows];

        for (int row = 0; row < numberOfColumns; row++) {
            for (int column = 0; column < numberOfRows; column++) {
//                newPixels1[row][column] = getPrediction(pixels[row][column], methodNumber);
                newPixels1[row][column] = getPrediction(row, column, methodNumber);
            }
        }
        return newPixels1;
    }


    /********************************/

    /**
     * NW |  N
     * -----------
     * W |  X
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
     * 1) X' = W
     * 2) X' = N
     * 3) X' = NW
     * 4) X' = N + W − NW
     * 5) X' = N + (W − NW)/2
     * 6) X' = W + (N − NW)/2
     * 7) X' = (N + W)/2
     * 8) X' = new standard
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
