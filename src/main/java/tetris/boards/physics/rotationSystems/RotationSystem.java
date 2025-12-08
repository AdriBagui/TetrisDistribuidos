package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.Tetromino;

/**
 * Abstract base class representing a rotation system.
 * <p>
 * A rotation system defines the rules for how a {@link Tetromino} rotates within the {@link BoardGrid}.
 * This includes basic rotation (changing orientation) and complex mechanics like "Wall Kicks"
 * (moving the piece slightly to fit it into a valid space if the basic rotation is obstructed).
 * </p>
 */
public abstract class RotationSystem {
    /** The grid on which collision checks are performed during rotation. */
    protected BoardGrid grid;

    /**
     * Constructs a new RotationSystem.
     *
     * @param grid The game board grid used for collision detection.
     */
    public RotationSystem(BoardGrid grid) { this.grid = grid; }

    /**
     * Attempts to rotate the specified tetromino 90 degrees clockwise.
     * <p>
     * Implementation should clone the tetromino, apply the rotation, check for collisions,
     * and apply any necessary wall kicks. If successful, the original tetromino's state is updated.
     * </p>
     *
     * @param tetromino The tetromino to rotate.
     */
    public abstract void rotateRight(Tetromino tetromino);

    /**
     * Attempts to rotate the specified tetromino 90 degrees counter-clockwise.
     * <p>
     * Implementation should clone the tetromino, apply the rotation, check for collisions,
     * and apply any necessary wall kicks. If successful, the original tetromino's state is updated.
     * </p>
     *
     * @param tetromino The tetromino to rotate.
     */
    public abstract void rotateLeft(Tetromino tetromino);

    /**
     * Attempts to rotate the specified tetromino 180 degrees (flip).
     * <p>
     * Not all rotation systems support this action.
     * </p>
     *
     * @param tetromino The tetromino to flip.
     */
    public abstract void flip(Tetromino tetromino);
}