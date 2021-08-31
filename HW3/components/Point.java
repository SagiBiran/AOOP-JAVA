package components;

/**
 * Point Class will helps to prints components ii GUI with perfect accurate due x && y coordinates
 *
 * @author Sagi Biran , ID: 205620859
 */
public class Point {
    /**
     * x coordinate of component
     */
    private int x;
    /**
     * x coordinate of component
     */
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
