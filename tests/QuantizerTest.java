import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class QuantizerTest {

    int k = 1;
    UniformQuantizer quantizer = new UniformQuantizer(k);
    DifferentialDecoder decoder = new DifferentialDecoder("C:\\Users\\gluch\\Desktop\\kkd\\Kodowanie-KompresjaDanych\\encoded-image.txt");

    public QuantizerTest() throws IOException {
    }


    @Test
    public void calculateDiffTest() {
        int[] diff = {10, -7, 3, 5, -6};
        int[] expected = {10, 3, 6, 11, 5};
        int[] result = new int[5];

        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                result[i] = diff[i];
            } else {
                result[i] = result[i - 1] + diff[i];
            }
            System.out.print(result[i] + "  ");
        }
        assertArrayEquals(expected, result);
    }


    @Test
    public void quantizeDiffTest() {
        int diff1 = 15;
        int diff2 = -20;
        int diff3 = 0;
        int diff4 = 64;
        int diff5 = 250;

        assertEquals(0, quantizer.quantizeDifferent(diff1));
        assertEquals(1, quantizer.quantizeDifferent(diff2));
        assertEquals(0, quantizer.quantizeDifferent(diff3));
        assertEquals(0, quantizer.quantizeDifferent(diff4));
        assertEquals(1, quantizer.quantizeDifferent(diff5));
    }


    @Test
    public void binToPixelTest(){
        String bin = "110";
        Pixel expected = new Pixel(192, 192, 64);
        Pixel result = decoder.binToPixel(bin);
        assertEquals(expected.getRed(), result.getRed());
        assertEquals(expected.getGreen(), result.getGreen());
        assertEquals(expected.getBlue(), result.getBlue());
    }


    @Test
    public void numberOfIntervalToMidpointTest(){

        int result1 = quantizer.numberOfIntervalToMidpoint("0");
        int result2 = quantizer.numberOfIntervalToMidpoint("1");

        assertEquals(64, result1);
        assertEquals(192, result2);
    }

    @Test
    public void smallTest(){
        String bin = "110";
        int red = quantizer.numberOfIntervalToMidpoint(bin.substring(0, k));

        assertEquals(192, red);
    }



}
