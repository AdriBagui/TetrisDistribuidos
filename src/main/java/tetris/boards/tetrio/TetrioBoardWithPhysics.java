package tetris.boards.tetrio;

import tetris.boards.BoardWithPhysics;
import tetris.boards.components.BoardGrid;
import tetris.boards.components.TetrominoHolder;
import tetris.boards.physics.rotationSystems.RotationSystemType;
import tetris.boards.physics.ReceivedGarbageHandler;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import java.awt.*;
import java.util.Random;

import static main.MainPanel.CELL_SIZE;
import static main.MainPanel.TETROMINO_HOLDER_WIDTH;
import static tetris.boards.tetrio.TetrioConfig.*;

public class TetrioBoardWithPhysics extends BoardWithPhysics {
    // TETROMINO HOLDER
    protected final TetrominoHolder tetrominoHolder;
    // GARBAGE SYSTEM
    protected Random garbageRandom;
    protected ReceivedGarbageHandler receivedGarbageHandler;

    public TetrioBoardWithPhysics(int x, int y, long seed) {
        super(x + TETROMINO_HOLDER_WIDTH, y, BoardGrid.ROWS, BoardGrid.SPAWN_ROWS, BoardGrid.COLUMNS, TETROMINOES_QUEUE_SIZE,
                TetrominoesGeneratorType.RANDOM_BAG_7_TETROMINOES_GENERATOR, seed, RotationSystemType.SUPER_ROTATION_SYSTEM,
                INITIAL_GRAVITY, LOCK_DELAY_FRAMES);

        tetrominoHolder = new TetrominoHolder(x, y + BoardGrid.SPAWN_ROWS *CELL_SIZE, tetrominoesQueue);
        this.x = x; // Restore the correct x (when calling super I add the width of the holder so that the rest of the components
                    // are drown further right and there is space for the holder, however that implies that the board x is
                    // incorrect so it has to be overwritten)

        garbageRandom = new Random();
        receivedGarbageHandler = new ReceivedGarbageHandler();
    }

    public void hold() {
        nextTetromino = tetrominoHolder.hold(fallingTetromino);
    }

    public void addGarbage(byte numberOfGarbageRows, byte emptyGarbageColumn) {
        receivedGarbageHandler.addGarbage(numberOfGarbageRows, emptyGarbageColumn);
    }

    @Override
    public void update() {
        super.update();

        if (receivedGarbageHandler.updateGarbage(grid, fallingTetromino, isFallingTetrominoLocked()))
            isAlive = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        // Draw Held Piece
        tetrominoHolder.draw(g2);
    }

    @Override
    protected void increaseLevel() {
        super.increaseLevel();

        if (level <= MAX_GRAVITY_LEVEL) gravity.increaseGravity(GRAVITY_INCREMENT_PER_LEVEL);
    }

    @Override
    protected int lockFallingTetrominoAndClearLines() {
        int clearedLines = super.lockFallingTetrominoAndClearLines();

        // Unlock tetrominoHolder
        tetrominoHolder.unlock();

        return clearedLines;
    }
}