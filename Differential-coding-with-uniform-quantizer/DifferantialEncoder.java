import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DifferantialEncoder {

    int k = 3;
    String fileName = "C:\\Users\\gluch\\Desktop\\kkd\\testy4\\example0.tga";

    String format = "%0" + k + "d";

    UniformQuantizer uniformQuantizer = new UniformQuantizer(k);


    public static void main(String[] args) {
        DifferantialEncoder differantialEncoder = new DifferantialEncoder();
        differantialEncoder.encode();
    }


    public void encode() {
        StringBuilder encodedImage = new StringBuilder();
        Pixel[][] differences = getSequenceOfDifferences();

        int height = differences.length;
        int width = differences[0].length;

        String firstLine = k + " " + width + " " + height + "\n";

        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {

                Pixel actualPixel = differences[row][column];

                int redInterval = uniformQuantizer.quantizeDifferent(actualPixel.getRed());
                int greenInterval = uniformQuantizer.quantizeDifferent(actualPixel.getGreen());
                int blueInterval = uniformQuantizer.quantizeDifferent(actualPixel.getBlue());

                encodedImage.append(intervalToBinary(redInterval));
                encodedImage.append(intervalToBinary(greenInterval));
                encodedImage.append(intervalToBinary(blueInterval));
            }
        }
        writeToFile(firstLine, encodedImage);
    }


    public Pixel[][] getSequenceOfDifferences() {
        Predictions predictions = new Predictions(fileName);
        Pixel[][] originImage = predictions.pixels;     //read origin image
        Pixel[][] predictionsArr = predictions.getImageOfPredictions();     //read image prediction W

        Pixel[][] quantizedPredictions = uniformQuantizer.imageQuantization(predictionsArr);   //and quantize it

        //calculate differences
        int height = originImage.length;
        int width = originImage[0].length;
        Pixel[][] differences = new Pixel[height][width];

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                differences[row][column] = Pixel.minus(originImage[row][column], quantizedPredictions[row][column]);
            }
        }
        return differences;
    }


    public String intervalToBinary(int interval) {
        String intervalStr = Integer.toBinaryString(interval);
        intervalStr = String.format(format, Integer.parseInt(intervalStr));
        return intervalStr;
    }

    /**
     * @param firstLine 3 values split by space:
     *                  - parameter k (bits for color)
     *                  - width
     *                  - height
     * @param output    encoded image (one line)
     */
    public void writeToFile(String firstLine, StringBuilder output) {

        try {
            File myObj = new File("encoded-image.txt");
            myObj.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("encoded-image.txt");
            myWriter.write(firstLine);
            myWriter.write(output.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }

    }


}
