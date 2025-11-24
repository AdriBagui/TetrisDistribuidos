package tetris.tetrominoes;

import tetris.GamePanel;

import java.awt.*;

// ==========================================
// ABSTRACT CLASS: Tetromino
// Base class for all Tetris shapes
// ==========================================
public abstract class Tetromino {
    private boolean[][][] shapeRotations;
    protected int rotationIndex;
    private double x, y;
    private int parentX, parentY;
    private Color color;


    public Tetromino(boolean[][][] shapeRotations, int rotationIndex, double x, double y, Color color, int boardX, int boardY) {
        this.shapeRotations = shapeRotations;
        this.rotationIndex = rotationIndex;
        setXY(x, y);
        this.color = color;
        this.parentX = boardX;
        this.parentY = boardY;
    }


    public abstract int getType();
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract int getApparentWidth();
    public abstract int getApparentHeight();

    public boolean[][][] getShapeRotations() { return shapeRotations; }
    public boolean[][] getShape() { return shapeRotations[rotationIndex]; }
    public int getRotationIndex() { return rotationIndex; }
    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public Color getColor() { return color; }
    public int getParentX() { return parentX; }
    public int getParentY() { return parentY; }

    public void setRotationIndex(int rotationIndex) { this.rotationIndex = rotationIndex; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setXY(double x, double y) { this.x = x; this.y = y; }
    public void setColor(Color color) { this.color = color; }
    public void setParentX(int parentX) { this.parentX = parentX; }
    public void setParentY(int parentY) { this.parentY = parentY; }
    public void setParentXY(int parentX, int parentY) { this.parentX = parentX; this.parentY = parentY; }

    public void moveDown() { y++; }
    public void moveUp() { y--; }
    public void moveLeft() { x--; }
    public void moveRight() { x++; }

    public void rotateRight() {
        rotationIndex++;

        if (rotationIndex > 3) { rotationIndex = 0; }
    }
    public void rotateLeft()
    {
        rotationIndex--;

        if (rotationIndex < 0) { rotationIndex = 3; }
    }
    public void flip()
    {
        rotationIndex += + 2;

        if (rotationIndex > 3) { rotationIndex = rotationIndex - 4; }
    }

    public void draw(Graphics2D g2) {
        boolean[][] shape = getShape();

        for(int r = 0; r< shape.length; r++) {
            for(int c = 0; c< shape[r].length; c++) {
                if(shape[r][c]) {
                    TetrominoCell.draw(g2, (int) (parentX + (x + c) * GamePanel.CELL_SIZE), (int) (parentY + (y + r) * GamePanel.CELL_SIZE), color);
                }
            }
        }
    }
}
