import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Encoding_LZ77 encoding_lz77 = new Encoding_LZ77();
        Decoding_LZ77 decoding_lz77 = new Decoding_LZ77();

//        String textToEncoding = "wabba-wabba-wabba-woo-woo-woo";
//
        String textToEncoding = "Czy ten program działa???? Tak, działa! działa ten program. Jest super, super-extra i się cieszę bardzo bardzo! Ooo xD";

//        String textToEncoding = "wabba-wabbbba";

        if(textToEncoding.length()<=1){
            throw new IllegalArgumentException("Text to encoding must be longer than 1!");
        }


        ArrayList<Tuple> encodedText = encoding_lz77.encoding(textToEncoding);

        for (Tuple tuple : encodedText) Tuple.printTuple(tuple);

        decoding_lz77.decoding(encodedText);



    }
}
