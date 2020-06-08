import java.io.IOException;
import java.util.ArrayList;

public class HammingEncoder {


    public static void main(String[] args) throws IOException {
        String inputFileName = "panda.jpg";
        Converter converter = new Converter();
        byte[] fileAsBytes = converter.convertFileToByteArray(inputFileName);

        System.out.println(Integer.toBinaryString(fileAsBytes[28]));
        for (int i = 0; i < 8; i++) {
            System.out.print(getBit(fileAsBytes[28], i) + " ");
        }

        ArrayList<int[]> blocks = byteArrayToBlocks(fileAsBytes);
        ArrayList<int[]> encodedBlocks = new ArrayList<>();

        for (int[] block : blocks) {
            encodedBlocks.add(codeBlock(block));
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
        int[][] G = {
                new int[]{1, 1, 0, 1},
                new int[]{1, 0, 1, 1},
                new int[]{1, 0, 0, 0},
                new int[]{0, 1, 1, 1},
                new int[]{0, 1, 0, 0},
                new int[]{0, 1, 1, 0},
                new int[]{0, 0, 0, 1},
                new int[]{1, 1, 1, 0},
        };
        return G;
    }


    public static int[] codeBlock(int[] block) {
        int[][] G = createMatrixG();
        int[] result = new int[8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                result[i] += G[i][j] * block[j];
            }
        }
        for (int i = 0; i < 8; i++) {
            result[i] = result[i] % 2;
        }
        return result;
    }


}
