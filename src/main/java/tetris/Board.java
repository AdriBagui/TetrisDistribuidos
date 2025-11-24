package tetris;

import tetris.generators.RandomBagTetrominoesGenerator;
import tetris.movement.CollisionDetector;
import tetris.movement.Gravity;
import tetris.movement.InputMovement;
import tetris.movement.srs.SuperRotationSystem;
import tetris.movement.srs.SuperRotationSystemPlus;
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
    private Gravity gravity;
    private InputMovement inputMovement;
    private int score, linesCleared, level;
    private boolean isAlive;
    private int skippedFrames;

    public Board(int playerID, long seed, int x, int y) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;

        grid = new boolean[ROWS + SPAWN_ROWS][COLUMNS];
        gridColor = new Color[ROWS + SPAWN_ROWS][COLUMNS];
        queue = new Queue(5, new RandomBagTetrominoesGenerator(seed), x + COLUMNS * GamePanel.CELL_SIZE, y + SPAWN_ROWS * GamePanel.CELL_SIZE);
        hold = new Hold(x - Hold.WIDTH, y + SPAWN_ROWS * GamePanel.CELL_SIZE, queue);
        collisionDetector = new CollisionDetector(grid);
        superRotationSystem = new SuperRotationSystemPlus(collisionDetector);
        gravity = new Gravity(grid, collisionDetector, 0.05);
        inputMovement = new InputMovement(collisionDetector, superRotationSystem);
        score = 0;
        linesCleared = 0;
        level = 1;
        isAlive = true;
        skippedFrames = 0;

        setCurrentTetromino(getQueuedTetromino());  // Initialize first block

        for(int i = 1; i < level && i < 21; i++) {
            gravity.increaseGravity();
        }
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

    private void setCurrentTetromino(Tetromino t) {
        currentTetromino = t;
        gravity.setFallingTetromino(currentTetromino);
        inputMovement.setFallingTetrimonio(currentTetromino);
    }

    public synchronized void hold() {
        nextTetromino = hold.hold(currentTetromino);

        if(nextTetromino == null) return;

        nextTetromino.setParentXY(x, y);
        nextTetromino.setXY((Board.COLUMNS - nextTetromino.getWidth())/2, 0);
    }

    public synchronized void update() {
        if (nextTetromino != null) {
            setCurrentTetromino(nextTetromino);
            nextTetromino = null;

            if (collisionDetector.checkCollision(currentTetromino)) {
                isAlive = false;
            }
        }

        if (!isAlive) return;

        inputMovement.update();

        gravity.update();

        if (gravity.locked()) {
            lockTetromino();
            clearLines();
            setCurrentTetromino(getQueuedTetromino());
        }

        skippedFrames = 0;
    }

    // Controls
    public void moveLeftPressed() { inputMovement.moveLeftPressed(); }
    public void moveLeftReleased() { inputMovement.moveLeftReleased(); }
    public void moveRightPressed() { inputMovement.moveRightPressed(); }
    public void moveRightReleased() { inputMovement.moveRightReleased(); }
    public void moveDownPressed() { inputMovement.moveDownPressed(); }
    public void moveDownReleased() { inputMovement.moveDownReleased(); }
    public void rotateRightPressed() { inputMovement.rotateRightPressed(); }
    public void rotateRightReleased() { inputMovement.rotateRightReleased(); }
    public void rotateLeftPressed() { inputMovement.rotateLeftPressed(); }
    public void rotateLeftReleased() { inputMovement.rotateLeftReleased(); }
    public void flipPressed() { inputMovement.flipPressed(); }
    public void flipReleased() { inputMovement.flipReleased(); }

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

        if (level < (this.linesCleared / 10) + 1 && level < 21) {
            gravity.increaseGravity();
        }

        level = (this.linesCleared / 10) + 1;
    }

    public int getLevel() { return level; }
    public int getScore() { return score; }
    public Tetromino getCurrentBlock() { return currentTetromino; }
    public boolean isAlive() { return isAlive; }

    public void draw(Graphics2D g2) {
        int boardWidth = COLUMNS * GamePanel.CELL_SIZE;
        int boardHeight = ROWS * GamePanel.CELL_SIZE;
        int spawnHeight = SPAWN_ROWS * GamePanel.CELL_SIZE;
        int boardAndSpawnHeight = boardHeight + spawnHeight;

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
        currentTetromino.draw(g2);

        // Draw Current Falling Tetromino Shadow
        if (isAlive) {
            TetrominoShadow tetrominoShadow = new TetrominoShadow(currentTetromino);
            drop(tetrominoShadow);
            tetrominoShadow.draw(g2);
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