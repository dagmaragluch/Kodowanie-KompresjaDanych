public class Pixel {

    private int row;
    private int column;
    ColorRGB color;

    public Pixel(int row, int column, ColorRGB color){
        this.row = row;
        this.column  =column;
        this.color = color;
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public ColorRGB getColor() {
        return color;
    }

    public void setColor(ColorRGB color) {
        this.color = color;
    }
}
