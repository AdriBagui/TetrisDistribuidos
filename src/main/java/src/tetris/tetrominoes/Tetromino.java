package src.tetris.tetrominoes;

import java.awt.*;

import static src.tetris.Config.*;

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


    public Tetromino(boolean[][][] shapeRotations, int rotationIndex, double x, double y, Color color, int parentX, int parentY) {
        this.shapeRotations = shapeRotations;
        this.rotationIndex = rotationIndex;
        this.x = x;
        this.y = y;
        this.color = color;
        this.parentX = parentX;
        this.parentY = parentY;
    }


    // ABSTRACT METHODS
    public abstract int getType();
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract int getApparentWidth();
    public abstract int getApparentHeight();
    public abstract Tetromino createCopy();

    // GETTERS
    public boolean[][][] getShapeRotations() { return shapeRotations; }
    public boolean[][] getShape() { return shapeRotations[rotationIndex]; }
    public int getRotationIndex() { return rotationIndex; }
    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public Color getColor() { return color; }
    public int getParentX() { return parentX; }
    public int getParentY() { return parentY; }

    // SETTERS
    public void setRotationIndex(int rotationIndex) {
        this.rotationIndex = rotationIndex;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) { this.y = y; }
    public synchronized void setXY(double x, double y) {
        setX(x);
        setY(y);
    }
    public void setColor(Color color) { this.color = color; }
    public void setParentX(int parentX) { this.parentX = parentX; }
    public void setParentY(int parentY) { this.parentY = parentY; }
    public synchronized void setParentXY(int parentX, int parentY) {
        setParentX(parentX);
        setParentY(parentY);
    }
    public synchronized void setXYRotationIndex(double x, double y, int rotationIndex) {
        setXY(x, y);
        setRotationIndex(rotationIndex);
    }

    // MOVEMENT CONTROLLERS
    public void moveDown() { setY(getY() + 1); }
    public void drop(int cells) { setY(getY() + cells); }
    public void moveUp() { setY(getY() - 1); }
    public void moveLeft() { setX(getX() - 1); }
    public void moveRight() { setX(getX() + 1); }
    public void rotateRight() {
        int aux = getRotationIndex() + 1;

        if (aux > 3) { aux = 0; }

        setRotationIndex(aux);
    }
    public void rotateLeft() {
        int aux = getRotationIndex() - 1;

        if (aux < 0) { aux = 3; }

        setRotationIndex(aux);
    }
    public void flip() {
        int aux = getRotationIndex() + 2;

        if (aux > 3) { aux = aux - 4; }

        setRotationIndex(aux);
    }

    // DRAW (Synchronized because when updating position and rotation simultaneously index of tetromino incoherent states may appear)
    public synchronized void draw(Graphics2D g2) {
        boolean[][] shape = getShape();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) {
                    TetrominoCell.draw(g2, (int) (parentX + (x + c) * CELL_SIZE), (int) (parentY + (y + r) * CELL_SIZE), color);
                }
            }
        }
    }
}
