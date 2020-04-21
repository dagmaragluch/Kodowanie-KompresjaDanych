package lista1;

import lista3.Tuple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class List1 {

    public static void main(String[] args) throws IOException {

        String fileName = args[0];
        Converter converter = new Converter();

        byte[] fileAsBytes = converter.convertFileToByteArray(fileName);

        // ENTROPIA
        HashMap<Byte, Float> mapNormal = converter.getCzestosc(fileAsBytes);
        double entropy = converter.calculatingEntropy1(mapNormal);
        System.out.println("Entropia: " + entropy);


        // ENTROPIA WARUNKOWA
        ArrayList<Tuple> tuples = converter.createListOfSymbolPairs(fileAsBytes);
        HashMap<Tuple, Float> mapConditional = converter.getCzestoscWarunkowa(tuples);
        double entropyConditional = converter.calculatingConditionalEntropy(mapConditional, mapNormal);
        System.out.println("Entropia warunkowa: " + entropyConditional);
    }

}
