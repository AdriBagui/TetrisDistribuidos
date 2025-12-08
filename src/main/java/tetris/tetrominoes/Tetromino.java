package tetris.tetrominoes;

import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

// ==========================================
// ABSTRACT CLASS: Tetromino
// Base class for all Tetris shapes
// ==========================================
public abstract class Tetromino {
    public static final int NUMBER_OF_TETROMINOES = TetrominoType.values().length;

    private boolean[][][] shapeRotations;
    protected int rotationIndex;
    private double x, y;
    private int parentX, parentY;
    private Color color;

    /**
     * Creates a tetromino.
     * @param shapeRotations list of matrix which represents all the possible rotations of a tetromino.
     * @param rotationIndex represents the current "rotation state" of a tetromino.
     * @param x coordinates for the x-axis.
     * @param y coordinates for the y-axis.
     * @param color color used for the tetromino.
     * @param parentX position x of its parent container (for example the position of the hold or the next pieces container).
     * @param parentY position y of its parent container (for example the position of the hold or the next pieces container).
     */
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

    /**
     * Gets the type of tetromino.
     * @return value of the type of the piece set in {@link tetris.Config}.
     */
    public abstract TetrominoType getType();
    /**
     * Gets the width of the container of the piece (for example, for I piece we have a 4x4 container while T piece has
     * a 3x3 container).
     * @return height of the hitbox.
     */
    public abstract int getWidth();
    /**
     * Gets the height of the container of the piece (for example, for I piece we have a 4x4 container while T piece has
     * a 3x3 container).
     * @return height of the hitbox.
     */
    public abstract int getHeight();
    /**
     * Gets the width of the hitbox of the piece at the moment.
     * @return width of the hitbox.
     */
    public abstract int getApparentWidth();
    /**
     * Gets the height of the hitbox of the piece at the moment.
     * @return height of the hitbox.
     */
    public abstract int getApparentHeight();
    /**
     * Creates a copy of the tetromino.
     * @return Copy of the tetromino.
     */
    public abstract Tetromino createCopy();

    // GETTERS
    /**
     * Gets the {@code ShapeRotations} which represents the hitbox of the tetromino in each position.
     * @return {@code ShapeRotations} of the tetromino.
     */
    public boolean[][][] getShapeRotations() { return shapeRotations; }
    /**
     * Gets the current {@code ShapeRotations} which represents the hitbox of the tetromino.
     * @return {@code ShapeRotations} of the tetromino with the current {@code rotationIndex}.
     */
    public boolean[][] getShape() { return shapeRotations[rotationIndex]; }
    /**
     * Gets the current {@code rotationIndex} of the tetromino.
     * @return Current {@code rotationIndex}.
     */
    public int getRotationIndex() { return rotationIndex; }
    /**
     * Gets the current {@code x} coordinate of the tetromino's container relative to its parent container grid.
     * @return Current {@code x} of the tetromino's container.
     */
    public int getX() { return (int) x; }
    /**
     * Gets the current {@code y} coordinate of the tetromino's container relative to its parent container grid.
     * @return Current {@code y} of the tetromino's container.
     */
    public int getY() { return (int) y; }
    /**
     * Gets the {@code color} of the tetromino.
     * @return {@code color} of the tetromino.
     */
    public Color getColor() { return color; }
    /**
     * Gets the position x of its parent container (for example the position of the hold or the next pieces container).
     * @return Position {@code parentX} of its parent container.
     */
    public int getParentX() { return parentX; }
    /**
     * Gets the position y of its parent container (for example the position of the hold or the next pieces container).
     * @return Position {@code parentY} of its parent container.
     */
    public int getParentY() { return parentY; }

