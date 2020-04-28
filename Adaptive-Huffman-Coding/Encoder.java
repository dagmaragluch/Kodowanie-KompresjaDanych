import java.io.*;

public class Encoder {

    public static void main(String[] args) throws IOException {

        HuffmanTree huffmanTree = new HuffmanTree();
        ConversionSupport conversion = new ConversionSupport();

        String inputFile = args[0];
        String text = conversion.convertFileToString(inputFile);
        String outputFile = args[1];


        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);

            if (!huffmanTree.isValueInTree(symbol)) {            //first occurrence of value
                huffmanTree.insertNewValue(symbol);
                buffer.append(huffmanTree.generateCodeNYT());       //add actual NYT code to output buffer
                buffer.append(huffmanTree.generateOriginSymbolCode(symbol));    //add ASCII code of new symbol

            } else {        //value is already in Tree
                buffer.append(huffmanTree.generateEncodingSymbolCode(symbol));      //add encoded code of symbol to output buffer
                huffmanTree.insertOldValue(symbol);     //najpierw wysyłamy kod, a dopiero później przebudowujemy drzewo !
            }
        }
//        System.out.println("ENCODE: " + buffer.toString());

        conversion.writeDecodedTextToFile(buffer, outputFile);

        conversion.getValuesDescribingTheCompression(inputFile, outputFile, text, buffer.toString());
    }

}
