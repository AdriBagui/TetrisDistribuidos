package tetris.general.boards;

import tetris.general.tetrominoes.TetrominoShadow;
import tetris.general.tetrominoes.generators.RandomBagTetrominoesGenerator;
import tetris.general.tetrominoes.Tetromino;

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
    protected final TetrominoHolder tetrominoHolder;
    protected final BoardGrid grid;
    private final TetrominoesQueue tetrominoesQueue;
    // SCORING SYSTEM
    protected int score, totalClearedLines, level;
    // END OF GAME DETECTION
    protected boolean isAlive;

    public Board(int x, int y, BoardGrid grid, long seed) {
        this.x = x;
        this.y = y;

        this.grid = grid;
        tetrominoesQueue = new TetrominoesQueue(TETROMINOES_QUEUE_SIZE, new RandomBagTetrominoesGenerator(seed), x + TETROMINO_HOLDER_WIDTH + BOARD_WIDTH, y + BOARD_SPAWN_ROWS * CELL_SIZE);
        tetrominoHolder = new TetrominoHolder(x, y + BOARD_SPAWN_ROWS * CELL_SIZE, tetrominoesQueue);

        score = 0;
        totalClearedLines = 0;
        level = 1;

        isAlive = true;

        nextTetromino = tetrominoesQueue.getNext(); // Initialize first block
        fallingTetromino = null;
    }

    protected abstract boolean isFallingTetrominoLocked();
    protected abstract void updateFallingTetromino();

    public boolean isAlive() { return isAlive; }
    public void hold() { nextTetromino = tetrominoHolder.hold(fallingTetromino); }
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
        g2.setColor(Color.WHITE);
        g2.drawString("Level: " + level, x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 20);

        // Draw Score
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + score, x + 50, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 20);

        // Draw Held Piece
        tetrominoHolder.draw(g2);

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
        tetrominoHolder.unlockHold();

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