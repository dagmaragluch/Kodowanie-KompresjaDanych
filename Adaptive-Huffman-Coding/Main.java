import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        HuffmanTree huffmanTree = new HuffmanTree();

        String text = "ABC";
        ArrayList<Integer> textInArray = new ArrayList<>();

        //Integer.toBinaryString(character...);

        for (int i = 0; i < text.length(); i++) {
            textInArray.add(Integer.parseInt(Integer.toBinaryString(text.charAt(i))));
        }


        huffmanTree.insertIntoTree(textInArray.get(0));
        huffmanTree.insertIntoTree(textInArray.get(1));
        huffmanTree.insertIntoTree(textInArray.get(1));
        huffmanTree.insertIntoTree(textInArray.get(2));
        huffmanTree.insertIntoTree(textInArray.get(2));

        System.out.println("hello");


    }
}
