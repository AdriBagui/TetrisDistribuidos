package tetris.boards.components;

import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoCell;

import static client.userInterface.panels.MainPanel.*;

import java.awt.*;

/**
 * Represents the grid (playfield) of the Tetris board.
 * <p>
 * This class handles the low-level data structure of the game: a 2D array of cells.
 * It provides core functionality for:
 * <ul>
 * <li>Collision detection (walls, floor, other blocks).</li>
 * <li>Locking pieces into the grid.</li>
 * <li>Detecting and clearing full lines.</li>
 * <li>Adding garbage lines for multiplayer.</li>
 * </ul>
 * </p>
 * <p>
 * <b>Coordinate System:</b><br>
 * The grid includes "Spawn Rows" (invisible buffer rows at the top).
 * Row 0 is the very top (invisible), increasing downwards.
 * </p>
 */
public class BoardGrid {
    // --- BOARD CONFIGURATION ---
    public static final int ROWS = 20;
    public static final int SPAWN_ROWS = 3;
    public static final int COLUMNS = 10;

    // --- GRID POSITION IN PANEL ---
    private final int x;
    private final int y;

    // --- DATA REPRESENTATION ---
    // grid[row][col] -> true if occupied
    protected boolean[][] grid;
    // gridColor[row][col] -> Color of the block if occupied
    protected Color[][] gridColor;
    protected int numberOfSpawnRows;

    /**
     * Creates a new empty grid.
     *
     * @param x                 X screen coordinate.
     * @param y                 Y screen coordinate.
     * @param numberOfRows      Visible rows.
     * @param numberOfSpawnRows Hidden spawn rows.
     * @param numberOfColumns   Columns.
     */
    public BoardGrid(int x, int y, int numberOfRows, int numberOfSpawnRows, int numberOfColumns) {
        this.x = x;
        this.y = y;

        // Total rows = visible + spawn
        grid = new boolean[numberOfRows + numberOfSpawnRows][numberOfColumns];
        gridColor = new Color[numberOfRows + numberOfSpawnRows][numberOfColumns];

        this.numberOfSpawnRows = numberOfSpawnRows;
    }

    // --- GETTERS ---

    public int getX() { return x; }
    public int getY() { return y; }
    public int getNumberOfRows() { return grid.length - numberOfSpawnRows; }
    public int getNumberOfSpawnRows() { return numberOfSpawnRows; }
    public int getTotalNumberOfRows() { return getNumberOfRows() + getNumberOfSpawnRows(); }
    public int getNumberOfColumns() { return grid[0].length; }

    public boolean isCellFilled(int row, int column) { return grid[row][column]; }
    public boolean isCellEmpty(int row, int column) { return !isCellFilled(row, column); }
    public Color getCellColor(int row, int column) { return gridColor[row][column]; }

    // --- CORE LOGIC ---

    /**
     * Locks a tetromino into the grid array and immediately checks/clears lines.
     *
     * @param t The tetromino to lock.
     * @return The number of lines cleared by this action (0-4).
     */
    public int lockTetrominoAndClearLines(Tetromino t) {
        boolean[][] tShape = t.getShape();
        Color tColor = t.getColor();
        int tX = t.getX();
        int tY = t.getY();
        int tCellX, tCellY;

        // We use an array to track which rows were affected to optimize the check
        int[] rowsToClear = new int[4];
        int numberOfRowsToClear = 0;
        int numberOfClearedRows;

        // 1. Lock the piece into the boolean/color arrays
        for (int tShapeRow = 0; tShapeRow < tShape.length; tShapeRow++) {
            tCellY = tY + tShapeRow;

            for (int tShapeColumn = 0; tShapeColumn < tShape[tShapeRow].length; tShapeColumn++) {
                tCellX = tX + tShapeColumn;

                if (tShape[tShapeRow][tShapeColumn]) {
                    // Safety check for bounds, though collision should prevent this
                    if (tCellY >= 0 && tCellY < getTotalNumberOfRows() && tCellX >= 0 && tCellX < getNumberOfColumns()) {
                        fillCell(tCellY, tCellX, tColor);
                    }
                }
            }

            // Check if this specific row became full
            if (tCellY >= 0 && tCellY < getTotalNumberOfRows() && isRowFull(tCellY)) {
                // Ensure we don't add duplicate rows if the shape iterates oddly
                // (Simple tetris shapes scan top-down, so strict ordering usually applies)
                rowsToClear[numberOfRowsToClear] = tCellY;
                numberOfRowsToClear++;
            }
        }

        // 2. Clear lines and shift blocks down
        if (numberOfRowsToClear > 0) {
            numberOfClearedRows = 1;

            // Iterate backwards from the lowest cleared row.
            // Any row ABOVE the cleared line needs to shift down by the number of cleared lines below it.
            // The logic here is optimized to process multiple clears in one pass.
            // Note: The loop starts from the lowest cleared row index.
            for (int row = rowsToClear[numberOfRowsToClear-1]; row > numberOfRowsToClear; row--) {
                // Determine how many lines deeply we need to shift based on how many rows were cleared below the current one
                while (numberOfRowsToClear-1-numberOfClearedRows >= 0 &&
                        row - numberOfClearedRows == rowsToClear[numberOfRowsToClear-1-numberOfClearedRows]) {
                    numberOfClearedRows++;
                }

                // Move row down
                if (row - numberOfClearedRows > numberOfClearedRows) {
                    copyRow(row - numberOfClearedRows, row);
                } else {
                    cutRow(row - numberOfClearedRows, row);
                }
            }
        }

        return numberOfRowsToClear;
    }

