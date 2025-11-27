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
    TetrominoShadow shadow;
    private boolean shadowVisible;


    public Tetromino(boolean[][][] shapeRotations, int rotationIndex, double x, double y, Color color, int parentX, int parentY, boolean hasShadow) {
        this.shapeRotations = shapeRotations;
        this.rotationIndex = rotationIndex;
        this.x = x;
        this.y = y;
        this.color = color;
        this.parentX = parentX;
        this.parentY = parentY;
        this.shadowVisible = false;

        if (hasShadow) shadow = new TetrominoShadow(this);
    }
    public Tetromino(boolean[][][] shapeRotations, int rotationIndex, double x, double y, Color color, int parentX, int parentY) {
        this(shapeRotations, rotationIndex, x, y, color, parentX, parentY, false);
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

        if (shadow != null) shadow.setRotationIndex(rotationIndex);
    }
    public void setX(double x) {
        this.x = x;
        if (shadow != null) shadow.setX(x);
    }
    public void setY(double y) { this.y = y; }
    public void setXY(double x, double y) {
        setX(x);
        setY(y);
    }
    public void setColor(Color color) {
        this.color = color;
        if (shadow != null) shadow.setColor(color);
    }
    public void setParentX(int parentX) {
        this.parentX = parentX;
        if (shadow != null) shadow.setParentX(parentX);
    }
    public void setParentY(int parentY) {
        this.parentY = parentY;
        if (shadow != null) shadow.setParentY(parentY);
    }
    public void setParentXY(int parentX, int parentY) {
        setParentX(parentX);
        setParentY(parentY);
    }

    // SHADOW CONTROLLERS
    public void showShadow() { shadowVisible = true; }
    public void hideShadow() { shadowVisible = false; }

    // MOVEMENT CONTROLLERS
    public void moveDown() { y++; }
    public void moveUp() { y--; }
    public void moveLeft() {
        x--;
        if (shadow != null) shadow.moveLeft();
    }
    public void moveRight() {
        x++;
        if (shadow != null) shadow.moveRight();
    }
    public void rotateRight() {
        rotationIndex++;

        if (rotationIndex > 3) { rotationIndex = 0; }

        if (shadow != null) shadow.rotateRight();
    }
    public void rotateLeft() {
        rotationIndex--;

        if (rotationIndex < 0) { rotationIndex = 3; }

        if (shadow != null) shadow.rotateLeft();
    }
    public void flip() {
        rotationIndex += + 2;

        if (rotationIndex > 3) { rotationIndex = rotationIndex - 4; }

        if (shadow != null) shadow.flip();
    }

    // DRAW
    public void draw(Graphics2D g2) {
        boolean[][] shape = getShape();

        if (shadowVisible && shadow != null) {
            shadow.draw(g2);
        }

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) {
                    TetrominoCell.draw(g2, (int) (parentX + (x + c) * GamePanel.CELL_SIZE), (int) (parentY + (y + r) * GamePanel.CELL_SIZE), color);
                }
            }
        }
    }
}
