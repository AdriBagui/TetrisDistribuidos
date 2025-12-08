package tetris.boards.physics;

import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.Tetromino;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Handles the logic for receiving and applying garbage lines in multiplayer games.
 * <p>
 * In Modern Tetris, when an opponent clears multiple lines, "Garbage" lines are sent to the
 * other player. This handler queues those incoming attacks and applies them to the board
 * at safe moments (usually after a piece locks), ensuring smooth gameplay flow.
 * </p>
 */
public class ReceivedGarbageHandler {
    /** Queue storing the number of lines for each incoming attack. */
    private final Queue<Byte> garbageRowsToAdd;

    /** Queue storing the column index of the "hole" for each incoming attack. */
    private final Queue<Byte> garbageEmptyColumnsToAdd;

    /**
     * Creates a new Garbage Handler using thread-safe queues.
     */
    public ReceivedGarbageHandler() {
        garbageRowsToAdd = new ConcurrentLinkedDeque<>();
        garbageEmptyColumnsToAdd = new ConcurrentLinkedDeque<>();
    }

    /**
     * Processes pending garbage and applies it to the board if conditions are met.
     * <p>
     * This method is typically called at the end of a frame or after a piece locks.
     * It handles:
     * 1. Checking if there is garbage to add.
     * 2. Pushing the falling tetromino UP if garbage is added underneath it (to prevent it from getting buried).
     * 3. Calling the grid to actually insert the rows.
     * </p>
     *
     * @param grid                    The game board grid.
     * @param fallingTetromino        The currently active piece.
     * @param isFallingTetrominoLocked Whether the current piece has just locked.
     * @return {@code true} if adding garbage caused a "Block Out" (Game Over), {@code false} otherwise.
     */
    public synchronized boolean updateGarbage(BoardGrid grid, Tetromino fallingTetromino, boolean isFallingTetrominoLocked) {
        boolean overflow = false;
        int distanceToFloor;
        byte garbageRows;

        while (!garbageRowsToAdd.isEmpty()) {
            garbageRows = garbageRowsToAdd.remove();

            // If the piece is still falling, we need to adjust its Y position.
            // If the garbage raises the floor higher than the piece, the piece must be pushed up.
            if (!isFallingTetrominoLocked) {
                distanceToFloor = grid.distanceToFloor(fallingTetromino);

                // If the new garbage height exceeds the gap between the piece and the floor...
                if (distanceToFloor < garbageRows) {
                    // Move the piece up to sit on top of the new garbage
                    fallingTetromino.setY(Math.max(fallingTetromino.getY() - garbageRows + distanceToFloor, 0));
                }
            }

            // Apply the garbage to the grid and check for game over (overflow)
            if (grid.addGarbage(garbageRows, garbageEmptyColumnsToAdd.remove()))
                overflow = true;
        }

        return overflow;
    }

    /**
     * Adds a new garbage attack to the processing queue.
     * <p>
     * This is thread-safe and can be called from the network thread.
     * </p>
     *
     * @param numberOfGarbageRows The number of lines to add.
     * @param emptyGarbageColumn  The column index (0-9) that should be empty (the hole).
     */
    public synchronized void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        garbageRowsToAdd.add(numberOfGarbageRows);
        garbageEmptyColumnsToAdd.add(emptyGarbageColumn);
    }
}