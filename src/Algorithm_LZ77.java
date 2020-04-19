import java.util.ArrayList;

public class Algorithm_LZ77 {

//    int k = 255;
//    int n = 256;

    int k = 7;
    int n = 8;

    StringBuilder dictionaryBuffer = new StringBuilder(k);
    StringBuilder encodingBuffer = new StringBuilder(n);
    StringBuilder bigBuffer = new StringBuilder();
    ArrayList<Tuple> encodedText = new ArrayList<>();


    public void encoding(String textToEncoding) {

     initBuffers(textToEncoding);

        while (encodingBuffer.length() != 0) {
            Tuple encodedTuple = oneStepOfEncoding();
            encodedText.add(encodedTuple);
        }


    }


    public Tuple oneStepOfEncoding() {
        String dictionary = dictionaryBuffer.toString();
        String ourPattern = encodingBuffer.toString();

        Tuple tupleToCheck = RabinKarpAlgorithm.searchAll(ourPattern, dictionary);

        if ((Integer) tupleToCheck.getFirst() == 0) {
            updateBuffers(1);
        } else {
            int c = (int) tupleToCheck.getSecond();
            updateBuffers(c);
        }

        System.out.println("first = " + tupleToCheck.getFirst().toString() + "   second = " + tupleToCheck.getSecond().toString());
        return tupleToCheck;
    }


    public void initBuffers(String textToEncoding) {
        dictionaryBuffer.append(String.valueOf(textToEncoding.charAt(0)).repeat(Math.max(0, dictionaryBuffer.capacity())));

        for (int i = 0; i < encodingBuffer.capacity(); i++)
            encodingBuffer.append(textToEncoding.charAt(i + 1));

        bigBuffer.append(dictionaryBuffer);
        bigBuffer.append(encodingBuffer);
        bigBuffer.append(textToEncoding.substring(n));

        encodedText.add(new Tuple(0, textToEncoding.charAt(0)));

    }

    public void updateBuffers(int j) {
        dictionaryBuffer = new StringBuilder(bigBuffer.substring(j, j + k));

        if (j + k + n < bigBuffer.length()) {
            encodingBuffer = new StringBuilder(bigBuffer.substring(j + k, j + k + n));
        } else {
            encodingBuffer = new StringBuilder(bigBuffer.substring(j + k));
        }
        bigBuffer = new StringBuilder(bigBuffer.substring(j));
    }


}

