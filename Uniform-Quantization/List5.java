import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class List5 {

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\gluch\\Desktop\\kkd\\testy4\\example0.tga";
        Quantization quantization = new Quantization(fileName);
        BufferedImage oldImage = quantization.image;


        Pixel[][] newPixels = quantization.imageQuantization();
        BufferedImage img = ConverterTGA.getNewImage(newPixels);
        System.out.println(img);


        File outputFile = new File("C:\\Users\\gluch\\Desktop\\kkd\\new-test.tga");
        ImageIO.write(img, "TGA", outputFile);

    }

    public static int bytesCount(int n) {       //teraz BITÓW!!!
//        return n < 0 ? 4 : (32 - Integer.numberOfLeadingZeros(n)) / 8 + 1;
        return n < 0 ? 4 : (32 - Integer.numberOfLeadingZeros(n));
    }
}
