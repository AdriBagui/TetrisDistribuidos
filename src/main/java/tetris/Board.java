package tetris;

import tetris.generators.RandomBagTetrominoesGenerator;
import tetris.movement.CollisionDetector;
import tetris.movement.srs.SuperRotationSystem;
import tetris.movement.srs.WallKick;
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

    private static final int INPUT_SKIPPED_FRAMES = 2;
    private static final int INPUT_DELAY = 10;

    private int playerID;
    private int x, y;
    private boolean[][] grid;// 20 rows, 10 cols
    private Color[][] gridColor;
    private Tetromino currentTetromino;
    private Tetromino nextTetromino;
    private Queue queue;
    private Hold hold;
    private CollisionDetector collisionDetector;
    private SuperRotationSystem superRotationSystem;
    private int score, linesCleared, level;
    private boolean isAlive;
    private int skippedFrames;
    private int fastMovingDown;
    private int movingLeft;
    private int movingRight;
    private boolean rotatedRight;
    private boolean rotatedLeft;
    private boolean flipped;

    public Board(int playerID, long seed, int x, int y) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        grid = new boolean[ROWS + SPAWN_ROWS][COLUMNS];
        gridColor = new Color[ROWS + SPAWN_ROWS][COLUMNS];
        queue = new Queue(5, new RandomBagTetrominoesGenerator(seed), x + COLUMNS * TwoPlayerPanel.CELL_SIZE, y + SPAWN_ROWS * TwoPlayerPanel.CELL_SIZE);
        hold = new Hold(x - Hold.WIDTH, y + SPAWN_ROWS * TwoPlayerPanel.CELL_SIZE, queue);
        collisionDetector = new CollisionDetector(grid);
        superRotationSystem = new WallKick(collisionDetector);
        score = 0;
        linesCleared = 0;
        level = 1;
        isAlive = true;
        skippedFrames = 0;
        fastMovingDown = -1;
        movingLeft = -1;
        movingRight = -1;
        rotatedRight = false;
        rotatedLeft = false;
        flipped = false;

        currentTetromino = getQueuedTetromino(); // Initialize first block
    }

    private Tetromino getQueuedTetromino() {
        Tetromino t = queue.getNext();
        t.setParentXY(x, y);
        t.setXY((Board.COLUMNS - t.getWidth())/2, 0);

        if (collisionDetector.checkCollision(t)) {
            isAlive = false;
        }

        return  t;
    }

    public synchronized void hold() {
        nextTetromino = hold.hold(currentTetromino);

        if(nextTetromino == null) return;

        nextTetromino.setParentXY(x, y);
        nextTetromino.setXY((Board.COLUMNS - nextTetromino.getWidth())/2, 0);
    }

    public synchronized void update() {
        if (nextTetromino != null) {
            currentTetromino = nextTetromino;
            currentTetromino.setXY(1, 0);
            nextTetromino = null;

            if (collisionDetector.checkCollision(currentTetromino)) {
                isAlive = false;
            }
        }

        if (!isAlive) return;

        if (movingLeft <= 0 || movingRight <= 0) {
            if (movingLeft >= 0) {
                if (movingLeft < INPUT_SKIPPED_FRAMES + INPUT_DELAY) {
                    movingLeft++;
                } else {
                    moveLeft();
                    movingLeft -= INPUT_SKIPPED_FRAMES;
                }
            }

            if (movingRight >= 0) {
                if (movingRight < INPUT_SKIPPED_FRAMES + INPUT_DELAY) {
                    movingRight++;
                } else {
                    moveRight();
                    movingRight -= INPUT_SKIPPED_FRAMES;
                }
            }
        } else {
            movingRight = INPUT_DELAY/2;
            movingLeft = INPUT_DELAY/2;
        }

        if (fastMovingDown >= 0) {
            if (fastMovingDown < INPUT_SKIPPED_FRAMES + INPUT_DELAY) {
                fastMovingDown++;
            } else {
                moveDown();
                fastMovingDown -= INPUT_SKIPPED_FRAMES;
            }
        }

        if (skippedFrames < Math.max(20 - level, 3)) {
            skippedFrames++;
            return;
        }

        currentTetromino.moveDown();

        if (collisionDetector.checkCollision(currentTetromino)) {
            currentTetromino.moveUp();
            lockTetromino();
            clearLines();
            currentTetromino = getQueuedTetromino();
        }

        skippedFrames = 0;
    }

    // Controls
    private void moveLeft() {
        if (movingRight >= 0) return;

        currentTetromino.moveLeft();

        if (collisionDetector.checkCollision(currentTetromino)) {
            currentTetromino.moveRight();
        }
    }

    public void moveLeftPressed() {
        if (movingLeft == -1) {
            moveLeft();
            movingLeft = 0;
        }
    }

    public void moveLeftReleased() {
        movingLeft = -1;
    }

    private void moveRight() {
        if (movingLeft >= 0) return;

        currentTetromino.moveRight();

        if (collisionDetector.checkCollision(currentTetromino)) {
            currentTetromino.moveLeft();
        }
    }

    public void moveRightPressed() {
        if (movingRight == -1) {
            moveRight();
            movingRight = 0;
        }
    }

    public void moveRightReleased() {
        movingRight = -1;
    }

    private void moveDown() {
        currentTetromino.moveDown();

        if (collisionDetector.checkCollision(currentTetromino)) {
            currentTetromino.moveUp();
        }
    }

    public void moveDownPressed() {
        if (fastMovingDown == -1) {
            moveDown();
            fastMovingDown = 0;
        }
    }

    public void moveDownReleased() {
        fastMovingDown = -1;
    }

    public void rotateRightPressed() {
        if (rotatedRight) return;

        superRotationSystem.rotateRight(currentTetromino);
        rotatedRight = true;
    }

    public void rotateRightReleased() {
        rotatedRight = false;
    }

    private void drop(Tetromino t) {
        while(!collisionDetector.checkCollision(t)) {
            t.moveDown();
        }

        t.moveUp();
    }

    private void lockTetromino() {
        boolean[][] tetrominoShape = currentTetromino.getShape();
        int x = currentTetromino.getX();
        int y = currentTetromino.getY();

        for (int r = 0; r < tetrominoShape.length; r++) {
            for (int c = 0; c < tetrominoShape[0].length; c++) {
                if(tetrominoShape[r][c]) {
                    grid[y+r][x+c] = true;
                    gridColor[y+r][x+c] = currentTetromino.getColor();
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
        level = (this.linesCleared / 10) + 1;
    }

    public int getLevel() { return level; }
    public int getScore() { return score; }
    public Tetromino getCurrentBlock() { return currentTetromino; }
    public boolean isAlive() { return isAlive; }

    public void draw(Graphics2D g2) {
        int boardWidth = COLUMNS * TwoPlayerPanel.CELL_SIZE;
        int boardHeight = ROWS * TwoPlayerPanel.CELL_SIZE;
        int spawnHeight = SPAWN_ROWS * TwoPlayerPanel.CELL_SIZE;
        int boardAndSpawnHeight = boardHeight + spawnHeight;

        // Draw Grid Background
        g2.setColor(Color.BLACK);
        g2.fillRect(x, y + spawnHeight, boardWidth, boardHeight);

        // Draw Static Blocks
        for (int r = 0; r < ROWS + SPAWN_ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (grid[r][c]) {
                    TetrominoCell.draw(g2, x + c * TwoPlayerPanel.CELL_SIZE, y + r * TwoPlayerPanel.CELL_SIZE, gridColor[r][c]);
                }
            }
        }

        // Draw Current Falling Tetromino
        currentTetromino.draw(g2);

        // Draw Current Falling Tetromino Shadow
        if (isAlive) {
            TetrominoShadow tetrominoShadow = new TetrominoShadow(currentTetromino);
            drop(tetrominoShadow);
            tetrominoShadow.draw(g2);
        }

        // Draw Grid Lines
        g2.setColor(new Color(255, 255, 255, 25));
        for (int r = TwoPlayerPanel.CELL_SIZE; r < boardHeight; r += TwoPlayerPanel.CELL_SIZE) {
            g2.drawLine(x, y + spawnHeight + r, x + boardWidth, y + spawnHeight + r);
        }
        for (int c = TwoPlayerPanel.CELL_SIZE; c < boardWidth; c += TwoPlayerPanel.CELL_SIZE) {
            g2.drawLine(x + c, y + spawnHeight, x + c, y + spawnHeight + boardHeight);
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
}