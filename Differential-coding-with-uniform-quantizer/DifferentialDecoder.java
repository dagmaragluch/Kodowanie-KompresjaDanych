import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DifferentialDecoder {

    String fileName = "C:\\Users\\gluch\\Desktop\\kkd\\Kodowanie-KompresjaDanych\\encoded-image.txt";
    int k;
    int width;
    int height;
    String image;
    UniformQuantizer quantizer;
    public final Pixel BLACK = new Pixel(0, 0, 0);

    DifferentialDecoder() throws IOException {
        readFile();
        quantizer = new UniformQuantizer(k);
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Decode");
        DifferentialDecoder decoder = new DifferentialDecoder();

        decoder.decode();

    }


    public void decode() {

        Pixel[][] differences = readDifferencesSequence();
        Pixel[][] newImage = decodeImage(differences);
        System.out.println(newImage);
    }


    public void readFile() throws IOException {
        BufferedReader brTest = new BufferedReader(new FileReader(fileName));
        String[] strings = brTest.readLine().split(" ");

        k = Integer.parseInt(strings[0]);
        width = Integer.parseInt(strings[1]);
        height = Integer.parseInt(strings[2]);

        image = brTest.readLine();
//        System.err.println(image.length());
    }


    public Pixel[][] readDifferencesSequence() {

        Pixel[][] differences = new Pixel[height][width];
        int pixelLength = 3 * k;

        int i = 0;

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                System.err.println("row = " + row + "  col = " + column);
                String str = image.substring(i, i + pixelLength);
                differences[row][column] = binToPixel(str);
                i += pixelLength;

            }
        }
        return differences;
    }


    public Pixel binToPixel(String bin) {
        int red = quantizer.numberOfIntervalToMidpoint(bin.substring(0, k));
        int green = quantizer.numberOfIntervalToMidpoint(bin.substring(k, 2 * k));
        int blue = quantizer.numberOfIntervalToMidpoint(bin.substring(2 * k));

        return new Pixel(red, green, blue, true);
    }


    public Pixel[][] decodeImage(Pixel[][] differences) {
        Pixel[][] decodedImage = new Pixel[height][width];
        Pixel pixelToAdd = BLACK;

//        System.err.println("*******************");
//        System.err.println("width (columns) = " + width);
//        System.err.println("height (rows) = " + height);

        System.err.println();

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                if (column == 0) {
                    pixelToAdd = BLACK;
                } else {
                    pixelToAdd = differences[row][column - 1];
                }
//                System.err.println("row = " + row + "  col = " + column);
                decodedImage[row][column] = Pixel.plus(differences[row][column], pixelToAdd);
            }
        }

        return decodedImage;
    }


}
