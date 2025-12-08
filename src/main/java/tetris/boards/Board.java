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

public abstract class Board {
    // BOARD POSITION IN PANEL
    protected int x;
    protected int y;
    // FALLING TETROMINO
    protected Tetromino fallingTetromino;
    protected Tetromino nextTetromino;
    // FALLING TETROMINO SHADOW
    protected TetrominoShadow fallingTetrominoShadow;
    // BOARD COMPONENTS
    protected final BoardGrid grid;
    private final int gridX;
    private final int gridY;
    protected final TetrominoesQueue tetrominoesQueue;
    // SCORING SYSTEM
    protected int score, totalClearedLines, level;
    // END OF GAME DETECTION
    protected boolean isAlive;

    /**
     * Creates the board to play tetris.
     * @param x x coordinate for the top left corner of the grid.
     * @param y y coordinate for the top left corner of the grid.
     * @param gridRows Number of rows in the grid.
     * @param gridSpawnRows Number of rows above the grid to spawn tetrominoes.
     * @param gridColumns Number of columns in the grid.
     * @param tetrominoesQueueSize Size of the queue of tetrominoes that is seen.
     * @param tetrominoesQueueGeneratorType Type of tetrominoes generator to change randomness type.
     * @param seed Seed for the tetromino's order.
     */
    public Board(int x, int y, int gridRows, int gridSpawnRows, int gridColumns, int tetrominoesQueueSize,
                 TetrominoesGeneratorType tetrominoesQueueGeneratorType, long seed) {
        this.x = x;
        this.y = y;
        gridX = x;
        gridY = y;

        grid = new BoardGrid(x, y, gridRows, gridSpawnRows, gridColumns);
        TetrominoesGenerator tetrominoesGenerator = TetrominoesGeneratorFactory.createTetrominoesGenerator(tetrominoesQueueGeneratorType, seed);
        tetrominoesQueue = new TetrominoesQueue(tetrominoesQueueSize, tetrominoesGenerator, x + gridColumns*CELL_SIZE, y + gridSpawnRows*CELL_SIZE);

        score = 0;
        totalClearedLines = 0;
        level = 0;

        isAlive = true;

        nextTetromino = tetrominoesQueue.getNext();
        fallingTetromino = null;
    }

    // ABSTRACT METHODS

    /**
     * Checks if the falling tetromino has been locked in place (e.g., by gravity or hard drop).
     * @return true if locked, false otherwise.
     */
    protected abstract boolean isFallingTetrominoLocked();

    /**
     * Updates the logic for the falling tetromino (movement, gravity, input).
     */
    protected abstract void updateFallingTetromino();

    // PUBLIC INTERFACE

    /**
     * Checks if the game is still running (player hasn't topped out).
     * @return true if alive, false if game over.
     */
    public boolean isAlive() { return isAlive; }

    /**
     * Returns the score of the board
     * @return the score of the board
     */
    public int getScore() { return score; }

    /**
     * Main update loop for the board.
     * Handles spawning new pieces, checking collisions, updating physics, and locking pieces.
     */
    public void update() {
        if (nextTetromino != null) {
            setNextTetrominoAsFallingTetromino();
            nextTetromino = null;

            if (grid.hasCollision(fallingTetromino)) {
                isAlive = false;
                return;
            }
        }

        updateFallingTetromino();
        updateFallingTetrominoShadow();

        if (isFallingTetrominoLocked()) { lockFallingTetrominoAndClearLines(); }
    }

    /**
     * Renders the board and all its components (grid, active piece, UI).
     * @param g2 the Graphics2D context.
     */
    public void draw(Graphics2D g2) {
        // Draws the board (with its borders and template)
        grid.draw(g2);

        // Draws the falling tetromino
        if (fallingTetromino != null) drawFallingTetromino(g2);


        g2.setColor(TEXT_COLOR);
        g2.setFont(SUBTITLE_FONT);
        // Draw Level
        g2.drawString("Level: " + level, gridX, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 50);
        // Draw Score
        g2.drawString("Score: " + score, gridX + 120, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 50);


        // Draw Next Piece Preview
        tetrominoesQueue.draw(g2);
    }

    // AUXILIARY METHODS

    /**
     * Promotes the next tetromino in the queue to be the currently falling one.
     * Resets its position to the spawn point.
     */
    protected void setNextTetrominoAsFallingTetromino() {
        nextTetromino.setParentXY(grid.getX(), grid.getY());
        // The >> 1 shifts bits one to the right, equivalent to dividing by 2.
        nextTetromino.setXY((grid.getNumberOfColumns() - nextTetromino.getWidth()) >> 1, 0);

        fallingTetromino = nextTetromino;
        fallingTetrominoShadow = new TetrominoShadow(fallingTetromino);
    }

    /**
     * Locks the falling tetromino into the grid and processes line clears.
     * Updates score and level based on cleared lines.
     * @return the number of lines cleared.
     */
    protected int lockFallingTetrominoAndClearLines() {
        int linesCleared = grid.lockTetrominoAndClearLines(fallingTetromino);

        // NES Scoring: 40, 100, 300, 1200
        switch (linesCleared) {
            case 1:
                score += 40;
                break;
            case 2:
                score += 100;
                break;
            case 3:
                score += 300;
                break;
            case 4:
                score += 1200;
                break;
        }

        totalClearedLines += linesCleared;

        while (level < (totalClearedLines / 10)) {
            increaseLevel();
        }

        nextTetromino = tetrominoesQueue.getNext();

        return linesCleared;
    }

    /**
     * Increases the game level. Can be overridden for mode-specific behavior.
     */
    protected void increaseLevel() {
        level++;
    }

    /**
     * Draws the currently falling tetromino.
     * @param g2 the Graphics2D context.
     */
    protected void drawFallingTetromino(Graphics2D g2) { fallingTetromino.draw(g2); }

    /**
     * Calculates and updates the position of the ghost piece (shadow).
     */
    private void updateFallingTetrominoShadow() {
        int shadowDistanceToFloor;
        fallingTetrominoShadow.setXYRotationIndex(fallingTetromino.getX(), fallingTetromino.getY(), fallingTetromino.getRotationIndex());
        shadowDistanceToFloor = grid.distanceToFloor(fallingTetrominoShadow);
        fallingTetrominoShadow.setY(fallingTetrominoShadow.getY() + shadowDistanceToFloor);
    }
}