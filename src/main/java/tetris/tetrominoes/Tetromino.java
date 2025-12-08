package tetris.tetrominoes;

import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

/**
 * Abstract base class representing a Tetris piece (Tetromino).
 * <p>
 * This class manages the state common to all pieces:
 * <ul>
 * <li><b>Position:</b> Logic coordinates (grid row/col) and display offsets.</li>
 * <li><b>Rotation:</b> Current orientation index (0-3).</li>
 * <li><b>Shape:</b> A 3D array defining the boolean grid for every possible rotation.</li>
 * <li><b>Appearance:</b> Color and rendering logic.</li>
 * </ul>
 * </p>
 */
public abstract class Tetromino {
    /** Total number of unique tetromino shapes (7). */
    public static final int NUMBER_OF_TETROMINOES = TetrominoType.values().length;

    /** * The definitions of the shape for every rotation.
     * Dimensions: [Rotation Index][Row][Column]
     */
    private boolean[][][] shapeRotations;

    /** Current rotation state (0 = 0 deg, 1 = 90 deg, 2 = 180 deg, 3 = 270 deg). */
    protected int rotationIndex;

    /** Logical X coordinate (Column) on the board grid. */
    private double x;

    /** Logical Y coordinate (Row) on the board grid. */
    private double y;

    /** Pixel X offset of the container (Board or Queue) holding this piece. */
    private int parentX;

    /** Pixel Y offset of the container (Board or Queue) holding this piece. */
    private int parentY;

    /** The base color of the tetromino. */
    private Color color;

    /**
     * Constructs a new Tetromino.
     *
     * @param shapeRotations 3D array defining the occupied cells for each of the 4 rotations.
     * @param rotationIndex  Initial rotation state.
     * @param x              Initial logical X coordinate.
     * @param y              Initial logical Y coordinate.
     * @param color          The color of the blocks.
     * @param parentX        Screen X offset of the parent container.
     * @param parentY        Screen Y offset of the parent container.
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


    // --- ABSTRACT METHODS ---

    /**
     * Gets the enum type of this tetromino.
     * @return The specific {@link TetrominoType}.
     */
    public abstract TetrominoType getType();

    /**
     * Gets the full width of the bounding box matrix (e.g., 4 for 'I', 3 for 'T').
     * @return The matrix width.
     */
    public abstract int getWidth();

    /**
     * Gets the full height of the bounding box matrix.
     * @return The matrix height.
     */
    public abstract int getHeight();

    /**
     * Gets the visual width of the piece in its current rotation.
     * <p>
     * Unlike {@link #getWidth()}, which returns the matrix size, this returns the
     * actual width of the filled blocks. Used for centering in the Next/Hold queues.
     * </p>
     * @return The apparent width in cells.
     */
    public abstract int getApparentWidth();

    /**
     * Gets the visual height of the piece in its current rotation.
     * @return The apparent height in cells.
     */
    public abstract int getApparentHeight();

    /**
     * Creates a deep copy of this tetromino.
     * Essential for physics simulations (ghost pieces, collision testing).
     * @return A new instance with the same state.
     */
    public abstract Tetromino createCopy();

    // --- GETTERS ---

    /**
     * Gets the 3D array defining all rotation states.
     * @return The shape definitions.
     */
    public boolean[][][] getShapeRotations() { return shapeRotations; }

    /**
     * Gets the 2D grid representing the piece's <i>current</i> rotation.
     * @return A boolean matrix where {@code true} represents a block.
     */
    public boolean[][] getShape() { return shapeRotations[rotationIndex]; }

    /**
     * Gets the current rotation index (0-3).
     * @return The index.
     */
    public int getRotationIndex() { return rotationIndex; }

    /**
     * Gets the logical X coordinate (grid column).
     * @return The X coordinate.
     */
    public int getX() { return (int) x; }

    /**
     * Gets the logical Y coordinate (grid row).
     * @return The Y coordinate.
     */
    public int getY() { return (int) y; }

