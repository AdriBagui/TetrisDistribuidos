package tetris.boards.modernTetris;

import tetris.boards.ReceiverBoard;
import tetris.boards.components.BoardGrid;
import tetris.boards.components.TetrominoHolder;
import tetris.boards.physics.ReceivedGarbageHandler;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import java.awt.*;

import static client.userInterface.panels.MainPanel.CELL_SIZE;
import static client.userInterface.panels.MainPanel.TETROMINO_HOLDER_WIDTH;
import static tetris.boards.modernTetris.ModernTetrisConfig.TETROMINOES_QUEUE_SIZE;

public class ReceiverModernTetrisBoard extends ReceiverBoard {
    // TETROMINO HOLDER
    protected final TetrominoHolder tetrominoHolder;
    // GARBAGE SYSTEM
    protected ReceivedGarbageHandler receivedGarbageHandler;

    public ReceiverModernTetrisBoard(int x, int y, long seed) {
        super(x + TETROMINO_HOLDER_WIDTH, y, BoardGrid.ROWS, BoardGrid.SPAWN_ROWS, BoardGrid.COLUMNS, TETROMINOES_QUEUE_SIZE,
                TetrominoesGeneratorType.RANDOM_BAG_7_TETROMINOES_GENERATOR, seed);

        tetrominoHolder = new TetrominoHolder(x, y + BoardGrid.SPAWN_ROWS*CELL_SIZE, tetrominoesQueue);
        this.x = x; // Restore the correct x (when calling super I add the width of the holder so that the rest of the components
                    // are drown further right and there is space for the holder, however that implies that the board x is
                    // incorrect so it has to be overwritten)

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
    protected int lockFallingTetrominoAndClearLines() {
        int clearedLines = super.lockFallingTetrominoAndClearLines();

        // Unlock tetrominoHolder
        tetrominoHolder.unlock();

        return clearedLines;
    }
}
