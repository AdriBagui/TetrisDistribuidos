package classictetris;

import java.awt.*;
import java.util.ArrayList;

// ==========================================
// ABSTRACT CLASS: Block
// Base class for all Tetris shapes
// ==========================================
public abstract class Block {
    protected int[][] shape;
    protected int x, y;
    protected int colorIndex;

    public Block() {
        initShape();
    }

    protected abstract void initShape();

    public int[][] getShape() { return shape; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getColorIndex() { return colorIndex; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void moveDown() { y++; }
    public void moveLeft() { x--; }
    public void moveRight() { x++; }

    public void rotate() {
        shape = getNextRotation();
    }

    // Returns what the shape would look like if rotated
    public int[][] getNextRotation() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotated = new int[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rotated[c][rows - 1 - r] = shape[r][c];
            }
        }
        return rotated;
    }

    // Helper to get actual occupied points
    public ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<>();
        for(int r=0; r<shape.length; r++) {
            for(int c=0; c<shape[r].length; c++) {
                if(shape[r][c] != 0) {
                    points.add(new Point(c, r));
                }
            }
        }
        return points;
    }
}
