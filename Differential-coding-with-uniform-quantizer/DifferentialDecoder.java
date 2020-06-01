import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DifferentialDecoder {

    String fileName;
    int k;
    int width;
    int height;
    String image;
    UniformQuantizer quantizer;
    Pixel[][] newImage;

    DifferentialDecoder(String encodedFile) throws IOException {
        fileName = encodedFile;
        readFile();
        quantizer = new UniformQuantizer(k);
    }


    public static void main(String[] args) throws IOException {

        if (args.length < 1) throw new IllegalArgumentException("Required 1 argument!");

        String encodedFileName = args[0];
        DifferentialDecoder decoder = new DifferentialDecoder(encodedFileName);

        decoder.decode();
    }

    /**
     * decode image and save it to file
     */
    public void decode() throws IOException {

        Pixel[][] differences = readDifferencesSequence();
        newImage = decodeImage(differences);

        BufferedImage img = ConverterTGA.getNewImage(newImage);

        String outputFileName = "decoded-image.tga";

        File outputFile = new File(outputFileName);
        ImageIO.write(img, "TGA", outputFile);
    }

    /**
     * read txt file with decoded image
     * and set parameters: k, width, height
     */
    public void readFile() throws IOException {
        BufferedReader brTest = new BufferedReader(new FileReader(fileName));
        String[] strings = brTest.readLine().split(" ");

        k = Integer.parseInt(strings[0]);
        width = Integer.parseInt(strings[1]);
        height = Integer.parseInt(strings[2]);
        image = brTest.readLine();
    }

    /**
     * convert encoded image (long binary string) to
     * differences sequence as 2D pixel array
     */
    public Pixel[][] readDifferencesSequence() {

        Pixel[][] differences = new Pixel[height][width];
        int pixelLength = 3 * k;

        int i = 0;

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                String str = image.substring(i, i + pixelLength);
                differences[row][column] = binToPixel(str);
                i += pixelLength;
            }
        }
        return differences;
    }

    /**
     * decoded image:
     * - values from first column are get literally
     * - next values are calculated using adding modulo 256
     */
    public Pixel[][] decodeImage(Pixel[][] differences) {
        Pixel[][] decodedImage = new Pixel[height][width];

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                if (column == 0) {
                    decodedImage[row][column] = differences[row][column];
                } else {
                    decodedImage[row][column] = Pixel.addModulo256(decodedImage[row][column - 1], differences[row][column]);
                }
            }
        }
        return decodedImage;
    }

    /**
     * @param bin - binary string describing pixel
     * @return pixel with differences
     */
    public Pixel binToPixel(String bin) {

        int red = quantizer.intervalNumberToMidpoint(bin.substring(0, k));
        int green = quantizer.intervalNumberToMidpoint(bin.substring(k, 2 * k));
        int blue = quantizer.intervalNumberToMidpoint(bin.substring(2 * k));

        return new Pixel(red, green, blue, true);
    }

}
