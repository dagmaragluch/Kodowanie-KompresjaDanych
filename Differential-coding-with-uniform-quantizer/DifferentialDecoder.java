import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DifferentialDecoder {

    String fileName = "C:\\Users\\gluch\\Desktop\\kkd\\Kodowanie-KompresjaDanych\\encoded-image.txt";
    int k;
    UniformQuantizer quantizer;

    DifferentialDecoder() throws IOException {
        k = readParameterK();
        quantizer = new UniformQuantizer(k);
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Decode");
        DifferentialDecoder decoder = new DifferentialDecoder();
//        decoder.readDifferencesSequence();

        Pixel p = decoder.binToPixel("100100100");
        System.out.println(p);


    }


    public int readParameterK() throws IOException {
        BufferedReader brTest = new BufferedReader(new FileReader(fileName));
        String firstLine = brTest.readLine();
//        System.out.println("Firstline is : " + firstLine);

        return Integer.parseInt(firstLine);
    }


    public Pixel[][] readDifferencesSequence() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        return null;
    }


    public Pixel binToPixel(String bin) {
        int red = quantizer.numberOfIntervalToMidpoint(bin.substring(0, k));
        int green = quantizer.numberOfIntervalToMidpoint(bin.substring(k, 2 * k));
        int blue = quantizer.numberOfIntervalToMidpoint(bin.substring(2 * k));

        return new Pixel(red, green, blue, true);
    }

//    public void calculateNewImage(){}


}
