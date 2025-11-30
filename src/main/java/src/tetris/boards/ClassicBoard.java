package src.tetris.boards;

import src.tetris.physics.BoardPhysics;
import src.tetris.physics.LocalBoardPhysics;
import src.tetris.tetrominoes.TetrominoShadow;

import java.awt.*;
import java.util.Random;

import static src.tetris.Config.BOARD_COLUMNS;
import static src.tetris.Config.BOARD_ROWS;
import static src.tetris.Config.BOARD_SPAWN_ROWS;

public class ClassicBoard extends Board {
    // BOARD LOGIC
    private LocalBoardPhysics localBoardPhysics;
    // TETROMINO SHADOW
    private TetrominoShadow fallingTetrominoShadow;
    // GARBAGE SYSTEM
    private Random garbageRandom;

    public ClassicBoard(int x, int y, long seed) {
        super(x, y, seed);
        localBoardPhysics = (LocalBoardPhysics) boardPhysics;

        fallingTetrominoShadow = null;

        garbageRandom = new Random();
    }

    @Override
    protected BoardPhysics initializeBoardPhysics() { return new LocalBoardPhysics(grid); }

    @Override
    protected void setNextTetrominoAsFallingTetromino() {
        super.setNextTetrominoAsFallingTetromino();
        fallingTetrominoShadow = new TetrominoShadow(fallingTetromino);
    }

    @Override
    protected void updateGarbage() {
        int emptyColumn = garbageRandom.nextInt(BOARD_COLUMNS);

        for (int y = 0; y < BOARD_ROWS + BOARD_SPAWN_ROWS - garbageLinesToAdd; y++) {
            System.arraycopy(grid[y + garbageLinesToAdd], 0, grid[y], 0, BOARD_COLUMNS);
            System.arraycopy(gridColor[y + garbageLinesToAdd], 0, gridColor[y], 0, BOARD_COLUMNS);
        }
        for (int y = BOARD_ROWS + BOARD_SPAWN_ROWS - 1; y > BOARD_ROWS + BOARD_SPAWN_ROWS - garbageLinesToAdd - 1; y--) {
            for (int x = 0; x < BOARD_COLUMNS; x++) {
                if(x == emptyColumn) {
                    grid[y][x] = false;
                    gridColor[y][x] = null;
                } else {
                    grid[y][x] = true;
                    gridColor[y][x] = Color.GRAY;
                }
            }
        }

        if(localBoardPhysics.thereIsCollision()) {
            fallingTetromino.setY(0);
            localBoardPhysics.drop(fallingTetromino);
        }

        garbageLinesToAdd = 0;
    }

    @Override
    protected void updateFallingTetromino() {
        super.updateFallingTetromino();
        updateFallingTetrominoShadow();
    }

    @Override
    protected void drawFallingTetromino(Graphics2D g2) {
        fallingTetrominoShadow.draw(g2);
        super.drawFallingTetromino(g2);
    }

    // Controls
    public void moveLeftPressed() { localBoardPhysics.moveLeftPressed(); }
    public void moveLeftReleased() { localBoardPhysics.moveLeftReleased(); }
    public void moveRightPressed() { localBoardPhysics.moveRightPressed(); }
    public void moveRightReleased() { localBoardPhysics.moveRightReleased(); }
    public void softDropPressed() { localBoardPhysics.moveDownPressed(); }
    public void softDropReleased() { localBoardPhysics.moveDownReleased(); }
    public void hardDropPressed() { localBoardPhysics.dropPressed(); }
    public void hardDropReleased() { localBoardPhysics.dropReleased(); }
    public void rotateRightPressed() { localBoardPhysics.rotateRightPressed(); }
    public void rotateRightReleased() { localBoardPhysics.rotateRightReleased(); }
    public void rotateLeftPressed() { localBoardPhysics.rotateLeftPressed(); }
    public void rotateLeftReleased() { localBoardPhysics.rotateLeftReleased(); }
    public void flipPressed() { localBoardPhysics.flipPressed(); }
    public void flipReleased() { localBoardPhysics.flipReleased(); }

    private void updateFallingTetrominoShadow() {
        fallingTetrominoShadow.setXYRotationIndex(fallingTetromino.getX(), fallingTetromino.getY(), fallingTetromino.getRotationIndex());
        localBoardPhysics.drop(fallingTetrominoShadow);
    }
}
