import java.io.IOException;

public class DescribingCompression {

    /**
     * encoded image to binary sequence,save it,
     * decoded and calculate mse and SNR
     *
     * @param args: input file name and number of bots for color
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) throw new IllegalArgumentException("Required 2 arguments!");
        String inputFileName = args[0];
        int bitsForColor = Integer.parseInt(args[1]);

        if (bitsForColor < 1 || bitsForColor > 7)
            throw new IllegalArgumentException("Value of bits must be in range [1, 7]");

        DifferentialEncoder encoder = new DifferentialEncoder(inputFileName, bitsForColor);
        encoder.encode();
        String encodedFileName = "encoded-image.txt";
        DifferentialDecoder decoder = new DifferentialDecoder(encodedFileName);
        decoder.decode();

        Quantization quantization = new Quantization(inputFileName, 8, 8, 8);
        Pixel[][] oldImage = quantization.pixels;
        Pixel[][] newImage = decoder.newImage;
        quantization.mseAndSNR(oldImage, newImage);
    }

}
