package tetris.boards;

import tetris.boards.components.BoardGrid;
import tetris.boards.components.TetrominoesQueue;
import tetris.tetrominoes.TetrominoShadow;
import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.generators.TetrominoesGenerator;
import tetris.tetrominoes.generators.TetrominoesGeneratorFactory;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

/**
 * An abstract representation of a Tetris game board.
 * <p>
 * This class serves as the root for all specific board implementations (e.g., Single Player,
 * Multiplayer Sender/Receiver). It manages the core components of the game state, including:
 * <ul>
 * <li>The game grid (walls, locked blocks).</li>
 * <li>The currently falling piece and the "next" queue.</li>
 * <li>The scoring and leveling system.</li>
 * <li>Rendering logic for the board and its UI elements.</li>
 * </ul>
 * </p>
 */
public abstract class Board {
    // --- BOARD POSITION IN PANEL ---
    /** The x-coordinate where the board is drawn on the panel. */
    protected int x;
    /** The y-coordinate where the board is drawn on the panel. */
    protected int y;

    // --- TETROMINO STATE ---
    /** The piece currently under player control or active gravity. */
    protected Tetromino fallingTetromino;
    /** The piece waiting to be spawned next. */
    protected Tetromino nextTetromino;
    /** The ghost piece showing where the falling tetromino would land. */
    protected TetrominoShadow fallingTetrominoShadow;

    // --- BOARD COMPONENTS ---
    /** The logical grid representing the playfield. */
    protected final BoardGrid grid;
    private final int gridX;
    private final int gridY;
    /** The component managing the queue of upcoming pieces. */
    protected final TetrominoesQueue tetrominoesQueue;

    // --- SCORING SYSTEM ---
    protected int score;
    protected int totalClearedLines;
    protected int level;

    // --- GAME STATE ---
    /** Flag indicating if the game is active (true) or game over (false). */
    protected boolean isAlive;

    /**
     * Initializes the base board structure.
     *
     * @param x                             X coordinate for the top-left corner of the grid display.
     * @param y                             Y coordinate for the top-left corner of the grid display.
     * @param gridRows                      Number of visible rows in the grid.
     * @param gridSpawnRows                 Number of invisible buffer rows above the grid.
     * @param gridColumns                   Number of columns in the grid.
     * @param tetrominoesQueueSize          Number of upcoming pieces to display.
     * @param tetrominoesQueueGeneratorType The algorithm for generating pieces (e.g., 7-Bag or Random).
     * @param seed                          The random seed ensuring deterministic piece generation (crucial for multiplayer).
     */
    public Board(int x, int y, int gridRows, int gridSpawnRows, int gridColumns, int tetrominoesQueueSize,
                 TetrominoesGeneratorType tetrominoesQueueGeneratorType, long seed) {
        this.x = x;
        this.y = y;
        gridX = x;
        gridY = y;

        grid = new BoardGrid(x, y, gridRows, gridSpawnRows, gridColumns);
        TetrominoesGenerator tetrominoesGenerator = TetrominoesGeneratorFactory.createTetrominoesGenerator(tetrominoesQueueGeneratorType, seed);
        tetrominoesQueue = new TetrominoesQueue(tetrominoesQueueSize, tetrominoesGenerator, x + gridColumns * CELL_SIZE, y + gridSpawnRows * CELL_SIZE);

        score = 0;
        totalClearedLines = 0;
        level = 0;

        isAlive = true;

        nextTetromino = tetrominoesQueue.getNext();
        fallingTetromino = null;
    }

    // --- ABSTRACT METHODS ---

    /**
     * Determines if the currently falling tetromino has met the conditions to be locked
     * into the grid (e.g., touched the ground for a specific duration).
     *
     * @return {@code true} if the piece should lock, {@code false} otherwise.
     */
    protected abstract boolean isFallingTetrominoLocked();

    /**
     * Updates the position and state of the falling tetromino.
     * This typically involves applying gravity, processing user input, or interpolating network updates.
     */
    protected abstract void updateFallingTetromino();

    // --- PUBLIC INTERFACE ---

    /**
     * Checks if the game session is still active.
     *
     * @return {@code true} if the player has not topped out, {@code false} if Game Over.
     */
    public boolean isAlive() { return isAlive; }

