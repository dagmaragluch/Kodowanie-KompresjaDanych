import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class List5 {

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\gluch\\Desktop\\kkd\\testy4\\example0.tga";
        Quantization quantization = new Quantization(fileName);
        BufferedImage oldImage = quantization.image;

//        Pixel p = new Pixel(74, 23, 42);
//        ConverterTGA.convertColorToInteger(p);


//        Pixel[][] newPixels = quantization.imageQuantization();
//        BufferedImage img = ConverterTGA.getNewImage(oldImage, newPixels);
//        System.out.println(img);


        File outputFile = new File("C:\\Users\\gluch\\Desktop\\kkd\\test2.tga");
//        ImageIO.write(img, "TGA", outputFile);
        ImageIO.write(oldImage, "TGA", outputFile);

//        System.out.println(Integer.toBinaryString(-10477536));


    }

    public static int bytesCount(int n) {       //teraz BITÓW!!!
//        return n < 0 ? 4 : (32 - Integer.numberOfLeadingZeros(n)) / 8 + 1;
        return n < 0 ? 4 : (32 - Integer.numberOfLeadingZeros(n));
    }
}
