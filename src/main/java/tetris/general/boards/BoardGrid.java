package tetris.general.boards;

import tetris.general.tetrominoes.Tetromino;
import tetris.general.tetrominoes.TetrominoCell;

import java.awt.*;

import static tetris.Config.*;
import static tetris.Config.BOARD_HEIGHT;
import static tetris.Config.BOARD_SPAWN_HEIGHT;
import static tetris.Config.BOARD_WIDTH;
import static tetris.Config.CELL_SIZE;

public class BoardGrid {
    // GRID POSITION IN PANEL
    private int x, y;
    // GRID REPRESENTATION (la fila 0 es la de arriba y la columna 0 es la de la izquierda)
    protected boolean[][] grid;
    protected Color[][] gridColor;
    protected int numberOfSpawnRows;

    public BoardGrid(int x, int y, int numberOfRows, int numberOfSpawnRows, int numberOfColumns) {
        this.x = x;
        this.y = y;

        grid = new boolean[numberOfRows + numberOfSpawnRows][numberOfColumns];
        gridColor = new Color[numberOfRows + numberOfSpawnRows][numberOfColumns];

        this.numberOfSpawnRows = numberOfSpawnRows;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getNumberOfRows() { return grid.length - numberOfSpawnRows; }
    public int getNumberOfSpawnRows() { return numberOfSpawnRows; }
    public int getTotalNumberOfRows() { return grid.length; }
    public int getNumberOfColumns() { return grid[0].length; }
    public boolean isCellFilled(int row, int column) { return grid[row][column]; }
    public boolean isCellEmpty(int row, int column) { return !isCellFilled(row, column); }
    public Color getCellColor(int row, int column) { return gridColor[row][column]; }

    public int lockTetrominoAndClearLines(Tetromino t) {
        boolean[][] tShape = t.getShape();
        Color tColor = t.getColor();
        int tX = t.getX();
        int tY = t.getY();
        int tCellX, tCellY;
        int[] rowsToClear = new int[4]; // El máximo número de líneas que puede limpiar un tetromino son 4
        int numberOfRowsToClear = 0;
        int numberOfClearedRows;

        // Itera por todas las celdas de la shape un tetromino
        for (int tShapeRow = 0; tShapeRow < tShape.length; tShapeRow++) {
            tCellY = tY + tShapeRow;

            for (int tShapeColumn = 0; tShapeColumn < tShape[tShapeRow].length; tShapeColumn++) {
                tCellX = tX + tShapeColumn;

                // Si la celda de la shape está llena la añade al tablero
                if (tShape[tShapeRow][tShapeColumn])
                    fillCell(tCellY, tCellX, tColor);
            }

            // Si se ha llenado la fila se anota que se va a borrar y cual es la fila para luego borrarla
            if (tCellY >= 0 && tCellY < getTotalNumberOfRows() && isRowFull(tCellY)) {
                rowsToClear[numberOfRowsToClear] = tCellY;
                numberOfRowsToClear++;
            }
        }

        // Si hay filas para limpiar las limpia
        if (numberOfRowsToClear > 0) {
            numberOfClearedRows = 1; // Variable para contar cuantas filas se han borrado

            // Este bucle es un poco raro. En resumen limpia las filas que se han llenado.
            // Intento explicar lo que hace: empieza de la fila más baja, si la de encima no está marcada para eliminar
            // la copia. Si está marcada para eliminar lo anota como una fila más limpiada y mira si la siguiente
            // fila de encima está marcada para eliminar, volviendo así al comienzo de la explicación.
            for (int row = rowsToClear[numberOfRowsToClear-1]; row > numberOfRowsToClear; row--) {
                while (numberOfRowsToClear-1-numberOfClearedRows >= 0 &&
                        row - numberOfClearedRows == rowsToClear[numberOfRowsToClear-1-numberOfClearedRows]) {
                    numberOfClearedRows++;
                }

                // Solo por eficiencia, se podría hacer cut en todas (no sé puede hacer copy en todas porque entonces
                // las numberOfClearedRows primeras filas no se limpian, se copian abajo pero siguen llenas)
                if (row - numberOfClearedRows > numberOfClearedRows) {
                    copyRow(row - numberOfClearedRows, row);
                } else {
                    cutRow(row - numberOfClearedRows, row);
                }

            }
        }

        return numberOfRowsToClear;
    }
    // Añade tantas líneas de basura (abajo del tablero) como se pide y deja la columna indicada vacía
    // Si al añadir las líneas de basura alguna de las líneas del tablero se sale por arriba devuelve true.
    // En caso contrario devuelve falso.
    public boolean addGarbage(int numberOfGarbageRows, int emptyGarbageColumn) {
        boolean overflow = false;

        // Comprueba si hay overflow
        for (int row = 0; row < numberOfGarbageRows; row++) {
            if (!isRowEmpty(row)) {
                overflow = true;
                break;
            }
        }

        // Mueve todas las celdas el número de líneas necesario para hacer hueco para la basura
        for (int row = 0; row < getTotalNumberOfRows() - numberOfGarbageRows; row++) {
            cutRow(row+numberOfGarbageRows, row);
        }

        // getTotalNumberOfRows() - 1 es la última línea indexable (van de la 0 a la getTotalNumberOfRows() - 1)
        // Rellena las lineas de basura
        for (int row = getTotalNumberOfRows() - numberOfGarbageRows; row < getTotalNumberOfRows(); row++) {
            fillRowWithGarbage(row, emptyGarbageColumn);
        }

        return overflow;
    }

    public boolean hasCollision(Tetromino t) {
        boolean[][] shape = t.getShape();
        int tCellX;
        int tCellY;

        // Itera por todas las celdas de la shape un tetromino
        for (int tShapeRow = 0; tShapeRow < shape.length; tShapeRow++) {
            for (int tShapeColumn = 0; tShapeColumn < shape[tShapeRow].length; tShapeColumn++) {
                // Si la celda de la shape está llena comprueba si está dentro del tablero y no colisiona con
                // celdas rellenas del tablero
                if (shape[tShapeRow][tShapeColumn]) {
                    tCellX = t.getX() + tShapeColumn;
                    tCellY = t.getY() + tShapeRow;

                    // Comprueba si se sale del tablero
                    if (tCellX < 0 || tCellX >= BOARD_COLUMNS || tCellY < 0 || tCellY >= BOARD_ROWS + BOARD_SPAWN_ROWS)
                        return true;

                    // Comprueba si hay colisión con celdas rellenas del tablero
                    if (grid[tCellY][tCellX])
                        return true;
                }
            }
        }
        return false;
    }
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

    public void draw(Graphics2D g2) {
        // Pinta el fondo
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y + BOARD_SPAWN_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT);

        // Pinta las líneas de la plantilla
        g2.setColor(new Color(255, 255, 255, 64));
        for (int y = CELL_SIZE; y < BOARD_HEIGHT; y += CELL_SIZE) {
            g2.drawLine(x, this.y + BOARD_SPAWN_HEIGHT + y, x + BOARD_WIDTH, this.y + BOARD_SPAWN_HEIGHT + y);
        }
        for (int x = CELL_SIZE; x < BOARD_WIDTH; x += CELL_SIZE) {
            g2.drawLine(this.x + x, y + BOARD_SPAWN_HEIGHT, this.x + x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        }

        // Dibuja las celdas llenas
        for (int row = 0; row < getTotalNumberOfRows(); row++) {
            for (int column = 0; column < getNumberOfColumns(); column++) {
                if (isCellFilled(row, column))
                    TetrominoCell.draw(g2, x + column * CELL_SIZE, y + row * CELL_SIZE, getCellColor(row, column));
            }
        }

        // Dibuja el borde
        g2.setColor(Color.WHITE);
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT, x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
    }

    // Auxiliary cell operations
    private void fillCell(int row, int column, Color color) {
        grid[row][column] = true;
        gridColor[row][column] = color;
    }
    private void emptyCell(int row, int column) {
        grid[row][column] = false;
        gridColor[row][column] = null;
    }
    // Auxiliary row operations
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
    private void emptyRow(int row) {
        for (int column = 0; column < getNumberOfColumns(); column++) {
            emptyCell(row, column);
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