    // SETTERS
    /**
     * Sets the {@code rotationIndex} of the tetromino.
     * @param rotationIndex new rotation index for the tetromino.
     */
    public void setRotationIndex(int rotationIndex) {
        this.rotationIndex = rotationIndex;
    }
    /**
     * Sets the current {@code x} coordinate of the tetromino's container relative to its parent container grid.
     * @param x new {@code x} coordinate for the tetromino's container.
     */
    public void setX(double x) { this.x = x; }
    /**
     * Sets the current {@code y} coordinate of the tetromino's container relative to its parent container grid.
     * @param y new {@code y} coordinate for the tetromino's container.
     */
    public void setY(double y) { this.y = y; }
    /**
     * Sets the pair ({@code x},{@code y}) of the tetromin's container relative to its parent container grid.
     * @param x new {@code x} coordinate for the tetromino's container.
     * @param y new {@code y} coordinate for the tetromino's container.
     */
    public synchronized void setXY(double x, double y) {
        setX(x);
        setY(y);
    }
    /**
     * Sets the {@code color} of the tetromino.
     * @param color new {@code color} for the tetromino.
     */
    public void setColor(Color color) { this.color = color; }

    /**
     * Sets the position x of its parent container (for example the position of the hold or the next pieces container).
     * @param parentX new position x for the parent container.
     */
    public void setParentX(int parentX) { this.parentX = parentX; }
    /**
     * Sets the position y of its parent container (for example the position of the hold or the next pieces container).
     * @param parentY new position y for the parent container
     */
    public void setParentY(int parentY) { this.parentY = parentY; }
    /**
     * Sets the pair (x,y) of its parent container (for example the position of the hold or the next pieces container).
     * @param parentX new position x for the parent container.
     * @param parentY new position y for the parent container.
     */
    public synchronized void setParentXY(int parentX, int parentY) {
        setParentX(parentX);
        setParentY(parentY);
    }
    /**
     * Sets the pair ({@code x},{@code y}) of the tetromin's container relative to its parent container grid and its {@code rotationIndex}.
     * @param x new {@code x} coordinate for the tetromino's container.
     * @param y new {@code y} coordinate for the tetromino's container.
     * @param rotationIndex new rotation index for the tetromino.
     */
    public synchronized void setXYRotationIndex(double x, double y, int rotationIndex) {
        setXY(x, y);
        setRotationIndex(rotationIndex);
    }

    // MOVEMENT CONTROLLERS

    /**
     * Moves the tetromino 1 block down.
     */
    public void moveDown() { setY(getY() + 1); }
    /**
     * Moves the tetromino to the bottom.
     */
    public void drop(int cells) { setY(getY() + cells); }
    /**
     * Moves the tetromino 1 block up.
     */
    public void moveUp() { setY(getY() - 1); }
    /**
     * Moves the tetromino 1 block left.
     */
    public void moveLeft() { setX(getX() - 1); }
    /**
     * Moves the tetromino 1 block right.
     */
    public void moveRight() { setX(getX() + 1); }
    /**
     * Rotates the tetromino to the right.
     */
    public void rotateRight() { setRotationIndex((getRotationIndex()+1) % 4); }
    /**
     * Rotates the tetromino to the right.
     */
    public void rotateLeft() { setRotationIndex((getRotationIndex()+3) % 4); }
    /**
     * Rotates the tetromino two times (180º rotation).
     */
    public void flip() { setRotationIndex((getRotationIndex()+2) % 4); }

    // DRAW (Synchronized because when updating position and rotation simultaneously index of tetromino incoherent states may appear)
    /**
     * Renders the Tetromino's shape onto its parent container.
     * @param g2 The {@link java.awt.Graphics2D Graphics2D} context used for drawing.
     */
    public synchronized void draw(Graphics2D g2) {
        boolean[][] shape = getShape();

        //For debugging (draws the hitbox borders of the tetromino)
//        g2.setColor(Color.WHITE);
//        g2.drawRect((int) (parentX + (x) * CELL_SIZE), (int) (parentY + (y) * CELL_SIZE), getWidth() * CELL_SIZE, getHeight() * CELL_SIZE);

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) {
                    TetrominoCell.draw(g2, (int) (parentX + (x + c) * CELL_SIZE), (int) (parentY + (y + r) * CELL_SIZE), color);
                }
            }
        }
    }
}
