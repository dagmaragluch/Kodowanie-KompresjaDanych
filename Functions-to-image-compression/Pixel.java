public class Pixel {

    private final int red;
    private final int green;
    private final int blue;


    public Pixel(int red, int green, int blue) {
//        if (red < 0 || green < 0 || blue < 0 || red > 255 || green > 255 || blue > 255)  //check if value is in range 0-255
//            throw new IllegalArgumentException("Color parameter outside of expected range");
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }


    public static Pixel plus(Pixel p1, Pixel p2) {
        int r = p1.getRed() + p2.getRed();
        int g = p1.getGreen() + p2.getGreen();
        int b = p1.getBlue() + p2.getBlue();

        return new Pixel(r, g, b);
    }

    public static Pixel minus(Pixel p1, Pixel p2) {
        int r = p1.getRed() - p2.getRed();
        int g = p1.getGreen() - p2.getGreen();
        int b = p1.getBlue() - p2.getBlue();

        return new Pixel(r, g, b);
    }

    public static Pixel div2(Pixel p1) {
        int r = p1.getRed() / 2;
        int g = p1.getGreen() / 2;
        int b = p1.getBlue() / 2;

        return new Pixel(r, g, b);
    }

    public static Pixel max(Pixel p1, Pixel p2) {
        int sum1 = p1.getRed() + p1.getGreen() + p1.getBlue();
        int sum2 = p2.getRed() + p2.getGreen() + p2.getBlue();

        if (sum1 >= sum2) return p1;
        else return p2;
    }

    public static Pixel min(Pixel p1, Pixel p2) {
        int sum1 = p1.getRed() + p1.getGreen() + p1.getBlue();
        int sum2 = p2.getRed() + p2.getGreen() + p2.getBlue();

        if (sum1 < sum2) return p1;
        else return p2;
    }

    public static boolean isGreaterOrEqualTo(Pixel p1, Pixel p2) {
        int sum1 = p1.getRed() + p1.getGreen() + p1.getBlue();
        int sum2 = p2.getRed() + p2.getGreen() + p2.getBlue();

        return sum1 >= sum2;
    }


}