    /**
     * Gets the current score of the board.
     *
     * @return The integer score.
     */
    public int getScore() { return score; }

    /**
     * The main game loop for the board.
     * <p>
     * This method orchestrates the cycle of:
     * 1. Spawning new pieces from the queue.
     * 2. Checking for immediate spawn collisions (Game Over condition).
     * 3. Updating the falling piece's logic.
     * 4. Calculating the ghost piece position.
     * 5. Locking pieces and processing line clears if necessary.
     * </p>
     */
    public void update() {
        // Spawn Phase
        if (nextTetromino != null) {
            setNextTetrominoAsFallingTetromino();
            nextTetromino = null;

            // Immediate Game Over check (Block Out)
            if (grid.hasCollision(fallingTetromino)) {
                isAlive = false;
                return;
            }
        }

        updateFallingTetromino();
        updateFallingTetrominoShadow();

        // Lock Phase
        if (isFallingTetrominoLocked()) { lockFallingTetrominoAndClearLines(); }
    }

    /**
     * Renders the complete board state.
     *
     * @param g2 The {@link Graphics2D} context.
     */
    public void draw(Graphics2D g2) {
        // 1. Draw Grid (Background, locked blocks, borders)
        grid.draw(g2);

        // 2. Draw Active Piece
        if (fallingTetromino != null) drawFallingTetromino(g2);

        // 3. Draw UI Text (Level, Score)
        g2.setColor(TEXT_COLOR);
        g2.setFont(SUBTITLE_FONT);
        g2.drawString("Level: " + level, gridX, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 50);
        g2.drawString("Score: " + score, gridX + 120, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 50);

        // 4. Draw Next Queue
        tetrominoesQueue.draw(g2);
    }

    // --- AUXILIARY METHODS ---

    /**
     * Promotes the next tetromino in the queue to become the active falling tetromino.
     * It resets the piece's position to the spawn point (top center) and initializes its shadow.
     */
    protected void setNextTetrominoAsFallingTetromino() {
        nextTetromino.setParentXY(grid.getX(), grid.getY());
        // Center the piece: (GridCols - PieceWidth) / 2
        nextTetromino.setXY((grid.getNumberOfColumns() - nextTetromino.getWidth()) >> 1, 0);

        fallingTetromino = nextTetromino;
        fallingTetrominoShadow = new TetrominoShadow(fallingTetromino);
    }

    /**
     * Commits the falling tetromino to the grid and clears completed lines.
     * <p>
     * This method handles the core scoring logic based on the NES scoring system
     * (40/100/300/1200 points per line count). It also updates the level.
     * </p>
     *
     * @return The number of lines cleared in this step.
     */
    protected int lockFallingTetrominoAndClearLines() {
        int linesCleared = grid.lockTetrominoAndClearLines(fallingTetromino);

        // Standard NES Scoring
        switch (linesCleared) {
            case 1: score += 40; break;
            case 2: score += 100; break;
            case 3: score += 300; break;
            case 4: score += 1200; break;
        }

        totalClearedLines += linesCleared;

        // Level up every 10 lines
        while (level < (totalClearedLines / 10)) {
            increaseLevel();
        }

        // Prepare the next piece for the next frame
        nextTetromino = tetrominoesQueue.getNext();

        return linesCleared;
    }

    /**
     * Increases the difficulty level of the game.
     * Subclasses can override this to apply side effects (e.g., increasing gravity).
     */
    protected void increaseLevel() {
        level++;
    }

    /**
     * Renders the falling tetromino.
     * Can be overridden to draw additional elements like the shadow (ghost piece).
     *
     * @param g2 The graphics context.
     */
    protected void drawFallingTetromino(Graphics2D g2) { fallingTetromino.draw(g2); }

    /**
     * Updates the position of the ghost piece (shadow).
     * It simulates dropping the current piece until it hits a collision.
     */
    private void updateFallingTetrominoShadow() {
        int shadowDistanceToFloor;
        fallingTetrominoShadow.setXYRotationIndex(fallingTetromino.getX(), fallingTetromino.getY(), fallingTetromino.getRotationIndex());
        shadowDistanceToFloor = grid.distanceToFloor(fallingTetrominoShadow);
        fallingTetrominoShadow.setY(fallingTetrominoShadow.getY() + shadowDistanceToFloor);
    }
}