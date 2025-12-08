package tetris.boards.nes;

import tetris.boards.ReceiverBoard;
import tetris.boards.components.BoardGrid;
import tetris.tetrominoes.generators.TetrominoesGeneratorType;

import static tetris.boards.nes.NESConfig.TETROMINOES_QUEUE_SIZE;

/**
 * A board that visualizes the state of a remote NES Tetris player.
 * <p>
 * This class is a lightweight receiver that mirrors the opponent's movements.
 * It uses the specific NES generator and queue size (1) to ensure the visual
 * representation of the "Next Piece" matches the opponent's logic.
 * </p>
 */
public class ReceiverNESBoard extends ReceiverBoard {
    /**
     * Constructs a receiver board for NES Tetris.
     *
     * @param x    The X screen coordinate.
     * @param y    The Y screen coordinate.
     * @param seed The random seed (synchronized with the sender).
     */
    public ReceiverNESBoard(int x, int y, long seed) {
        super(x, y, BoardGrid.ROWS, BoardGrid.SPAWN_ROWS, BoardGrid.COLUMNS, TETROMINOES_QUEUE_SIZE,
                TetrominoesGeneratorType.NES_TETROMINOES_GENERATOR, seed);
    }
}