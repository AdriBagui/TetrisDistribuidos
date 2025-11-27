package tetris;

import tetris.generators.RandomBagTetrominoesGenerator;
import tetris.movement.TetrominoMovement;
import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoCell;
import tetris.tetrominoes.TetrominoShadow;

import java.awt.*;
import java.util.Random;

import static tetris.Config.*;

// ==========================================
// CLASS: Board
// Logic for a single player's 10x20 grid
// ==========================================
public class Board {
    private int playerID;
    private int x, y;
    private boolean[][] grid;// 20 rows, 10 cols
    private Color[][] gridColor;
    private Tetromino fallingTetromino;
    private TetrominoShadow fallingTetrominoShadow;
    private Tetromino nextTetromino;
    private Queue queue;
    private Hold hold;
    private TetrominoMovement tetrominoMovement;
    private int score, linesCleared, level;
    private boolean isAlive;
    private Board enemyBoard;
    private Random random;

    public Board(int playerID, long seed, int x, int y) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;

        grid = new boolean[BOARD_ROWS + BOARD_SPAWN_ROWS][BOARD_COLUMNS];
        gridColor = new Color[BOARD_ROWS + BOARD_SPAWN_ROWS][BOARD_COLUMNS];
        queue = new Queue(5, new RandomBagTetrominoesGenerator(seed), x + BOARD_COLUMNS * CELL_SIZE, y + BOARD_SPAWN_ROWS * CELL_SIZE);
        hold = new Hold(x - HOLD_WIDTH, y + BOARD_SPAWN_ROWS * CELL_SIZE, queue);
        tetrominoMovement = new TetrominoMovement(grid);
        score = 0;
        linesCleared = 0;
        level = 1;
        isAlive = true;
        random = new Random();

        nextTetromino = queue.getNext(); // Initialize first block
        fallingTetromino = null;
        fallingTetrominoShadow = null;
    }

    public int getLevel() { return level; }
    public int getScore() { return score; }
    public Tetromino getFallingTetromino() { return fallingTetromino; }
    public boolean isAlive() { return isAlive; }
    public void setEnemyBoard(Board enemyBoard) { this.enemyBoard = enemyBoard; }
    public void addGarbage(int lines) {
        int columnaQueMeLaChupa = random.nextInt(BOARD_COLUMNS);

        for (int y = 0; y < BOARD_ROWS + BOARD_SPAWN_ROWS - lines; y++) {
            System.arraycopy(grid[y + lines], 0, grid[y], 0, BOARD_COLUMNS);
            System.arraycopy(gridColor[y + lines], 0, gridColor[y], 0, BOARD_COLUMNS);
        }
        for (int y = BOARD_ROWS + BOARD_SPAWN_ROWS - 1; y > BOARD_ROWS + BOARD_SPAWN_ROWS - lines - 1l; y--) {
            for (int x = 0; x < BOARD_COLUMNS; x++) {
                if(x == columnaQueMeLaChupa) {
                    grid[y][x] = false;
                    gridColor[y][x] = null;
                } else {
                    grid[y][x] = true;
                    gridColor[y][x] = Color.GRAY;
                }
            }
        }
    }

    // Controls
    public void moveLeftPressed() { tetrominoMovement.moveLeftPressed(); }
    public void moveLeftReleased() { tetrominoMovement.moveLeftReleased(); }
    public void moveRightPressed() { tetrominoMovement.moveRightPressed(); }
    public void moveRightReleased() { tetrominoMovement.moveRightReleased(); }
    public void moveDownPressed() { tetrominoMovement.moveDownPressed(); }
    public void moveDownReleased() { tetrominoMovement.moveDownReleased(); }
    public void dropPressed() { tetrominoMovement.dropPressed(); }
    public void dropReleased() { tetrominoMovement.dropReleased(); }
    public void rotateRightPressed() { tetrominoMovement.rotateRightPressed(); }
    public void rotateRightReleased() { tetrominoMovement.rotateRightReleased(); }
    public void rotateLeftPressed() { tetrominoMovement.rotateLeftPressed(); }
    public void rotateLeftReleased() { tetrominoMovement.rotateLeftReleased(); }
    public void flipPressed() { tetrominoMovement.flipPressed(); }
    public void flipReleased() { tetrominoMovement.flipReleased(); }
    public void hold() { nextTetromino = hold.hold(fallingTetromino); }

    public synchronized void update() {
        if (nextTetromino != null) {
            updateFallingTetromino();
            nextTetromino = null;

            if (tetrominoMovement.thereIsCollision()) {
                isAlive = false;
            }
        }

        if (!isAlive) return;

        tetrominoMovement.update();
        updateFallingTetrominoShadow();

        if (tetrominoMovement.isLocked()) {
            lockTetromino();
            clearLines();
            nextTetromino = queue.getNext();;
        }
    }
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
        if (fallingTetromino != null) {
            fallingTetrominoShadow.draw(g2);
            fallingTetromino.draw(g2);
        }

        // Draw Border
        g2.setColor(Color.WHITE);
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT, x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);
        g2.drawLine(x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT, x + BOARD_WIDTH, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT);

        // Draw Level
        g2.setColor(Color.WHITE);
        g2.drawString("Level: " + getLevel(), x, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 20);

        // Draw Score
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + getScore(), x + 50, y + BOARD_SPAWN_HEIGHT + BOARD_HEIGHT + 20);

        // Draw Held Piece
        hold.draw(g2);

        // Draw Next Piece Preview
        queue.draw(g2);
    }

    private void updateFallingTetromino() {
        nextTetromino.setParentXY(x, y);
        nextTetromino.setXY((BOARD_COLUMNS - nextTetromino.getWidth())/2, 0);

        fallingTetromino = nextTetromino;
        fallingTetrominoShadow = new TetrominoShadow(fallingTetromino);
        tetrominoMovement.setFallingTetromino(fallingTetromino);
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

        hold.unlockHold();
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
            tetrominoMovement.increaseGravity();
        }

        level = (this.linesCleared / 10) + 1;
    }

    private void updateFallingTetrominoShadow() {
        synchronized (fallingTetrominoShadow) {
            fallingTetrominoShadow.setXYRotationIndex(fallingTetromino.getX(), fallingTetromino.getY(), fallingTetromino.getRotationIndex());
            tetrominoMovement.drop(fallingTetrominoShadow);
        }
    }
}