    /**
     * Pushes existing blocks up and inserts garbage lines at the bottom.
     * Used in multiplayer modes.
     *
     * @param numberOfGarbageRows Count of rows to add.
     * @param emptyGarbageColumn  The index of the hole in the garbage lines.
     * @return {@code true} if blocks were pushed off the top (Game Over), {@code false} otherwise.
     */
    public boolean addGarbage(int numberOfGarbageRows, int emptyGarbageColumn) {
        boolean overflow = false;

        // 1. Check if top rows have blocks (they will be pushed out)
        for (int row = 0; row < numberOfGarbageRows; row++) {
            if (!isRowEmpty(row)) {
                overflow = true;
                break;
            }
        }

        // 2. Shift everything UP
        for (int row = 0; row < getTotalNumberOfRows() - numberOfGarbageRows; row++) {
            cutRow(row + numberOfGarbageRows, row);
        }

        // 3. Fill bottom with garbage
        for (int row = getTotalNumberOfRows() - numberOfGarbageRows; row < getTotalNumberOfRows(); row++) {
            fillRowWithGarbage(row, emptyGarbageColumn);
        }

        return overflow;
    }

    /**
     * Checks if a tetromino occupies any cell that is already filled or out of bounds.
     *
     * @param t The tetromino to test.
     * @return {@code true} if a collision exists.
     */
    public boolean hasCollision(Tetromino t) {
        boolean[][] shape = t.getShape();
        int tCellX;
        int tCellY;

        for (int tShapeRow = 0; tShapeRow < shape.length; tShapeRow++) {
            for (int tShapeColumn = 0; tShapeColumn < shape[tShapeRow].length; tShapeColumn++) {
                if (shape[tShapeRow][tShapeColumn]) {
                    tCellX = t.getX() + tShapeColumn;
                    tCellY = t.getY() + tShapeRow;

                    // 1. Check Boundaries (Walls/Floor)
                    if (tCellX < 0 || tCellX >= getNumberOfColumns() || tCellY < 0 || tCellY >= getTotalNumberOfRows())
                        return true;

                    // 2. Check Existing Blocks
                    if (grid[tCellY][tCellX])
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Calculates the drop distance for the ghost piece.
     *
     * @param t The tetromino to test.
     * @return The number of cells the piece can drop before hitting something.
     */
    public int distanceToFloor(Tetromino t) {
        Tetromino aux = t.createCopy();
        int distanceToFloor = 0;

        aux.moveDown();
        while (!hasCollision(aux)) {
            distanceToFloor++;
            aux.moveDown();
        }

        return distanceToFloor;
    }

    /**
     * Renders the grid background, lines, and blocks.
     *
     * @param g2 The graphics context.
     */
    public void draw(Graphics2D g2) {
        // Draw Background
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y + BOARD_SPAWN_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT);

        // Draw Grid Lines (Transparent Gray)
        g2.setColor(new Color(110, 110 , 110, 40));
        for (int y = CELL_SIZE; y < BOARD_HEIGHT; y += CELL_SIZE) {
            g2.drawLine(x, this.y + BOARD_SPAWN_HEIGHT + y, x + BOARD_WIDTH, this.y + BOARD_SPAWN_HEIGHT + y);
        }
        for (int x = CELL_SIZE; x < BOARD_WIDTH; x += CELL_SIZE) {
            g2.drawLine(this.x + x, y + BOARD_SPAWN_HEIGHT, this.x + x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        }

        // Draw Cells
        for (int row = 0; row < getTotalNumberOfRows(); row++) {
            for (int column = 0; column < getNumberOfColumns(); column++) {
                // Only draw visible rows (skip spawn area usually) or draw everything if requested.
                // Here we draw starting from y, which includes spawn rows but the viewport likely clips them
                // or the drawing logic for TetrominoCell needs absolute coordinates.
                // NOTE: 'y' passed in constructor is usually top of the panel.
                if (isCellFilled(row, column))
                    TetrominoCell.draw(g2, x + column * CELL_SIZE, y + row * CELL_SIZE, getCellColor(row, column));
            }
        }

        // Draw Outer Border
        g2.setColor(new Color(110, 110 , 110));
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT, x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
    }

    // --- PRIVATE UTILS ---

    private void fillCell(int row, int column, Color color) {
        grid[row][column] = true;
        gridColor[row][column] = color;
    }

    private void emptyCell(int row, int column) {
        grid[row][column] = false;
        gridColor[row][column] = null;
    }

    private boolean isRowFull(int row) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            if (isCellEmpty(row, column)) return false;
        }
        return true;
    }

    private boolean isRowEmpty(int row) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            if (isCellFilled(row, column)) return false;
        }
        return true;
    }

    private void copyRow(int srcRow, int destRow) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            if (isCellFilled(srcRow, column)) {
                fillCell(destRow, column, getCellColor(srcRow, column));
            } else {
                emptyCell(destRow, column);
            }
        }
    }

    private void cutRow(int srcRow, int destRow) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            if (isCellFilled(srcRow, column)) {
                fillCell(destRow, column, getCellColor(srcRow, column));
                emptyCell(srcRow, column);
            } else {
                emptyCell(destRow, column);
            }
        }
    }

    private void fillRowWithGarbage(int row, int emptyColumn) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            if(column == emptyColumn) {
                emptyCell(row, column);
            } else {
                fillCell(row, column, Color.GRAY);
            }
        }
    }
}