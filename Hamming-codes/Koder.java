import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Koder {

    public static int[][] G = createMatrixG();

    public static void main(String[] args) throws IOException {

        if (args.length < 2) throw new IllegalArgumentException("Required 2 arguments!");

        String inputFileName = args[0];
        String outputFileName = args[1];
        Converter converter = new Converter();
        byte[] fileAsBytes = converter.convertFileToByteArray(inputFileName);

        ArrayList<int[]> blocks = byteArrayToBlocks(fileAsBytes);
        StringBuilder encodedBlocks = new StringBuilder();

        for (int[] block : blocks) {
            int[] encodedBlock = codeBlock(block);
            for (int i = 0; i < block.length; i++) {
                encodedBlocks.append(encodedBlock[i]);
            }
        }

        byte[] bytes = String.valueOf(encodedBlocks).getBytes();
        try (FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.write(bytes);
        }
    }


    private static int getBit(byte b, int bit) {
        return ((b & (1 << bit)) != 0) ? 1 : 0;
    }

    public static ArrayList<int[]> byteArrayToBlocks(byte[] byteArray) {
        ArrayList<int[]> blocks = new ArrayList<>();
        for (byte b : byteArray) {
            // revert blocks - 7 is 0000 0111
            // first block form byte
            int[] block = new int[4];
            block[0] = getBit(b, 7);
            block[1] = getBit(b, 6);
            block[2] = getBit(b, 5);
            block[3] = getBit(b, 4);
            blocks.add(block);
            // second block  form byte
            block = new int[4];
            block[0] = getBit(b, 3);
            block[1] = getBit(b, 2);
            block[2] = getBit(b, 1);
            block[3] = getBit(b, 0);
            blocks.add(block);
        }
        return blocks;
    }


    public static int[][] createMatrixG() {
        return new int[][]{
                new int[]{1, 1, 0, 1},
                new int[]{1, 0, 1, 1},
                new int[]{1, 0, 0, 0},
                new int[]{0, 1, 1, 1},
                new int[]{0, 1, 0, 0},
                new int[]{0, 1, 1, 0},
                new int[]{0, 0, 0, 1},
                new int[]{1, 1, 1, 0},
        };
    }

    public static int[] codeBlock(int[] block) {
        int[] result = new int[8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                result[i] += G[i][j] * block[j];
            }
        }
        for (int i = 0; i < 8; i++) {   // modulo 2
            result[i] = result[i] % 2;
        }
        return result;
    }

}
