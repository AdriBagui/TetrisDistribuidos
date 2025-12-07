package tetris.boards.components;

import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoCell;

import static main.MainPanel.*;

import java.awt.*;

public class BoardGrid {
    // BOARD CONFIGURATION
    public static final int ROWS = 20;
    public static final int SPAWN_ROWS = 3;
    public static final int COLUMNS = 10;

    // GRID POSITION IN PANEL
    private int x, y;
    // GRID REPRESENTATION (row 0 is the top one and column 0 is the left one)
    protected boolean[][] grid;
    protected Color[][] gridColor;
    protected int numberOfSpawnRows;

    /**
     * Creates the grid where the game takes place.
     * @param x x coordinate for the top left corner of the grid.
     * @param y y coordinate for the top left corner of the grid.
     * @param numberOfRows number of visible rows in the grid.
     * @param numberOfSpawnRows number of invisible rows above the grid used for spawning tetrominoes.
     * @param numberOfColumns number of columns in the grid.
     */
    public BoardGrid(int x, int y, int numberOfRows, int numberOfSpawnRows, int numberOfColumns) {
        this.x = x;
        this.y = y;

        grid = new boolean[numberOfRows + numberOfSpawnRows][numberOfColumns];
        gridColor = new Color[numberOfRows + numberOfSpawnRows][numberOfColumns];

        this.numberOfSpawnRows = numberOfSpawnRows;
    }

    // GETTERS

    /**
     * Gets the x coordinate of the grid.
     * @return x coordinate.
     */
    public int getX() { return x; }

    /**
     * Gets the y coordinate of the grid.
     * @return y coordinate.
     */
    public int getY() { return y; }

    /**
     * Gets the number of visible rows (excluding spawn rows).
     * @return number of visible rows.
     */
    public int getNumberOfRows() { return grid.length - numberOfSpawnRows; }

    /**
     * Gets the number of spawn rows (invisible rows at the top).
     * @return number of spawn rows.
     */
    public int getNumberOfSpawnRows() { return numberOfSpawnRows; }

    /**
     * Gets the total number of rows (visible + spawn).
     * @return total number of rows.
     */
    public int getTotalNumberOfRows() { return grid.length; }

    /**
     * Gets the number of columns in the grid.
     * @return number of columns.
     */
    public int getNumberOfColumns() { return grid[0].length; }

    /**
     * Checks if a specific cell is filled.
     * @param row row index of the cell.
     * @param column column index of the cell.
     * @return true if the cell is filled, false otherwise.
     */
    public boolean isCellFilled(int row, int column) { return grid[row][column]; }

    /**
     * Checks if a specific cell is empty.
     * @param row row index of the cell.
     * @param column column index of the cell.
     * @return true if the cell is empty, false otherwise.
     */
    public boolean isCellEmpty(int row, int column) { return !isCellFilled(row, column); }

    /**
     * Gets the color of a specific cell.
     * @param row row index of the cell.
     * @param column column index of the cell.
     * @return color of the cell.
     */
    public Color getCellColor(int row, int column) { return gridColor[row][column]; }

    // LOGIC METHODS

