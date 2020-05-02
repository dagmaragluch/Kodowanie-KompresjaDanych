import java.util.HashMap;
import java.util.Map;

public class CalculationEntropy {


    public HashMap<Pixel, Float> getCzestoscOfPixels(Pixel[][] array) {
        HashMap<Pixel, Float> map = new HashMap<>();
        int all = array.length * array[0].length;

        for (int r = 0; r < array.length; r++) {
            for (int c = 0; c < array[0].length; c++) {
                Pixel keyToBeChecked = array[r][c];
                Pixel whatPixelIsInMap = isPixelInMap(map, keyToBeChecked);
                //klucza nie ma w mapie i trzeba go dodać
                if (whatPixelIsInMap == null) {
                    map.put(keyToBeChecked, (float) (1.0 / all));
                } else {             //klucz jest w mapie i zwiększamy il wystąpień
                    Float actualValue = map.get(whatPixelIsInMap);
                    Float newValue = actualValue + (float) (1.0 / all);
                    map.replace(keyToBeChecked, newValue);
                }
            }
        }
        return map;
    }

    public HashMap<Integer, Float> getCzestoscOfColor(Pixel[][] array, int colorNumber) {
        HashMap<Integer, Float> map = new HashMap<>();
        int all = array.length * array[0].length;
        Integer keyToBeChecked;

        for (int r = 0; r < array.length; r++) {
            for (int c = 0; c < array[0].length; c++) {
                if (colorNumber == 1)
                    keyToBeChecked = array[r][c].getRed();
                else if (colorNumber == 2)
                    keyToBeChecked = array[r][c].getGreen();
                else
                    keyToBeChecked = array[r][c].getBlue();

                boolean isKeyInMap = map.containsKey(keyToBeChecked);
                //klucza nie ma w mapie i trzeba go dodać
                if (!isKeyInMap) {
                    map.put(keyToBeChecked, (float) (1.0 / all));
                } else {             //klucz jest w mapie i zwiększamy il wystąpień
                    Float actualValue = map.get(keyToBeChecked);
                    Float newValue = actualValue + (float) (1.0 / all);
                    map.replace(keyToBeChecked, newValue);
                }
            }
        }
        return map;
    }


    public double calculatingEntropyOfColor(HashMap<Integer, Float> map) {
        double H = 0.0;
        for (Map.Entry<Integer, Float> entry : map.entrySet()) {
            float P = entry.getValue();
            H = H + P * Converter.log2(P);
        }
        H *= -1;
        return H;
    }

    public double calculatingEntropyOfPixels(HashMap<Pixel, Float> map) {
        double H = 0.0;
        for (Map.Entry<Pixel, Float> entry : map.entrySet()) {
            float P = entry.getValue();
            H = H + P * Converter.log2(P);
        }
        H *= -1;
        return H;
    }

    public Pixel isPixelInMap(HashMap<Pixel, Float> map, Pixel pixelToCheck) {
        for (Pixel pixel : map.keySet()) {
            if (Pixel.isEquals(pixel, pixelToCheck)) {
                return pixel;
            }
        }
        return null;
    }


}
