package src.tetris.boards;

import src.tetris.physics.CollisionDetector;
import src.tetris.tetrominoes.generators.RandomBagTetrominoesGenerator;
import src.tetris.physics.BoardPhysics;
import src.tetris.tetrominoes.Tetromino;
import src.tetris.tetrominoes.TetrominoCell;

import java.awt.*;

import static src.tetris.Config.*;

// ==========================================
// CLASS: Board
// Logic for a single player's 10x20 grid
// ==========================================
public abstract class Board {
    // BOARD POSITION IN PANEL
    private int x, y;
    // GRID
    protected boolean[][] grid; // 20 rows, 10 cols
    protected Color[][] gridColor;
    // TETROMINOES
    protected Tetromino fallingTetromino;
    protected Tetromino nextTetromino;
    // BOARD LOGIC
    private final TetrominoesQueue tetrominoesQueue;
    protected final TetrominoHolder tetrominoHolder;
    protected boolean holdTetromino;
    protected final BoardPhysics boardPhysics;
    // SCORING SYSTEM
    private int score, linesCleared, level;
    // GARBAGE SYSTEM
    protected Board enemyBoard;
    protected int garbageLinesToAdd;
    // END OF GAME DETECTION
    private boolean isAlive;

    public Board(int x, int y, long seed) {
        this.x = x;
        this.y = y;

        grid = new boolean[BOARD_ROWS + BOARD_SPAWN_ROWS][BOARD_COLUMNS];
        gridColor = new Color[BOARD_ROWS + BOARD_SPAWN_ROWS][BOARD_COLUMNS];

        tetrominoesQueue = new TetrominoesQueue(TETROMINOES_QUEUE_SIZE, new RandomBagTetrominoesGenerator(seed), x + BOARD_COLUMNS * CELL_SIZE, y + BOARD_SPAWN_ROWS * CELL_SIZE);
        tetrominoHolder = new TetrominoHolder(x - TETROMINO_HOLDER_WIDTH, y + BOARD_SPAWN_ROWS * CELL_SIZE, tetrominoesQueue);
        boardPhysics = initializeBoardPhysics();

        score = 0;
        linesCleared = 0;
        level = 1;

        garbageLinesToAdd = 0;

        isAlive = true;

        nextTetromino = tetrominoesQueue.getNext(); // Initialize first block
        fallingTetromino = null;
    }

    protected abstract BoardPhysics initializeBoardPhysics();
    protected abstract void updateGarbage();

    public void setEnemyBoard(Board enemyBoard) { this.enemyBoard = enemyBoard; }
    public void addGarbage(int lines) { this.garbageLinesToAdd += lines; }
    public void hold() { nextTetromino = tetrominoHolder.hold(fallingTetromino); }

    public boolean isAlive() { return isAlive; }

    public void update() {
        if (nextTetromino != null) {
            setNextTetrominoAsFallingTetromino();
            nextTetromino = null;
        }

        if (CollisionDetector.checkCollision(grid, fallingTetromino)) {
            isAlive = false;
            return;
        }

        updateFallingTetromino();

        if (boardPhysics.isLocked()) { lockTetromino(); }
        if (garbageLinesToAdd > 0) { updateGarbage(); }
    }
    protected void updateFallingTetromino() { boardPhysics.update(); }

    public void draw(Graphics2D g2) {
        // Draw Grid Background
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y + BOARD_SPAWN_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT);

        // Draw Grid Lines
        g2.setColor(new Color(255, 255, 255, 64));
        for (int r = CELL_SIZE; r < BOARD_HEIGHT; r += CELL_SIZE) {
            g2.drawLine(x, y + BOARD_SPAWN_HEIGHT + r, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + r);
        }
        for (int c = CELL_SIZE; c < BOARD_WIDTH; c += CELL_SIZE) {
            g2.drawLine(x + c, y + BOARD_SPAWN_HEIGHT, x + c, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        }

        // Draw Static Blocks
        for (int r = 0; r < BOARD_ROWS + BOARD_SPAWN_ROWS; r++) {
            for (int c = 0; c < BOARD_COLUMNS; c++) {
                if (grid[r][c]) {
                    TetrominoCell.draw(g2, x + c * CELL_SIZE, y + r * CELL_SIZE, gridColor[r][c]);
                }
            }
        }

        // Draw Current Falling Tetromino
        if (fallingTetromino != null) drawFallingTetromino(g2);

        // Draw Border
        g2.setColor(Color.WHITE);
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT, x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);

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
    protected void drawFallingTetromino(Graphics2D g2) { fallingTetromino.draw(g2); }

    protected void setNextTetrominoAsFallingTetromino() {
        nextTetromino.setParentXY(x, y);
        nextTetromino.setXY((BOARD_COLUMNS - nextTetromino.getWidth())/2, 0);

        fallingTetromino = nextTetromino;
        boardPhysics.setFallingTetromino(fallingTetromino);
    }

    private void lockTetromino() {
        boolean[][] tetrominoShape = fallingTetromino.getShape();
        int x = fallingTetromino.getX();
        int y = fallingTetromino.getY();

        for (int r = 0; r < tetrominoShape.length; r++) {
            for (int c = 0; c < tetrominoShape[0].length; c++) {
                if(tetrominoShape[r][c]) {
                    grid[y+r][x+c] = true;
                    gridColor[y+r][x+c] = fallingTetromino.getColor();
                }
            }
        }

        clearLines();
        nextTetromino = tetrominoesQueue.getNext();;

        tetrominoHolder.unlockHold();
    }
    private void clearLines() {
        int linesCleared = 0;
        for (int r = 0; r < BOARD_ROWS + BOARD_SPAWN_ROWS; r++) {
            boolean full = true;
            for (int c = 0; c < BOARD_COLUMNS; c++) {
                if (!grid[r][c]) {
                    full = false;
                    break;
                }
            }
            if (full) {
                linesCleared++;
                // Shift down
                for (int y = r; y > 0; y--) {
                    System.arraycopy(grid[y - 1], 0, grid[y], 0, BOARD_COLUMNS);
                    System.arraycopy(gridColor[y - 1], 0, gridColor[y], 0, BOARD_COLUMNS);
                }
                // Clear top
                for (int c = 0; c < BOARD_COLUMNS; c++) {
                    grid[0][c] = false;
                    gridColor[0][c] = null;
                }
            }
        }
        // NES Scoring: 40, 100, 300, 1200
        switch (linesCleared) {
            case 1:
                score += 40;
                break;
            case 2:
                score += 100;
                enemyBoard.addGarbage(1);
                break;
            case 3:
                score += 300;
                enemyBoard.addGarbage(2);
                break;
            case 4:
                score += 1200;
                enemyBoard.addGarbage(4);
                break;
        }

        this.linesCleared += linesCleared;

        if (level < (this.linesCleared / 10) + 1 && level < 21) {
            //boardPhysics.increaseGravity();
        }

        level = (this.linesCleared / 10) + 1;
    }
}