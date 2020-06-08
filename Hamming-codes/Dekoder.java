import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Dekoder {

    public static int[][] H = createMatrixH();
    public static int doubleErrorsCounter = 0;

    public static void main(String[] args) throws IOException {
        if (args.length < 2) throw new IllegalArgumentException("Required 2 arguments!");

        String inputFileName = args[0];
        String outputFileName = args[1];

        String encodedFile = Converter.readAllBytes(inputFileName);
        ArrayList<int[]> blocks = byteStringToBlocks(encodedFile);

        StringBuilder encodedBlocks = new StringBuilder();

        for (int[] block : blocks) {
            int[] syndrome = getSyndrome(block);
            block = checkAndCorrectError(syndrome, block);

            for (int i = 0; i < block.length; i++) {
                encodedBlocks.append(block[i]);
            }
        }

        byte[] bytes = String.valueOf(encodedBlocks).getBytes();
        try (FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.write(bytes);
        }

        System.out.println("Liczba podwójnych błędów: " + doubleErrorsCounter);
    }


    public static int[][] createMatrixH() {
        return new int[][]{
                new int[]{1, 0, 1, 0, 1, 0, 1, 0},
                new int[]{0, 1, 1, 0, 0, 1, 1, 0},
                new int[]{0, 0, 0, 1, 1, 1, 1, 0},
                new int[]{1, 1, 1, 1, 1, 1, 1, 1},
        };
    }

    public static ArrayList<int[]> byteStringToBlocks(String byteString) {
        ArrayList<int[]> blocks = new ArrayList<>();
        int[] block = new int[8];

        for (int i = 0; i < byteString.length(); i++) {
            block[i % 8] = Character.getNumericValue(byteString.charAt(i));
            if (i % 8 == 7) {
                blocks.add(block);
                block = new int[8];
            }
        }
        return blocks;
    }


    public static int[] getSyndrome(int[] block) {
        int[] result = new int[4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                result[i] += H[i][j] * block[j];
            }
        }
        for (int i = 0; i < 4; i++) {   // modulo 2
            result[i] = result[i] % 2;
        }
        return result;
    }


    public static int[] checkAndCorrectError(int[] syndrome, int[] block) {

        if (syndrome[3] == 1) {   //check last bit
            String number = String.valueOf(syndrome[2]) + String.valueOf(syndrome[1]) + String.valueOf(syndrome[0]);
            int bitToChange = Integer.parseInt(number, 2);

            if (block[bitToChange] == 0) {
                block[bitToChange] = 1;
            } else
                block[bitToChange] = 0;
        } else {
            if (!(syndrome[2] == 0 && syndrome[1] == 0 && syndrome[0] == 0)) {
                doubleErrorsCounter++;
            }
        }
        return block;
    }

}
