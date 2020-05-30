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

    DifferentialDecoder() throws IOException {
        readFile();
        quantizer = new UniformQuantizer(k);
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Decode");
        DifferentialDecoder decoder = new DifferentialDecoder();
        Pixel[][] diff = decoder.readDifferencesSequence();

//        Pixel p = decoder.binToPixel("100000111");
//        System.out.println(p);

        System.out.println(diff);


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


    public Pixel[][] readDifferencesSequence() throws IOException {

        Pixel[][] differences = new Pixel[height][width];
        int pixelLength = 3 * k;

        int i = 0;
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {

//                Pixel actualPixel = differences[row][column];

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

    public void parseLine(String line) {

        int i = 0;
        while (i < line.length()) {
            String str = line.substring(i, i + (3 * k));

        }


//        for (int i = 0; i < line.length(); i+=(3*k)) {
//        }
    }


}
