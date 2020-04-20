import java.util.ArrayList;

public class Encoding_LZ77 {

    private final int k = 255;
    private final int n = 256;

    StringBuilder dictionaryBuffer = new StringBuilder(k);
    StringBuilder encodingBuffer = new StringBuilder(n);
    StringBuilder bigBuffer = new StringBuilder();
    ArrayList<Tuple> encodedText = new ArrayList<>();


    public ArrayList<Tuple> encoding(String textToEncoding) {

        initBuffers(textToEncoding);

        while (encodingBuffer.length() != 0) {
            Tuple encodedTuple = oneStepOfEncoding();
            encodedText.add(encodedTuple);
        }
        return encodedText;
    }


    public Tuple oneStepOfEncoding() {
        String dictionary = dictionaryBuffer.toString();
        String ourPattern = encodingBuffer.toString();

        Tuple tupleToCheck = RabinKarpAlgorithm.searchAll(ourPattern, dictionary);

        if (tupleToCheck.getSecond() instanceof Character) {         //(0, letter_code)
            updateBuffers(1);
        } else {                                            //(i, j)
            int c = (int) tupleToCheck.getSecond();
            updateBuffers(c);
        }
        return tupleToCheck;
    }


    public void initBuffers(String textToEncoding) {
        dictionaryBuffer.append(String.valueOf(textToEncoding.charAt(0)).repeat(Math.max(0, dictionaryBuffer.capacity())));

//        dodatkowy warunek w pętli for, bo może zdażyć się sytuacja,
//        gdy długość tekstu do zakodawania jest mniejsza od długości bufora
        for (int i = 0; i < encodingBuffer.capacity() && i < textToEncoding.length() - 1; i++) {
            encodingBuffer.append(textToEncoding.charAt(i + 1));

        }

        bigBuffer.append(dictionaryBuffer);
        bigBuffer.append(encodingBuffer);
        if (n + 1 < textToEncoding.length()) {
            bigBuffer.append(textToEncoding.substring(n + 1));
        }

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

