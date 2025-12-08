package tetris.boards.modernTetris;

import tetris.boards.BoardWithPhysics;
import tetris.boards.components.BoardGrid;
import tetris.boards.components.TetrominoHolder;
import tetris.boards.physics.rotationSystems.RotationSystemType;
import tetris.boards.physics.ReceivedGarbageHandler;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import java.awt.*;
import java.util.Random;

import static client.userInterface.panels.MainPanel.CELL_SIZE;
import static client.userInterface.panels.MainPanel.TETROMINO_HOLDER_WIDTH;
import static tetris.boards.modernTetris.ModernTetrisConfig.*;

/**
 * A concrete implementation of a playable board using Modern Tetris rules.
 * <p>
 * Key features of this implementation include:
 * <ul>
 * <li><b>Hold Piece:</b> Players can swap the falling piece with a stored one using {@link TetrominoHolder}.</li>
 * <li><b>Super Rotation System (SRS):</b> Uses standard wall-kick data defined in {@link tetris.boards.physics.rotationSystems.SuperRotationSystem}.</li>
 * <li><b>7-Bag Generator:</b> Ensures a balanced distribution of pieces.</li>
 * <li><b>Garbage System:</b> Can receive garbage lines from opponents and add them to the grid.</li>
 * <li><b>Gravity Scaling:</b> Implements a specific gravity curve defined in {@link ModernTetrisConfig}.</li>
 * </ul>
 * </p>
 */
public class ModernTetrisBoardWithPhysics extends BoardWithPhysics {
    // --- COMPONENTS ---
    /** The UI component for holding a piece. */
    protected final TetrominoHolder tetrominoHolder;

    // --- GARBAGE SUBSYSTEM ---
    /** Random number generator for determining hole positions in outgoing garbage. */
    protected Random garbageRandom;
    /** Handles the buffering and application of incoming garbage lines. */
    protected ReceivedGarbageHandler receivedGarbageHandler;

    /**
     * Constructs a new Modern Tetris board.
     *
     * @param x    The X screen coordinate.
     * @param y    The Y screen coordinate.
     * @param seed The random seed for piece generation.
     */
    public ModernTetrisBoardWithPhysics(int x, int y, long seed) {
        super(x + TETROMINO_HOLDER_WIDTH, y, BoardGrid.ROWS, BoardGrid.SPAWN_ROWS, BoardGrid.COLUMNS, TETROMINOES_QUEUE_SIZE,
                TetrominoesGeneratorType.RANDOM_BAG_7_TETROMINOES_GENERATOR, seed, RotationSystemType.SUPER_ROTATION_SYSTEM,
                INITIAL_GRAVITY, LOCK_DELAY_FRAMES);

        tetrominoHolder = new TetrominoHolder(x, y + BoardGrid.SPAWN_ROWS * CELL_SIZE, tetrominoesQueue);
        // Adjust the visual X coordinate back to include the holder's width
        this.x = x;

        garbageRandom = new Random();
        receivedGarbageHandler = new ReceivedGarbageHandler();
    }

    /**
     * Attempts to swap the current piece with the held piece.
     * Delegates to {@link TetrominoHolder#hold(tetris.tetrominoes.Tetromino)}.
     */
    public void hold() {
        nextTetromino = tetrominoHolder.hold(fallingTetromino);
    }

    /**
     * Queues garbage lines to be added to the board.
     *
     * @param numberOfGarbageRows Count of rows to add.
     * @param emptyGarbageColumn  Column index for the hole.
     */
    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        receivedGarbageHandler.addGarbage(numberOfGarbageRows, emptyGarbageColumn);
    }

    /**
     * Main update loop.
     * <p>
     * Updates physics (via super) and then processes any pending garbage lines.
     * If adding garbage forces blocks off the top of the screen, the game ends.
     * </p>
     */
    @Override
    public void update() {
        super.update();

        // Apply garbage if conditions are met (e.g., piece just locked or safe to add)
        if (receivedGarbageHandler.updateGarbage(grid, fallingTetromino, isFallingTetrominoLocked()))
            isAlive = false;
    }

    /**
     * Renders the board and the Hold queue.
     *
     * @param g2 The graphics context.
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        tetrominoHolder.draw(g2);
    }

    /**
     * Increases the level and adjusts gravity according to the configuration.
     */
    @Override
    protected void increaseLevel() {
        super.increaseLevel();

        if (level <= MAX_GRAVITY_LEVEL) gravity.increaseGravity(GRAVITY_INCREMENT_PER_LEVEL);
    }

    /**
     * Locks the piece, clears lines, and unlocks the Hold mechanism for the next turn.
     *
     * @return Number of cleared lines.
     */
    @Override
    protected int lockFallingTetrominoAndClearLines() {
        int clearedLines = super.lockFallingTetrominoAndClearLines();

        // Allow holding again for the new piece
        tetrominoHolder.unlock();

        return clearedLines;
    }
}