package tetris.boards.components;

import tetris.tetrominoes.generators.TetrominoesGenerator;
import tetris.tetrominoes.Tetromino;

import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

/**
 * A UI component that manages and displays the queue of upcoming Tetrominoes.
 * <p>
 * This class ensures that the player can see what pieces are coming next, which is essential
 * for strategic play. It uses a {@link TetrominoesGenerator} to ensure the sequence
 * adheres to the specific game mode rules (e.g., pure random vs. 7-bag).
 * </p>
 */
public class TetrominoesQueue {
    private final Tetromino[] tetrominoesQueue;
    private final TetrominoesGenerator tetrominoesGenerator;
    private final int x;
    private final int y;

    /**
     * Initializes the queue and pre-populates it.
     *
     * @param queueSize            How many pieces to show in the preview list.
     * @param tetrominoesGenerator The strategy for creating new pieces.
     * @param x                    X coordinate on the screen.
     * @param y                    Y coordinate on the screen.
     */
    public TetrominoesQueue(int queueSize, TetrominoesGenerator tetrominoesGenerator, int x, int y) {
        this.tetrominoesQueue = new Tetromino[queueSize];
        this.tetrominoesGenerator = tetrominoesGenerator;
        this.x = x;
        this.y = y;

        // Fill the initial queue
        for(int i = 0; i < queueSize; i++) {
            setTetrominoPosition(tetrominoesGenerator.getNext(x, y), i);
        }
    }

    /**
     * Pops the first tetromino from the queue and generates a new one at the end.
     * This shifts all remaining pieces up in the visual list.
     *
     * @return The next {@link Tetromino} ready to enter the board.
     */
    public Tetromino getNext() {
        Tetromino next = tetrominoesQueue[0];

        // Shift elements up
        for (int i = 0; i < tetrominoesQueue.length - 1; i++) {
            setTetrominoPosition(tetrominoesQueue[i+1], i);
        }

        // Add new element at the bottom
        setTetrominoPosition(tetrominoesGenerator.getNext(x, y), tetrominoesQueue.length - 1);

        return next;
    }

    /**
     * Renders the "Next" box and the contained pieces.
     *
     * @param g2 The graphics context.
     */
    public void draw(Graphics2D g2) {
        int queueHeight = CELL_SIZE * (tetrominoesQueue.length * 3 + 2);

        // Header Text
        g2.setColor(TEXT_COLOR);
        g2.setFont(DEFAULT_FONT);
        g2.drawString("Next:", x + CELL_SIZE, y + CELL_SIZE);

        // Pieces
        for (Tetromino tetromino : tetrominoesQueue) {
            tetromino.draw(g2);
        }

        // Border
        g2.setColor(new Color(110, 110 , 110));
        g2.drawRect(x, y, TETROMINOES_QUEUE_WIDTH, queueHeight);
    }

    /**
     * Centers a tetromino visually within its slot in the queue.
     *
     * @param t The tetromino to position.
     * @param i The index in the queue (0 is top).
     */
    private void setTetrominoPosition(Tetromino t, int i) {
        // Calculate offset to center the piece in a 5-block wide container
        double offX = 0.5 * (5 - t.getApparentWidth());
        // Calculate Y offset based on index (each slot is approx 3 cells high)
        double offY = 2 - 0.5 * (2 - t.getApparentHeight());

        t.setXY(offX, offY + 3 * i);
        tetrominoesQueue[i] = t;
    }
}