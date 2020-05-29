public class DifferantialEncoder {

    int k = 7;
    String fileName = "C:\\Users\\gluch\\Desktop\\kkd\\testy4\\example0.tga";

    UniformQuantizer uniformQuantizer = new UniformQuantizer(k);


    public Pixel[][] getSequenceOfDifferences() {
        Predictions predictions = new Predictions(fileName);

        //read origin image
        Pixel[][] originImage = predictions.pixels;

        //read image prediction W and quantize it
        Pixel[][] differences = predictions.getImageOfPredictions();

        Pixel[][] quantizedPredictions = uniformQuantizer.imageQuantization(differences);

        //calculate differences
        int width = originImage.length;
        int height = originImage[0].length;
        Pixel[][] newImage = new Pixel[width][height];

        for (int row = 0; row < width; row++) {
            for (int column = 0; column < height; column++) {

                newImage[row][column] = Pixel.minus(originImage[row][column], quantizedPredictions[row][column]);

            }
        }

        return newImage;
    }


    public static void main(String[] args) {
        DifferantialEncoder differantialEncoder = new DifferantialEncoder();

        Pixel[][] diff = differantialEncoder.getSequenceOfDifferences();

//        System.out.println(diff);

    }


}
