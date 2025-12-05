package tetris.tetrio.boards;

import tetris.general.boards.BoardGrid;
import tetris.general.tetrominoes.Tetromino;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GarbageRecievingSystem {
    private Queue<Byte> garbageRowsToAdd;
    private Queue<Byte> garbageEmptyColumnsToAdd;

    public GarbageRecievingSystem() {
        garbageRowsToAdd = new ConcurrentLinkedDeque<>();
        garbageEmptyColumnsToAdd = new ConcurrentLinkedDeque<>();
    }

    public synchronized boolean updateGarbage(BoardGrid grid, Tetromino fallingTetromino, boolean isFallingTetrominoLocked)  {
        boolean overflow = false;
        int distanceToFloor;
        byte garbageRows;

        while (!garbageRowsToAdd.isEmpty()) {
            garbageRows = garbageRowsToAdd.remove();

            // Si no ha sido ya fijado al tablero
            if (!isFallingTetrominoLocked) { // No es null si acaba de ser fijado en el mismo frame
                distanceToFloor = grid.distanceToFloor(fallingTetromino);

                // Si las filas de basura llegan a la altura del tetromino
                if (distanceToFloor < garbageRows) {
                    fallingTetromino.setY(Math.max(fallingTetromino.getY() - garbageRows + distanceToFloor, 0));
                }
            }

            // Si ha habido overflow al añadir basura se marca
            if (grid.addGarbage(garbageRows, garbageEmptyColumnsToAdd.remove()))
                overflow = true;
        }

        return overflow;
    }

    public synchronized void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        garbageRowsToAdd.add(numberOfGarbageRows);
        garbageEmptyColumnsToAdd.add(emptyGarbageColumn);
    }
}
