package tetris;

import tetris.generators.RandomBagTetrominoesGenerator;
import tetris.movement.TetrominoMovement;
import tetris.tetrominoes.Tetromino;
import tetris.tetrominoes.TetrominoCell;
import tetris.tetrominoes.TetrominoShadow;

import java.awt.*;

// ==========================================
// CLASS: Board
// Logic for a single player's 10x20 grid
// ==========================================
public class Board {
    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    public static final int SPAWN_ROWS = 3;

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

    public Board(int playerID, long seed, int x, int y) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;

        grid = new boolean[ROWS + SPAWN_ROWS][COLUMNS];
        gridColor = new Color[ROWS + SPAWN_ROWS][COLUMNS];
        queue = new Queue(5, new RandomBagTetrominoesGenerator(seed), x + COLUMNS * GamePanel.CELL_SIZE, y + SPAWN_ROWS * GamePanel.CELL_SIZE);
        hold = new Hold(x - Hold.WIDTH, y + SPAWN_ROWS * GamePanel.CELL_SIZE, queue);
        tetrominoMovement = new TetrominoMovement(grid);
        score = 0;
        linesCleared = 0;
        level = 1;
        isAlive = true;

        nextTetromino = queue.getNext(); // Initialize first block
        fallingTetromino = null;
        fallingTetrominoShadow = null;
    }

    public int getLevel() { return level; }
    public int getScore() { return score; }
    public Tetromino getFallingTetromino() { return fallingTetromino; }
    public boolean isAlive() { return isAlive; }

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
        int boardWidth = COLUMNS * GamePanel.CELL_SIZE;
        int boardHeight = ROWS * GamePanel.CELL_SIZE;
        int spawnHeight = SPAWN_ROWS * GamePanel.CELL_SIZE;

        // Draw Grid Background
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y + spawnHeight, boardWidth, boardHeight);

        // Draw Grid Lines
        g2.setColor(new Color(255, 255, 255, 64));
        for (int r = GamePanel.CELL_SIZE; r < boardHeight; r += GamePanel.CELL_SIZE) {
            g2.drawLine(x, y + spawnHeight + r, x + boardWidth, y + spawnHeight + r);
        }
        for (int c = GamePanel.CELL_SIZE; c < boardWidth; c += GamePanel.CELL_SIZE) {
            g2.drawLine(x + c, y + spawnHeight, x + c, y + spawnHeight + boardHeight);
        }

        // Draw Static Blocks
        for (int r = 0; r < ROWS + SPAWN_ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (grid[r][c]) {
                    TetrominoCell.draw(g2, x + c * GamePanel.CELL_SIZE, y + r * GamePanel.CELL_SIZE, gridColor[r][c]);
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
        g2.drawLine(x, y + spawnHeight, x, y + spawnHeight + boardHeight);
        g2.drawLine(x + boardWidth, y + spawnHeight, x + boardWidth, y + spawnHeight + boardHeight);
        g2.drawLine(x, y + spawnHeight + boardHeight, x + boardWidth, y + spawnHeight + boardHeight);

        // Draw Level
        g2.setColor(Color.WHITE);
        g2.drawString("Level: " + getLevel(), x, y + spawnHeight + boardHeight + 30);

        // Draw Score
        g2.setColor(Color.WHITE);
        g2.drawString("Score: " + getScore(), x + 50, y + spawnHeight + boardHeight + 30);

        // Draw Held Piece
        hold.draw(g2);

        // Draw Next Piece Preview
        queue.draw(g2);
    }

    private void updateFallingTetromino() {
        nextTetromino.setParentXY(x, y);
        nextTetromino.setXY((Board.COLUMNS - nextTetromino.getWidth())/2, 0);

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
        for (int r = 0; r < ROWS + SPAWN_ROWS; r++) {
            boolean full = true;
            for (int c = 0; c < COLUMNS; c++) {
                if (!grid[r][c]) {
                    full = false;
                    break;
                }
            }
            if (full) {
                linesCleared++;
                // Shift down
                for (int y = r; y > 0; y--) {
                    System.arraycopy(grid[y - 1], 0, grid[y], 0, COLUMNS);
                    System.arraycopy(gridColor[y - 1], 0, gridColor[y], 0, COLUMNS);
                }
                // Clear top
                for (int c = 0; c < COLUMNS; c++) {
                    grid[0][c] = false;
                    gridColor[0][c] = null;
                }
            }
        }
        // NES Scoring: 40, 100, 300, 1200
        switch (linesCleared) {
            case 1 -> score += 40;
            case 2 -> score += 100;
            case 3 -> score += 300;
            case 4 -> score += 1200;
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