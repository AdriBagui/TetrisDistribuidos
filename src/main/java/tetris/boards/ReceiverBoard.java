package tetris.boards;

import tetris.tetrominoes.generators.TetrominoesGeneratorType;

/**
 * A board implementation used to replicate the state of a remote player.
 * <p>
 * Unlike {@link BoardWithPhysics}, this class does <b>not</b> calculate gravity or process
 * keyboard inputs. Instead, it receives absolute position updates (X, Y, Rotation) from
 * the network (via {@link tetris.boards.io.ReceiverBoardInputHandler}) and interpolates
 * the display. This ensures the local view matches exactly what the opponent sees.
 * </p>
 */
public abstract class ReceiverBoard extends Board {
    // Buffer variables to store the next state received from the network
    private int fallingTetrominoNextX;
    private int fallingTetrominoNextY;
    private int fallingTetrominoNextRotationIndex;
    private boolean isFallingTetrominoLocked;

    /**
     * Constructs a ReceiverBoard.
     *
     * @param x                             X screen coordinate.
     * @param y                             Y screen coordinate.
     * @param gridRows                      Visible rows.
     * @param gridSpawnRows                 Hidden spawn rows.
     * @param gridColumns                   Columns.
     * @param tetrominoesQueueSize          Next queue size.
     * @param tetrominoesQueueGeneratorType RNG algorithm (must match sender).
     * @param seed                          Random seed (must match sender).
     */
    public ReceiverBoard(int x, int y, int gridRows, int gridSpawnRows, int gridColumns, int tetrominoesQueueSize,
                         TetrominoesGeneratorType tetrominoesQueueGeneratorType, long seed) {
        super(x, y, gridRows, gridSpawnRows, gridColumns, tetrominoesQueueSize, tetrominoesQueueGeneratorType, seed);

        // Initialize to safe defaults (off-screen)
        fallingTetrominoNextX = -128;
        fallingTetrominoNextY = -128;
        fallingTetrominoNextRotationIndex = -1;
        isFallingTetrominoLocked = false;
    }

    /**
     * Updates the target position for the falling piece.
     * Called by the InputHandler when a network packet arrives.
     *
     * @param x             The new X coordinate.
     * @param y             The new Y coordinate.
     * @param rotationIndex The new rotation state.
     */
    public void setFallingTetrominoXYRotationIndex(int x, int y, int rotationIndex) {
        this.fallingTetrominoNextX = x;
        this.fallingTetrominoNextY = y;
        this.fallingTetrominoNextRotationIndex = rotationIndex;
    }

    /**
     * Signals that the current piece has locked on the remote side.
     */
    public void lockFallingTetromino() { isFallingTetrominoLocked = true; }

    @Override
    protected boolean isFallingTetrominoLocked() {
        return isFallingTetrominoLocked;
    }

    /**
     * Applies the buffered network state to the actual game object.
     */
    @Override
    protected void updateFallingTetromino() {
        fallingTetromino.setXYRotationIndex(fallingTetrominoNextX, fallingTetrominoNextY, fallingTetrominoNextRotationIndex);
    }

    /**
     * When a new piece spawns, reset the lock flag.
     */
    @Override
    protected void setNextTetrominoAsFallingTetromino() {
        super.setNextTetrominoAsFallingTetromino();
        isFallingTetrominoLocked = false;
    }
}