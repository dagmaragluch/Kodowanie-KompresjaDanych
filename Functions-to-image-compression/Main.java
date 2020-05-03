import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        CalculationEntropy entropy = new CalculationEntropy();
        Predictions predictions = new Predictions(args[0]);
        int optimalMethod = 1;

        Pixel[][] img0 = predictions.pixels;

        /** entropy of input image*/
        HashMap<Pixel, Float> mapOfPixels0 = entropy.getCzestoscOfPixels(img0);
        HashMap<Integer, Float> mapOfRed0 = entropy.getCzestoscOfColor(img0, 1);
        HashMap<Integer, Float> mapOfGreen0 = entropy.getCzestoscOfColor(img0, 2);
        HashMap<Integer, Float> mapOfBlue0 = entropy.getCzestoscOfColor(img0, 3);
        double entropyPixels0 = entropy.calculatingEntropyOfPixels(mapOfPixels0);
        double minEntropy = entropyPixels0;
        double entropyRed0 = entropy.calculatingEntropyOfColor(mapOfRed0);
        double entropyGreen0 = entropy.calculatingEntropyOfColor(mapOfGreen0);
        double entropyBlue0 = entropy.calculatingEntropyOfColor(mapOfBlue0);
        System.out.println("entropia obrazu wejściowego: " + entropyPixels0);
        System.out.println("entropie składowych kolorów:");
        System.out.println("    R: " + entropyRed0);
        System.out.println("    G: " + entropyGreen0);
        System.out.println("    B: " + entropyBlue0);
        System.out.println();

        /** entropy of predictions-images */
        for (int i = 1; i <= 8; i++) {
            Pixel[][] img = predictions.getImageOfPredictions(i);
            HashMap<Pixel, Float> mapOfPixels = entropy.getCzestoscOfPixels(img);
            HashMap<Integer, Float> mapOfRed = entropy.getCzestoscOfColor(img, 1);
            HashMap<Integer, Float> mapOfGreen = entropy.getCzestoscOfColor(img, 2);
            HashMap<Integer, Float> mapOfBlue = entropy.getCzestoscOfColor(img, 3);
            double entropyPixels = entropy.calculatingEntropyOfPixels(mapOfPixels);
            if (entropyPixels < minEntropy) {
                minEntropy = entropyPixels;
                optimalMethod = i;
            }

            double entropyRed = entropy.calculatingEntropyOfColor(mapOfRed);
            double entropyGreen = entropy.calculatingEntropyOfColor(mapOfGreen);
            double entropyBlue = entropy.calculatingEntropyOfColor(mapOfBlue);
            System.out.println("Sposób liczenia predykatów: " + i);
            System.out.println("entropia obrazu pochodnego: " + entropyPixels);
            System.out.println("entropie składowych kolorów:");
            System.out.println("    R: " + entropyRed);
            System.out.println("    G: " + entropyGreen);
            System.out.println("    B: " + entropyBlue);
            System.out.println();
        }

        System.out.println("Minimalna entropia: " + minEntropy);
        System.out.println("Optymalna metoda: " + optimalMethod);
    }

}
