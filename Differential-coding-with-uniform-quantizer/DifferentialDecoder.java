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


    public void decode() throws IOException {

        Pixel[][] differences = readDifferencesSequence();
        Pixel[][] newImage = decodeImage(differences);

        BufferedImage img = ConverterTGA.getNewImage(newImage);

        String outputFileName = "decoded-image.tga";

        File outputFile = new File(outputFileName);
        ImageIO.write(img, "TGA", outputFile);

    }


    public void readFile() throws IOException {
        BufferedReader brTest = new BufferedReader(new FileReader(fileName));
        String[] strings = brTest.readLine().split(" ");

        k = Integer.parseInt(strings[0]);
        width = Integer.parseInt(strings[1]);
        height = Integer.parseInt(strings[2]);
        image = brTest.readLine();
    }


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


    public Pixel binToPixel(String bin) {
//        System.err.println(bin.substring(0, k));
        int red = quantizer.numberOfIntervalToMidpoint(bin.substring(0, k));
        int green = quantizer.numberOfIntervalToMidpoint(bin.substring(k, 2 * k));
        int blue = quantizer.numberOfIntervalToMidpoint(bin.substring(2 * k));

        return new Pixel(red, green, blue, true);
    }

}
