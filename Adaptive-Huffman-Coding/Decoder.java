import java.io.IOException;
import java.util.ArrayList;

public class Decoder {

    public static void main(String[] args) throws IOException {

        HuffmanTree huffmanTree = new HuffmanTree();
        ConversionSupport conversion = new ConversionSupport();

        String inputFile = args[0];
        String text = conversion.convertFileToString(inputFile);
        String outputFile = args[1];


        StringBuilder buffer = new StringBuilder();
        ArrayList<Integer> textInArr = new ArrayList<>();

        /** fill ArrayList with symbols 0 and 1, that represent encoded text */
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '1')
                textInArr.add(1);
            else
                textInArr.add(0);
        }

        /**first symbol is always new - we read his ASCII code*/
        if (huffmanTree.isEmpty()) {     //read first symbol
            String codeASCII = text.substring(0, 8);
            char newSymbol = huffmanTree.getSymbolFromASCIICode(codeASCII);
            buffer.append(newSymbol);
            huffmanTree.insertNewValue(newSymbol);
            huffmanTree.generateCodeNYT();  //set codeNYT to '0'
        }


        int i = 8;  //we already read first 8 bits

        while (i < text.length()) {
            /**check if current binary sequence is NYT code
             * if yes - it's new symbol, and we get read code NYT and next his ASCII code */
            if (isNextCodeNYT(huffmanTree, text, i)) {
                int actualNYTLength = huffmanTree.codeNYT.length();
                String codeASCII = text.substring(i + actualNYTLength, i + actualNYTLength + 8);
                char newSymbol = huffmanTree.getSymbolFromASCIICode(codeASCII);
                buffer.append(newSymbol);
                huffmanTree.insertNewValue(newSymbol);      //insert new value into Huffman Tree
                i += actualNYTLength + 8;   //read codeNYT and 1 symbol (8 bits)
                huffmanTree.generateCodeNYT();  //update codeNYT

                /** current binary sequence isn't NYT code;
                 * we are looking for encoded code of symbol */
            } else {
                Tuple tuple = findSymbolInTree(huffmanTree, textInArr, i);
                char newSymbol = (char) tuple.getFirst();       //char value of symbol
                int readBits = (int) tuple.getSecond();         //length of read binary sequence
                buffer.append(newSymbol);
                i += readBits;
                huffmanTree.insertOldValue(newSymbol);      //insert symbol into Huffman Tree - update tree
            }
        }
//        System.out.println("DECODE: " + buffer.toString());

        conversion.writeDecodedTextToFile(buffer, outputFile);

    }


    public static boolean isNextCodeNYT(HuffmanTree tree, String text, int i) {
        String actualCodeNYT = tree.codeNYT.toString();
        if (i + actualCodeNYT.length() < text.length()) {
            String stringToCheck = text.substring(i, i + actualCodeNYT.length());
            return Integer.parseInt(actualCodeNYT) == Integer.parseInt(stringToCheck);
        } else return false;
    }


    /**
     * find char value of symbol using Huffman Tree:
     * - start from root and go to leaf
     * - if next bit is 1 - go to right child, otherwise to left
     * - because use codes are unique prefix code, we have only one option
     * to find leaf
     */
    public static Tuple findSymbolInTree(HuffmanTree tree, ArrayList<Integer> arr, int startIndex) {
        int i = startIndex;
        Node actualNode = tree.root;

        while (actualNode.getNodeType() != Node.NodeType.LEAF) {
            if (arr.get(i) == 1) {
                actualNode = actualNode.rightChild;
            } else {
                actualNode = actualNode.leftChild;
            }
            i++;
        }
        int readBits = i - startIndex;

        return new Tuple(actualNode.getValue(), readBits);
    }

}
