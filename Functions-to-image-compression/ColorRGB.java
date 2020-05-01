public class ColorRGB {

    int red;
    int green;
    int blue;


    public ColorRGB(int red, int green, int blue) {
        if (red < 0 || green < 0 || blue < 0 || red > 255 || green > 255 || blue > 255) {   //check if value is in range 0-255
            throw new IllegalArgumentException("Color parameter outside of expected range");
        } else {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }


    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
