package tetris.boards.components;

import tetris.tetrominoes.generators.TetrominoesGenerator;
import tetris.tetrominoes.Tetromino;

import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

public class TetrominoesQueue {
    private final Tetromino[] tetrominoesQueue;
    private final TetrominoesGenerator tetrominoesGenerator;
    private final int x;
    private final int y;

    /**
     * Creates the queue component that displays upcoming tetrominoes.
     * @param queueSize the number of upcoming tetrominoes to display.
     * @param tetrominoesGenerator the generator strategy (e.g., Random or 7-Bag).
     * @param x the x coordinate of the queue on the screen.
     * @param y the y coordinate of the queue on the screen.
     */
    public TetrominoesQueue(int queueSize, TetrominoesGenerator tetrominoesGenerator, int x, int y) {
        this.tetrominoesQueue = new Tetromino[queueSize];
        this.tetrominoesGenerator = tetrominoesGenerator;
        this.x = x;
        this.y = y;

        for(int i = 0; i < queueSize; i++) {
            setTetrominoPosition(tetrominoesGenerator.getNext(x, y), i);
        }
    }

    /**
     * Retrieves the next tetromino from the queue and shifts the remaining ones up.
     * Also generates a new tetromino at the end of the queue.
     * @return the next {@link Tetromino} to play.
     */
    public Tetromino getNext() {
        Tetromino next = tetrominoesQueue[0];
        for (int i = 0; i < tetrominoesQueue.length - 1; i++) {
            setTetrominoPosition(tetrominoesQueue[i+1], i);
        }
        setTetrominoPosition(tetrominoesGenerator.getNext(x, y), tetrominoesQueue.length - 1);

        return next;
    }

    /**
     * Renders the queue and the surrounding border.
     * @param g2 the Graphics2D context.
     */
    public void draw(Graphics2D g2) {
        int queueHeight = CELL_SIZE * (tetrominoesQueue.length * 3 + 2);

        g2.setColor(TEXT_COLOR);
        g2.setFont(DEFAULT_FONT);
        g2.drawString("Next:", x + CELL_SIZE, y + CELL_SIZE);

        for (Tetromino tetromino : tetrominoesQueue) {
            tetromino.draw(g2);
        }

        // Draw Border
        g2.setColor(new Color(110, 110 , 110));
        g2.drawRect(x, y, TETROMINOES_QUEUE_WIDTH, queueHeight);
    }

    /**
     * Helper method to calculate and set the visual position of a tetromino in the list.
     * Centers the piece horizontally within the queue box.
     * @param t the tetromino to position.
     * @param i the index in the queue (0 is the top).
     */
    private void setTetrominoPosition(Tetromino t, int i) {
        double offX = 0.5 * (5-t.getApparentWidth());
        double offY = 2 - 0.5 * (2-t.getApparentHeight());

        t.setXY(offX, offY + 3*i);
        tetrominoesQueue[i] = t;
    }
}