    /**
     * Locks the falling tetromino into the grid and clears any completed lines.
     * @param t the tetromino to lock.
     * @return number of lines cleared.
     */
    public int lockTetrominoAndClearLines(Tetromino t) {
        boolean[][] tShape = t.getShape();
        Color tColor = t.getColor();
        int tX = t.getX();
        int tY = t.getY();
        int tCellX, tCellY;
        int[] rowsToClear = new int[4]; // The maximum number of lines a tetromino can clear is 4
        int numberOfRowsToClear = 0;
        int numberOfClearedRows;

        // Iterate through all cells of the tetromino shape
        for (int tShapeRow = 0; tShapeRow < tShape.length; tShapeRow++) {
            tCellY = tY + tShapeRow;

            for (int tShapeColumn = 0; tShapeColumn < tShape[tShapeRow].length; tShapeColumn++) {
                tCellX = tX + tShapeColumn;

                // If the shape cell is filled, add it to the board
                if (tShape[tShapeRow][tShapeColumn])
                    fillCell(tCellY, tCellX, tColor);
            }

            // If the row is filled, mark it for clearing
            if (tCellY >= 0 && tCellY < getTotalNumberOfRows() && isRowFull(tCellY)) {
                rowsToClear[numberOfRowsToClear] = tCellY;
                numberOfRowsToClear++;
            }
        }

        // If there are rows to clear, clear them
        if (numberOfRowsToClear > 0) {
            numberOfClearedRows = 1; // Variable to count how many rows have been cleared so far

            // This loop iterates backwards from the lowest row to clear.
            // It shifts rows down to fill the cleared spaces.
            for (int row = rowsToClear[numberOfRowsToClear-1]; row > numberOfRowsToClear; row--) {
                while (numberOfRowsToClear-1-numberOfClearedRows >= 0 &&
                        row - numberOfClearedRows == rowsToClear[numberOfRowsToClear-1-numberOfClearedRows]) {
                    numberOfClearedRows++;
                }

                // Optimization: use copy instead of cut when possible
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
     * Adds garbage lines to the bottom of the board.
     * @param numberOfGarbageRows number of garbage rows to add.
     * @param emptyGarbageColumn the column index that should remain empty in the garbage lines.
     * @return true if the garbage addition causes a game over (overflow), false otherwise.
     */
    public boolean addGarbage(int numberOfGarbageRows, int emptyGarbageColumn) {
        boolean overflow = false;

        // Check for overflow (if existing blocks are pushed out of bounds)
        for (int row = 0; row < numberOfGarbageRows; row++) {
            if (!isRowEmpty(row)) {
                overflow = true;
                break;
            }
        }

        // Move all cells up to make space for garbage
        for (int row = 0; row < getTotalNumberOfRows() - numberOfGarbageRows; row++) {
            cutRow(row+numberOfGarbageRows, row);
        }

        // Fill the bottom lines with garbage
        for (int row = getTotalNumberOfRows() - numberOfGarbageRows; row < getTotalNumberOfRows(); row++) {
            fillRowWithGarbage(row, emptyGarbageColumn);
        }

        return overflow;
    }

    /**
     * Checks if a tetromino collides with the grid boundaries or existing blocks.
     * @param t the tetromino to check.
     * @return true if there is a collision, false otherwise.
     */
    public boolean hasCollision(Tetromino t) {
        boolean[][] shape = t.getShape();
        int tCellX;
        int tCellY;

        // Iterate through all cells of the tetromino shape
        for (int tShapeRow = 0; tShapeRow < shape.length; tShapeRow++) {
            for (int tShapeColumn = 0; tShapeColumn < shape[tShapeRow].length; tShapeColumn++) {
                // If the shape cell is filled, check for collision
                if (shape[tShapeRow][tShapeColumn]) {
                    tCellX = t.getX() + tShapeColumn;
                    tCellY = t.getY() + tShapeRow;

                    // Check bounds
                    if (tCellX < 0 || tCellX >= getNumberOfColumns() || tCellY < 0 || tCellY >= getTotalNumberOfRows())
                        return true;

                    // Check for collision with existing blocks
                    if (grid[tCellY][tCellX])
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Calculates the distance from the tetromino to the nearest obstacle below it (floor or blocks).
     * @param t the tetromino to check.
     * @return the distance in cells.
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
     * Draws the grid, its contents, and the border.
     * @param g2 the Graphics2D context.
     */
    public void draw(Graphics2D g2) {
        // Paint background
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y + BOARD_SPAWN_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT);

        // Paint grid lines
        g2.setColor(new Color(110, 110 , 110, 40));
        for (int y = CELL_SIZE; y < BOARD_HEIGHT; y += CELL_SIZE) {
            g2.drawLine(x, this.y + BOARD_SPAWN_HEIGHT + y, x + BOARD_WIDTH, this.y + BOARD_SPAWN_HEIGHT + y);
        }
        for (int x = CELL_SIZE; x < BOARD_WIDTH; x += CELL_SIZE) {
            g2.drawLine(this.x + x, y + BOARD_SPAWN_HEIGHT, this.x + x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        }

        // Draw filled cells
        for (int row = 0; row < getTotalNumberOfRows(); row++) {
            for (int column = 0; column < getNumberOfColumns(); column++) {
                if (isCellFilled(row, column))
                    TetrominoCell.draw(g2, x + column * CELL_SIZE, y + row * CELL_SIZE, getCellColor(row, column));
            }
        }

        // Draw border
        g2.setColor(new Color(110, 110 , 110));
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT, x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
    }

    // AUXILIARY CELL OPERATIONS

    /**
     * Fills a specific cell with a color.
     * @param row row index.
     * @param column column index.
     * @param color color to fill.
     */
    private void fillCell(int row, int column, Color color) {
        grid[row][column] = true;
        gridColor[row][column] = color;
    }

    /**
     * Empties a specific cell.
     * @param row row index.
     * @param column column index.
     */
    private void emptyCell(int row, int column) {
        grid[row][column] = false;
        gridColor[row][column] = null;
    }

    // AUXILIARY ROW OPERATIONS

    /**
     * Checks if a row is completely full.
     * @param row row index.
     * @return true if full, false otherwise.
     */
    private boolean isRowFull(int row) {
        boolean full = true;

        for (int column = 0; column < getNumberOfColumns(); column++) {
            if (isCellEmpty(row, column)) {
                full = false;
                break;
            }
        }

        return full;
    }

    /**
     * Checks if a row is completely empty.
     * @param row row index.
     * @return true if empty, false otherwise.
     */
    private boolean isRowEmpty(int row) {
        boolean empty = true;

        for (int column = 0; column < getNumberOfColumns(); column++) {
            if (isCellFilled(row, column)) {
                empty = false;
                break;
            }
        }

        return empty;
    }

    /**
     * Copies the content of one row to another.
     * @param srcRow source row index.
     * @param destRow destination row index.
     */
    private void copyRow(int srcRow, int destRow) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            if (isCellFilled(srcRow, column)) {
                fillCell(destRow, column, getCellColor(srcRow, column));
            }
            else {
                emptyCell(destRow, column);
            }
        }
    }

    /**
     * Moves (cuts) the content of one row to another, emptying the source row.
     * @param srcRow source row index.
     * @param destRow destination row index.
     */
    private void cutRow(int srcRow, int destRow) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            if (isCellFilled(srcRow, column)) {
                fillCell(destRow, column, getCellColor(srcRow, column));
                emptyCell(srcRow, column);
            }
            else {
                emptyCell(destRow, column);
            }
        }
    }

    /**
     * Empties an entire row.
     * @param row row index.
     */
    private void emptyRow(int row) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            emptyCell(row, column);
        }
    }

    /**
     * Fills a row with garbage blocks, leaving one column empty.
     * @param row row index.
     * @param emptyColumn column index to leave empty.
     */
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