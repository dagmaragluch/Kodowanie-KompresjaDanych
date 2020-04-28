import java.io.*;
import java.util.HashMap;

public class ConversionSupport {

    Converter converter = new Converter();

    public String convertFileToString(String fileName) throws IOException {
        InputStream is = new FileInputStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        String textInString = sb.toString();

//        System.out.println("'" + textInString.substring(0, textInString.length() - 1) + "'");
        return textInString.substring(0, textInString.length() - 1);  //remove last (redundant) EOF symbol
    }

    public void writeDecodedTextToFile(StringBuilder buffer, String outputFile) {
        try {
            FileWriter myWriter = new FileWriter(outputFile);
            myWriter.write(buffer.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }

    }

    public void getValuesDescribingTheCompression(String inputFileName, String outputFileName, String text, String binaryString) throws IOException {
        System.out.println("Wartości opisujące kompresję:");

        //wielkość kodowanego pliku
        int sizeOfText = text.length();
        System.out.println("Wielkość kodowanego pliku: " + sizeOfText);

        //wielkośś uzyskanego kodu
        int sizeOfCode = convertFileToString(outputFileName).length() / 8;
        System.out.println("Wielkość uzyskanego kodu: " + sizeOfCode);

        //stopień kompresji = dł przed skompresowaniem/dł po skompresowaniu
        float compressionDegree = (float) sizeOfText / sizeOfCode;

        System.out.println("stopień kompresji: " + compressionDegree);

        //entropia kodowanego tekstu
        byte[] byteArray = converter.convertFileToByteArray(inputFileName);
        HashMap<Byte, Float> mapOfText = converter.getCzestosc(byteArray);
        double textEntropy = converter.calculatingEntropy1(mapOfText);
        System.out.println("entropia kodowanego tekstu: " + textEntropy);

        //entropia uzyskanego kodu
        byte[] byteArray2 = converter.convertFileToByteArray(outputFileName);
        HashMap<Byte, Float> mapOfText2 = converter.getCzestosc(byteArray2);
        double codeEntropy = converter.calculatingEntropy1(mapOfText2);
        System.out.println("entropia uzyskanego kodu: " + codeEntropy);

        //średnia długość kodu
        float averageLengthOfCode = (float) binaryString.length() / text.length();
        System.out.println("średnia długość kodu: " + averageLengthOfCode);
    }
}
