import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Sprawdz {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) throw new IllegalArgumentException("Required 2 arguments!");
        String in1 = args[0];
        String in2 = args[1];
        int errorCounter = 0;

        Converter converter = new Converter();
        byte[] bytes1 = converter.convertFileToByteArray(in1);
        byte[] bytes2 = converter.convertFileToByteArray(in2);

        ArrayList<int[]> blocks1 = Koder.byteArrayToBlocks(bytes1);
        ArrayList<int[]> blocks2 = Koder.byteArrayToBlocks(bytes2);

        for (int i = 0; i < blocks1.size(); i++) {
            if (!Arrays.equals(blocks1.get(i), blocks2.get(i))) {
                errorCounter++;
            }
        }
        System.out.println("Liczba różniących się bloków: " + errorCounter);
    }


}
