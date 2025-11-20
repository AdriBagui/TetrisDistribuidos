package classictetris;

import classictetris.blocks.*;

import java.awt.*;

// ==========================================
// CLASS: Board
// Logic for a single player's 10x20 grid
// ==========================================
public class Board {
    private int[][] grid = new int[20][10]; // 20 rows, 10 cols
    private Block currentBlock;
    private Block nextBlock;
    private int score = 0;
    private boolean isAlive = true;
    private int playerID; // 1 or 2
    private MainFrame mainFrame;
    private int blockCounter = 0; // Which block index we are on
    private int linesCounter;
    private int skippedFrames = 0;
    private int level;
    private boolean fastMovingDown = false;

    public Board(int playerID, MainFrame frame, int level) {
        this.playerID = playerID;
        this.mainFrame = frame;
        this.level = level;
        this.linesCounter = (level-1) * 10;
        spawnBlock(); // Initialize first block
    }

    private void spawnBlock() {
        if (nextBlock == null) {
            // First spawn
            currentBlock = createBlock(mainFrame.getBlockTypeForIndex(blockCounter++));
            nextBlock = createBlock(mainFrame.getBlockTypeForIndex(blockCounter++));
        } else {
            currentBlock = nextBlock;
            nextBlock = createBlock(mainFrame.getBlockTypeForIndex(blockCounter++));
        }

        // Set Start Position
        currentBlock.setX(3);
        currentBlock.setY(0);

        // Game Over check immediately on spawn
        if (checkCollision(currentBlock.getX(), currentBlock.getY(), currentBlock.getShape())) {
            isAlive = false;
        }
    }

    public void update() {
        if (!isAlive) return;

        if (fastMovingDown) {
            if (skippedFrames < Math.max(20 - level, 3)/10) {
                skippedFrames++;
                return;
            }
        }
        else {
            if (skippedFrames < Math.max(20 - level, 3)) {
                skippedFrames++;
                return;
            }
        }

        if (!checkCollision(currentBlock.getX(), currentBlock.getY() + 1, currentBlock.getShape())) {
            currentBlock.moveDown();
        } else {
            lockBlock();
            clearLines();
            spawnBlock();
        }

        skippedFrames = 0;
    }

    // Controls
    public void moveLeft() {
        if (isAlive && !checkCollision(currentBlock.getX() - 1, currentBlock.getY(), currentBlock.getShape())) {
            currentBlock.moveLeft();
        }
    }

    public void moveRight() {
        if (isAlive && !checkCollision(currentBlock.getX() + 1, currentBlock.getY(), currentBlock.getShape())) {
            currentBlock.moveRight();
        }
    }

    public void startMoveDown() {
        fastMovingDown = true;
    }

    public void stopMoveDown() {
        fastMovingDown = false;
    }

    public void drop() {
        if (!isAlive) return;
        while(!checkCollision(currentBlock.getX(), currentBlock.getY() + 1, currentBlock.getShape())) {
            currentBlock.moveDown();
        }
        // Force update next tick will lock it
    }

    public void rotate() {
        if (!isAlive) return;
        int[][] rotated = currentBlock.getNextRotation();
        // Basic Wall kick logic (if rotation hits wall, try moving left/right)
        if (!checkCollision(currentBlock.getX(), currentBlock.getY(), rotated)) {
            currentBlock.rotate();
        } else if (!checkCollision(currentBlock.getX() - 1, currentBlock.getY(), rotated)) {
            currentBlock.moveLeft();
            currentBlock.rotate();
        } else if (!checkCollision(currentBlock.getX() + 1, currentBlock.getY(), rotated)) {
            currentBlock.moveRight();
            currentBlock.rotate();
        }
    }

    private void lockBlock() {
        for (Point p : currentBlock.getPoints()) {
            int x = currentBlock.getX() + p.x;
            int y = currentBlock.getY() + p.y;
            if (y >= 0 && y < 20 && x >= 0 && x < 10) {
                grid[y][x] = currentBlock.getColorIndex();
            }
        }
    }

    private void clearLines() {
        int linesCleared = 0;
        for (int r = 0; r < 20; r++) {
            boolean full = true;
            for (int c = 0; c < 10; c++) {
                if (grid[r][c] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                linesCleared++;
                // Shift down
                for (int y = r; y > 0; y--) {
                    System.arraycopy(grid[y - 1], 0, grid[y], 0, 10);
                }
                // Clear top
                for (int c = 0; c < 10; c++) grid[0][c] = 0;
            }
        }
        // NES Scoring: 40, 100, 300, 1200
        switch (linesCleared) {
            case 1 -> score += 40;
            case 2 -> score += 100;
            case 3 -> score += 300;
            case 4 -> score += 1200;
        }

        linesCounter += linesCleared;
        level = linesCounter / 10 + 1;
    }

    private boolean checkCollision(int x, int y, int[][] shape) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] != 0) {
                    int boardX = x + c;
                    int boardY = y + r;

                    // Check boundaries
                    if (boardX < 0 || boardX >= 10 || boardY >= 20) return true;

                    // Check grid occupied (ignore above board)
                    if (boardY >= 0 && grid[boardY][boardX] != 0) return true;
                }
            }
        }
        return false;
    }

    public int[][] getGrid() { return grid; }
    public int getLevel() { return level; }
    public int getScore() { return score; }
    public Block getCurrentBlock() { return currentBlock; }
    public Block getNextBlock() { return nextBlock; }
    public boolean isAlive() { return isAlive; }

    // Factory for blocks
    private Block createBlock(int type) {
        return switch (type) {
            case 0 -> new LineBlock();
            case 1 -> new JBlock();
            case 2 -> new LBlock();
            case 3 -> new SquareBlock();
            case 4 -> new SBlock();
            case 5 -> new TBlock();
            case 6 -> new ZBlock();
            default -> new SquareBlock();
        };
    }
}