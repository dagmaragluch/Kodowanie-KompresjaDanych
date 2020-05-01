import java.awt.image.BufferedImage;
import java.io.IOException;

public class PredictionsJPEG {

    public final ColorRGB BLACK = new ColorRGB(0, 0, 0);

    BufferedImage image;

    {
        try {
            image = (BufferedImage) ConverterTGA.getImage("C:\\Users\\gluch\\Desktop\\kkd\\testy4\\example0.tga");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final Pixel[][] pixels = ConverterTGA.getPixel2DArray(image);
    public final int numberOfColumns = pixels.length;
    public final int numberOfRows = pixels[0].length;


    public ColorRGB[][] getImageOfPredictions(int methodNumber) {

        ColorRGB[][] newPixels1 = new ColorRGB[numberOfColumns][numberOfRows];

        for (int row = 0; row < numberOfColumns; row++) {
            for (int column = 0; column < numberOfRows; column++) {
                newPixels1[row][column] = getPrediction(pixels[row][column], methodNumber);
                //ZMIENIĆ TO JAKOŚ, ŻEBY WSTAWIAĆ RÓŻNICĘ X-X'
                //PROBLEM: COLOR NIE MOZE MIEC WARTOSCI UJEMNYCH
                //ALBO WYRZUCIC EXCEPTION ALBO DOPISAĆ NOWA KLASE
            }
        }

        return newPixels1;
    }


    /********************************/


    public void test() {
        System.out.println("WEIGHT = " + numberOfColumns + "   HEIGHT = " + numberOfRows);
    }


    public static void main(String[] args) {
        PredictionsJPEG jpeg = new PredictionsJPEG();

        ColorRGB[][] c = jpeg.getImageOfPredictions(1);
        System.out.println(c);
    }


    /**
     * NW |  N
     * -----------
     * W |  X
     */
    public ColorRGB getN(Pixel pixelX) {
        int rowX = pixelX.getRow();
        int columnX = pixelX.getColumn();

        if (rowX != 0) {
            return pixels[rowX - 1][columnX].getColor();
        } else {
            return BLACK;
        }
    }

    public ColorRGB getW(Pixel pixelX) {
        int rowX = pixelX.getRow();
        int columnX = pixelX.getColumn();

        if (columnX != 0) {
            return pixels[rowX][columnX - 1].getColor();
        } else {
            return BLACK;
        }
    }

    public ColorRGB getNW(Pixel pixelX) {
        int rowX = pixelX.getRow();
        int columnX = pixelX.getColumn();

        if (rowX != 0 && columnX != 0) {
            return pixels[rowX - 1][columnX - 1].getColor();
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

    public ColorRGB getPrediction(Pixel oldPixel, int methodNumber) {

        switch (methodNumber) {
            case 1:
                return getW(oldPixel);
            case 2:
                return getN(oldPixel);
            default:
                return null;
        }
    }


}