    /**
     * Gets the render color.
     * @return The color.
     */
    public Color getColor() { return color; }

    /**
     * Gets the screen X offset of the parent container.
     * @return Screen pixels X.
     */
    public int getParentX() { return parentX; }

    /**
     * Gets the screen Y offset of the parent container.
     * @return Screen pixels Y.
     */
    public int getParentY() { return parentY; }

    // --- SETTERS ---

    /**
     * Sets the rotation index.
     * @param rotationIndex New index (must be 0-3).
     */
    public void setRotationIndex(int rotationIndex) {
        this.rotationIndex = rotationIndex;
    }

    /**
     * Sets the logical X coordinate.
     * @param x New X coordinate.
     */
    public void setX(double x) { this.x = x; }

    /**
     * Sets the logical Y coordinate.
     * @param y New Y coordinate.
     */
    public void setY(double y) { this.y = y; }

    /**
     * Atomic setter for position.
     * @param x New X coordinate.
     * @param y New Y coordinate.
     */
    public synchronized void setXY(double x, double y) {
        setX(x);
        setY(y);
    }

    /**
     * Sets the render color.
     * @param color New color.
     */
    public void setColor(Color color) { this.color = color; }

    public void setParentX(int parentX) { this.parentX = parentX; }
    public void setParentY(int parentY) { this.parentY = parentY; }

    /**
     * Atomic setter for parent container offsets.
     * @param parentX New screen X offset.
     * @param parentY New screen Y offset.
     */
    public synchronized void setParentXY(int parentX, int parentY) {
        setParentX(parentX);
        setParentY(parentY);
    }

    /**
     * Atomic setter for full spatial state.
     * @param x New logical X.
     * @param y New logical Y.
     * @param rotationIndex New rotation index.
     */
    public synchronized void setXYRotationIndex(double x, double y, int rotationIndex) {
        setXY(x, y);
        setRotationIndex(rotationIndex);
    }

    // --- MOVEMENT HELPERS ---

    /** Moves the piece down by 1 unit. */
    public void moveDown() { setY(getY() + 1); }

    /** * Moves the piece down by a specific amount.
     * @param cells Number of cells to drop.
     */
    public void drop(int cells) { setY(getY() + cells); }

    /** Moves the piece up by 1 unit. */
    public void moveUp() { setY(getY() - 1); }

    /** Moves the piece left by 1 unit. */
    public void moveLeft() { setX(getX() - 1); }

    /** Moves the piece right by 1 unit. */
    public void moveRight() { setX(getX() + 1); }

    /** Cycles rotation state clockwise. */
    public void rotateRight() { setRotationIndex((getRotationIndex()+1) % 4); }

    /** Cycles rotation state counter-clockwise. */
    public void rotateLeft() { setRotationIndex((getRotationIndex()+3) % 4); }

    /** Cycles rotation state by 180 degrees. */
    public void flip() { setRotationIndex((getRotationIndex()+2) % 4); }

    // --- RENDERING ---

    /**
     * Renders the Tetromino to the graphics context.
     * <p>
     * <b>Thread Safety:</b> This method is synchronized to ensure that the position and rotation
     * index do not change mid-render, which could result in tearing or drawing the piece in an invalid state.
     * </p>
     *
     * @param g2 The {@link Graphics2D} context.
     */
    public synchronized void draw(Graphics2D g2) {
        boolean[][] shape = getShape();

        // Debug: Draw bounding box
        // g2.setColor(Color.WHITE);
        // g2.drawRect((int) (parentX + (x) * CELL_SIZE), (int) (parentY + (y) * CELL_SIZE), getWidth() * CELL_SIZE, getHeight() * CELL_SIZE);

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c]) {
                    // Calculate screen position: Parent Offset + (Logical Coord + Block Offset) * Cell Size
                    int screenX = (int) (parentX + (x + c) * CELL_SIZE);
                    int screenY = (int) (parentY + (y + r) * CELL_SIZE);

                    TetrominoCell.draw(g2, screenX, screenY, color);
                }
            }
        }
    }
}