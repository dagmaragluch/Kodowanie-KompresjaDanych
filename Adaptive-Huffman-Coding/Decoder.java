import java.util.ArrayList;

public class Decoder {

    public static void main(String[] args) {

        HuffmanTree huffmanTree = new HuffmanTree();

        String text = "01000001001000010101";
//        String text = "010000010010000100001000011";
        StringBuilder buffer = new StringBuilder();
        ArrayList<Integer> textInArr = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '1')
                textInArr.add(1);
            else
                textInArr.add(0);
        }

        if (huffmanTree.isEmpty()) {     //read first symbol
            String codeASCII = text.substring(0, 8);
            char newSymbol = huffmanTree.getSymbolFromASCIICode(codeASCII);
            buffer.append(newSymbol);
            huffmanTree.insertNewValue(newSymbol);
            huffmanTree.generateCodeNYT();  //set codeNYT to '0'
        }


        int i = 8;

        while (i < text.length()) {
            if (isNextCodeNYT(huffmanTree, text, i)) {
                int actualNYTLength = huffmanTree.codeNYT.length();
                String codeASCII = text.substring(i + actualNYTLength, i + actualNYTLength + 8);
                char newSymbol = huffmanTree.getSymbolFromASCIICode(codeASCII);
                buffer.append(newSymbol);
                huffmanTree.insertNewValue(newSymbol);
                i = i + actualNYTLength + 8;   //read codeNYT and 1 symbol (8 bits)
                huffmanTree.generateCodeNYT();  //update codeNYT
            } else {
                Tuple tuple = findSymbolInTree(huffmanTree, textInArr, i);
                char newSymbol = (char) tuple.getFirst();
                int readBits = (int) tuple.getSecond();
                buffer.append(newSymbol);
                i += readBits;
                huffmanTree.insertOldValue(newSymbol);
            }
        }


        System.out.println("DECODE: " + buffer.toString());

    }


    public static boolean isNextCodeNYT(HuffmanTree tree, String text, int i) {
        String actualCodeNYT = tree.codeNYT.toString();
        String stringToCheck = text.substring(i, i + actualCodeNYT.length());
        return Integer.parseInt(actualCodeNYT) == Integer.parseInt(stringToCheck);
    }


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
