public class Encoder {

    public static void main(String[] args) {

        HuffmanTree huffmanTree = new HuffmanTree();

        String text = "DAGMARA MARIA";

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
//            huffmanTree.insertIntoTree(symbol);
            if (!huffmanTree.isValueInTree(symbol)) {
                huffmanTree.insertNewValue(symbol);
                buffer.append(huffmanTree.generateCodeNYT());
                buffer.append(huffmanTree.generateOriginSymbolCode(symbol));
            } else {
                buffer.append(huffmanTree.generateEncodingSymbolCode(symbol));      //najpierw wysyłamy kod, a dopiero później przebudowujemy drzewo
                huffmanTree.insertOldValue(symbol);
            }
        }

        System.out.println("ENCODE: " + buffer.toString());

    }
}
