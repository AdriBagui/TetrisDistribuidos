package tetris.boards.modernTetris;

import tetris.boards.ReceiverBoard;
import tetris.boards.components.BoardGrid;
import tetris.boards.components.TetrominoHolder;
import tetris.boards.physics.ReceivedGarbageHandler;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import java.awt.*;

import static client.userInterface.panels.MainPanel.CELL_SIZE;
import static client.userInterface.panels.MainPanel.TETROMINO_HOLDER_WIDTH;
import static tetris.boards.modernTetris.ModernTetrisConfig.TETROMINOES_QUEUE_SIZE;

/**
 * A board that visualizes the state of a remote Modern Tetris player.
 * <p>
 * Unlike the physics board, this class is driven purely by network events. It maintains
 * visual components specific to Modern Tetris (Hold Queue, Garbage indicators) so the
 * local player can see exactly what the opponent is doing.
 * </p>
 */
public class ReceiverModernTetrisBoard extends ReceiverBoard {
    // --- COMPONENTS ---
    /** The visual component for the opponent's held piece. */
    protected final TetrominoHolder tetrominoHolder;

    // --- GARBAGE SUBSYSTEM ---
    /** Handles the visual application of garbage sent to the opponent. */
    protected ReceivedGarbageHandler receivedGarbageHandler;

    /**
     * Constructs a receiver board for Modern Tetris.
     *
     * @param x    X screen coordinate.
     * @param y    Y screen coordinate.
     * @param seed Random seed (synchronized with opponent).
     */
    public ReceiverModernTetrisBoard(int x, int y, long seed) {
        super(x + TETROMINO_HOLDER_WIDTH, y, BoardGrid.ROWS, BoardGrid.SPAWN_ROWS, BoardGrid.COLUMNS, TETROMINOES_QUEUE_SIZE,
                TetrominoesGeneratorType.RANDOM_BAG_7_TETROMINOES_GENERATOR, seed);

        tetrominoHolder = new TetrominoHolder(x, y + BoardGrid.SPAWN_ROWS * CELL_SIZE, tetrominoesQueue);
        // Correct the visual X offset
        this.x = x;

        receivedGarbageHandler = new ReceivedGarbageHandler();
    }

    /**
     * Triggered by a network message when the opponent holds a piece.
     * Updates the visual state of the holder.
     */
    public void hold() {
        nextTetromino = tetrominoHolder.hold(fallingTetromino);
    }

    /**
     * Triggered by a network message when the opponent receives garbage.
     *
     * @param numberOfGarbageRows Count of rows.
     * @param emptyGarbageColumn  Hole index.
     */
    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        receivedGarbageHandler.addGarbage(numberOfGarbageRows, emptyGarbageColumn);
    }

    /**
     * Updates the visual state, including processing any pending garbage animations.
     */
    @Override
    public void update() {
        super.update();

        if (receivedGarbageHandler.updateGarbage(grid, fallingTetromino, isFallingTetrominoLocked()))
            isAlive = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        tetrominoHolder.draw(g2);
    }

    /**
     * Handles piece locking and unlocks the holder (visual state only).
     */
    @Override
    protected int lockFallingTetrominoAndClearLines() {
        int clearedLines = super.lockFallingTetrominoAndClearLines();
        tetrominoHolder.unlock();
        return clearedLines;
    }
}