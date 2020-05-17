import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class List5 {

    public static void main(String[] args) throws IOException {

        if (args.length < 5) {
            throw new IllegalArgumentException("Required 5 arguments!");
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        int bitsForRed = Integer.parseInt(args[2]);
        int bitsForGreen = Integer.parseInt(args[3]);
        int bitsForBlue = Integer.parseInt(args[4]);

        if (bitsForRed < 0 || bitsForRed > 8 || bitsForGreen < 0 || bitsForGreen > 8 || bitsForBlue < 0 || bitsForBlue > 8) {
            throw new IllegalArgumentException("Value of bits must be in range [0, 8]");
        }

        Quantization quantization = new Quantization(inputFileName, bitsForRed, bitsForGreen, bitsForBlue);

        Pixel[][] newPixels = quantization.imageQuantization();
        BufferedImage img = ConverterTGA.getNewImage(newPixels);

        quantization.mseAndSNR(quantization.pixels, newPixels);


        File outputFile = new File(outputFileName);
        ImageIO.write(img, "TGA", outputFile);

    }

}
