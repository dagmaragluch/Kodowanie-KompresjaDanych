public class Encoder {

    public static void main(String[] args) {

        HuffmanTree huffmanTree = new HuffmanTree();

        String text = "ABBA";
//        String text = "ABC";
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
//            huffmanTree.insertIntoTree(symbol);
            if (!huffmanTree.isValueInTree(symbol)) {
                huffmanTree.insertNewValue(symbol);
                buffer.append(huffmanTree.generateCodeNYT());
                buffer.append(huffmanTree.generateOriginSymbolCode(symbol));
            } else {
                huffmanTree.insertOldValue(symbol);
                buffer.append(huffmanTree.generateEncodingSymbolCode(symbol));
            }
        }

        System.out.println("ENCODE: " + buffer.toString());

    }
}
