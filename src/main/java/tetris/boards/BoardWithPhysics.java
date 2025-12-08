package tetris.boards;

import tetris.boards.physics.Gravity;
import tetris.boards.physics.PlayerInputHandler;
import tetris.boards.physics.rotationSystems.RotationSystem;
import tetris.boards.physics.rotationSystems.RotationSystemFactory;
import tetris.boards.physics.rotationSystems.RotationSystemType;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import java.awt.*;

/**
 * A concrete implementation of {@link Board} that includes a full physics engine.
 * <p>
 * This class is designed for boards that are simulated locally, meaning all movement,
 * gravity, rotation, and collision checks are calculated on this client. This is used
 * for Single Player games and for the "Local Player" board in Multiplayer games.
 * </p>
 */
public abstract class BoardWithPhysics extends Board {
    // --- PHYSICS SUBSYSTEMS ---
    /** Handles wall kicks and rotation rules (SRS, Nintendo, etc.). */
    protected RotationSystem rotationSystem;
    /** Handles automatic downward movement and locking delays. */
    protected Gravity gravity;
    /** Handles user keyboard input (WASD/Arrows) and DAS/ARR timings. */
    protected PlayerInputHandler playerInputHandler;

    /**
     * Constructs a board with active physics.
     *
     * @param x                             X screen coordinate.
     * @param y                             Y screen coordinate.
     * @param gridRows                      Visible rows.
     * @param gridSpawnRows                 Hidden spawn rows.
     * @param gridColumns                   Columns.
     * @param tetrominoesQueueSize          Next queue size.
     * @param tetrominoesQueueGeneratorType RNG algorithm.
     * @param seed                          Random seed.
     * @param rotationSystemType            The ruleset for rotation (e.g., Super Rotation System).
     * @param initialGravity                Cells per frame the piece falls.
     * @param lockDelayFrames               Frames a piece waits on the ground before locking.
     */
    public BoardWithPhysics(int x, int y, int gridRows, int gridSpawnRows, int gridColumns, int tetrominoesQueueSize,
                            TetrominoesGeneratorType tetrominoesQueueGeneratorType, long seed, RotationSystemType rotationSystemType,
                            double initialGravity, int lockDelayFrames) {
        super(x, y, gridRows, gridSpawnRows, gridColumns, tetrominoesQueueSize, tetrominoesQueueGeneratorType, seed);

        rotationSystem = RotationSystemFactory.createRotationSystem(rotationSystemType, grid);
        gravity = new Gravity(grid, initialGravity, lockDelayFrames);
        playerInputHandler = new PlayerInputHandler(grid, rotationSystem, gravity);
    }

    // --- OVERRIDES FOR BOARD LOGIC ---

    /**
     * Delegates the lock check to the {@link Gravity} component.
     */
    @Override
    protected boolean isFallingTetrominoLocked() { return gravity.isLocked(); }

    /**
     * Updates physics components in the specific order required for responsive gameplay.
     * <ol>
     * <li><b>Input:</b> Player moves/rotates the piece first.</li>
     * <li><b>Gravity:</b> The piece falls. This ensures soft-drops/hard-drops from input are processed in the same frame.</li>
     * </ol>
     */
    @Override
    protected void updateFallingTetromino() {
        playerInputHandler.update();
        gravity.update();
    }

    /**
     * When a new piece spawns, it must be registered with the physics components
     * so they know what to move.
     */
    @Override
    protected void setNextTetrominoAsFallingTetromino() {
        super.setNextTetrominoAsFallingTetromino();
        gravity.setFallingTetromino(fallingTetromino);
        playerInputHandler.setFallingTetromino(fallingTetromino);
    }

    /**
     * Draws the shadow (ghost piece) first, then the actual falling piece on top.
     */
    @Override
    protected void drawFallingTetromino(Graphics2D g2) {
        fallingTetrominoShadow.draw(g2);
        super.drawFallingTetromino(g2);
    }

    // --- INPUT PROXIES ---
    // These methods forward input events from the UI layer to the PlayerInputHandler.

    public void moveLeftPressed() { playerInputHandler.moveLeftPressed(); }
    public void moveLeftReleased() { playerInputHandler.moveLeftReleased(); }
    public void moveRightPressed() { playerInputHandler.moveRightPressed(); }
    public void moveRightReleased() { playerInputHandler.moveRightReleased(); }
    public void softDropPressed() { playerInputHandler.softDropPressed(); }
    public void softDropReleased() { playerInputHandler.softDropReleased(); }
    public void hardDropPressed() { playerInputHandler.hardDropPressed(); }
    public void hardDropReleased() { playerInputHandler.hardDropReleased(); }
    public void rotateRightPressed() { playerInputHandler.rotateRightPressed(); }
    public void rotateRightReleased() { playerInputHandler.rotateRightReleased(); }
    public void rotateLeftPressed() { playerInputHandler.rotateLeftPressed(); }
    public void rotateLeftReleased() { playerInputHandler.rotateLeftReleased(); }
    public void flipPressed() { playerInputHandler.flipPressed(); }
    public void flipReleased() { playerInputHandler.flipReleased(); }
}