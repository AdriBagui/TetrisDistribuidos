package tetris.general.boards;

import tetris.general.tetrominoes.TetrominoShadow;
import tetris.general.tetrominoes.generators.RandomBagTetrominoesGenerator;
import tetris.general.tetrominoes.Tetromino;
import tetris.general.tetrominoes.generators.TetrominoesGenerator;
import tetris.general.tetrominoes.generators.TetrominoesGeneratorFactory;

import java.awt.*;

import static tetris.Config.*;

public abstract class Board {
    // BOARD POSITION IN PANEL
    private final int x, y;
    // FALLING TETROMINO
    protected Tetromino fallingTetromino;
    protected Tetromino nextTetromino;
    // FALLING TETROMINO SHADOW
    protected TetrominoShadow fallingTetrominoShadow;
    // BOARD COMPONENTS
    protected final BoardGrid grid;
    protected final TetrominoHolder tetrominoHolder;
    private final int tetrominoHolderWidth;
    private final TetrominoesQueue tetrominoesQueue;
    // SCORING SYSTEM
    protected int score, totalClearedLines, level;
    // END OF GAME DETECTION
    protected boolean isAlive;

    /**
     * Creates the board to play tetris
     * @param x x coordinate for the top left corner
     * @param y y coordinate for the top left corner
     * @param gridRows Number of rows in the grid
     * @param gridSpawnRows Number of rows above the grid to spawn tetrominoes
     * @param gridColumns Number of columns in the grid
     * @param tetrominoesQueueSize Size of the queue of tetrominoes that is seen
     * @param tetrominoesQueueGeneratorType Type of tetrominoes generator to change randomness type
     * @param seed Seed for the tetromino's order
     * @param hasHolder True if the user can hold pieces false otherwise
     */
    public Board(int x, int y, int gridRows, int gridSpawnRows, int gridColumns, int tetrominoesQueueSize, int tetrominoesQueueGeneratorType, long seed, boolean hasHolder) {
        this.x = x;
        this.y = y;

        tetrominoHolderWidth = hasHolder ? TETROMINO_HOLDER_WIDTH : 0;

        grid = new BoardGrid(x + tetrominoHolderWidth, y, gridRows, gridSpawnRows, gridColumns);
        TetrominoesGenerator tetrominoesGenerator = TetrominoesGeneratorFactory.createTetrominoesGenerator(tetrominoesQueueGeneratorType, seed);
        tetrominoesQueue = new TetrominoesQueue(tetrominoesQueueSize, tetrominoesGenerator, x + tetrominoHolderWidth + gridColumns*CELL_SIZE, y + gridSpawnRows*CELL_SIZE);
        tetrominoHolder = hasHolder ? new TetrominoHolder(x, y + gridSpawnRows*CELL_SIZE, tetrominoesQueue) : null;

        score = 0;
        totalClearedLines = 0;
        level = 1;

        isAlive = true;

        nextTetromino = tetrominoesQueue.getNext();
        fallingTetromino = null;
    }

    protected abstract boolean isFallingTetrominoLocked();
    protected abstract void updateFallingTetromino();

    public boolean isAlive() { return isAlive; }
    public void hold() { if (tetrominoHolder != null) nextTetromino = tetrominoHolder.hold(fallingTetromino); }
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
    public void draw(Graphics2D g2) {
        // Dibuja el tablero (con sus bordes y la plantilla)
        grid.draw(g2);

        // Dibuja el tetromino que está cayendo
        if (fallingTetromino != null) drawFallingTetromino(g2);

        // Draw Level
        g2.setColor(new Color(220, 220 , 220));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
        g2.drawString("Level: " + level, tetrominoHolderWidth + x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 50);

        // Draw Score
        g2.setColor(new Color(220, 220 , 220));
        g2.drawString("Score: " + score, tetrominoHolderWidth + x + 120, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 50);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // Draw Held Piece
        if (tetrominoHolder != null) tetrominoHolder.draw(g2);

        // Draw Next Piece Preview
        tetrominoesQueue.draw(g2);
    }

    // Auxiliary methods
    protected void setNextTetrominoAsFallingTetromino() {
        nextTetromino.setParentXY(grid.getX(), grid.getY());
        // El >> 1 lo ha sugerido IntelIJ (es como el SHR 1 de ensamblador),
        // es decir, mueve los bits uno para la derecha que es equivalente a dividir entre 2.
        nextTetromino.setXY((BOARD_COLUMNS - nextTetromino.getWidth()) >> 1, 0);

        fallingTetromino = nextTetromino;
        fallingTetrominoShadow = new TetrominoShadow(fallingTetromino);
    }
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

//        if (level < (this.totalClearedLines / 10) + 1 && level < 21) {
//            boardPhysics.increaseGravity();
//        }

        level = (this.totalClearedLines / 10) + 1;

        this.totalClearedLines += linesCleared;

        nextTetromino = tetrominoesQueue.getNext();
        if (tetrominoHolder != null) tetrominoHolder.unlockHold();

        return linesCleared;
    }
    protected void drawFallingTetromino(Graphics2D g2) { fallingTetromino.draw(g2); }
    private void updateFallingTetrominoShadow() {
        int shadowDistanceToFloor;
        fallingTetrominoShadow.setXYRotationIndex(fallingTetromino.getX(), fallingTetromino.getY(), fallingTetromino.getRotationIndex());
        shadowDistanceToFloor = grid.distanceToFloor(fallingTetrominoShadow);
        fallingTetrominoShadow.setY(fallingTetrominoShadow.getY() + shadowDistanceToFloor);
    }
}