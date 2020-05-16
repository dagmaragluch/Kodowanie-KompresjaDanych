import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class List5 {

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\gluch\\Desktop\\kkd\\testy4\\example0.tga";
//        Integer a = 2;
//        Integer b = 15;
//        Integer c = 31;
//        System.out.println(bytesCount(a));
//        System.out.println(bytesCount(b));
//        System.out.println(bytesCount(c));

        Quantization quantization = new Quantization(fileName);

//        Pixel p = new Pixel(74, 23, 42);
//        ConverterTGA.convertColorToInteger(p);

        Pixel[][] newPixels = quantization.imageQuantization();
        BufferedImage img = ConverterTGA.getNewImage(quantization.image, newPixels);
        System.out.println(img);

        File outputFile = new File("C:\\Users\\gluch\\Desktop\\kkd\\image.tga");
        ImageIO.write(img, "TGA", outputFile);


    }

    public static int bytesCount(int n) {       //teraz BITÓW!!!
//        return n < 0 ? 4 : (32 - Integer.numberOfLeadingZeros(n)) / 8 + 1;
        return n < 0 ? 4 : (32 - Integer.numberOfLeadingZeros(n));
    }
}
