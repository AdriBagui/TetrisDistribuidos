package tetris.boards.components;

import tetris.tetrominoes.Tetromino;

import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

/**
 * A UI component that manages the "Hold" mechanic.
 * <p>
 * This allows a player to store one active piece for later use. The logic enforces
 * that a swap can only occur once per turn (until a piece is locked).
 * </p>
 */
public class TetrominoHolder {
    /** Indicates if the player has already used the hold function this turn. */
    private boolean locked;
    private Tetromino heldTetromino;
    private final TetrominoesQueue tetrominoesQueue;
    private final int x;
    private final int y;

    /**
     * Creates the Hold box.
     *
     * @param x                 X coordinate.
     * @param y                 Y coordinate.
     * @param tetrominoesQueue  Reference to the queue (needed if holding when empty).
     */
    public TetrominoHolder(int x, int y, TetrominoesQueue tetrominoesQueue) {
        this.x = x;
        this.y = y;
        this.tetrominoesQueue = tetrominoesQueue;
        locked = false;
        heldTetromino = null;
    }

    /**
     * Swaps the current falling piece with the held piece.
     *
     * @param t The tetromino currently falling on the board.
     * @return The piece that was previously held (to become the new falling piece).
     * If the holder was empty, it returns the next piece from the queue.
     * Returns {@code null} if the holder is currently locked.
     */
    public Tetromino hold(Tetromino t) {
        if (locked) return null;

        Tetromino aux;
        locked = true;

        if (heldTetromino == null) {
            // First hold of the game: pull from queue
            aux = tetrominoesQueue.getNext();
        } else {
            // Swap: return the held piece
            aux = heldTetromino;
        }

        // Store the new piece
        heldTetromino = t;
        // Reset position and rotation for display
        heldTetromino.setXY(0.5 * (5 - t.getWidth()), 2);
        heldTetromino.setParentXY(x, y);
        heldTetromino.setRotationIndex(0);

        return aux;
    }

    /**
     * Resets the lock state. Should be called when a piece locks onto the board.
     */
    public void unlock() { locked = false; }

    /**
     * Checks if a hold action is permitted.
     *
     * @return {@code true} if locked, {@code false} if available.
     */
    public boolean isLocked() { return locked; }

    /**
     * Renders the Hold box and the held piece.
     *
     * @param g2 The graphics context.
     */
    public void draw(Graphics2D g2) {
        // Label
        g2.setColor(TEXT_COLOR);
        g2.setFont(DEFAULT_FONT);
        g2.drawString("Hold:", x + CELL_SIZE, y + CELL_SIZE);

        // Piece
        if (heldTetromino != null) {
            Color aux = heldTetromino.getColor();

            // Gray out the piece if it can't be used this turn
            if (isLocked()) heldTetromino.setColor(Color.GRAY);

            heldTetromino.draw(g2);

            // Restore color
            heldTetromino.setColor(aux);
        }

        // Border
        g2.setColor(new Color(110, 110 , 110));
        g2.drawRect(x, y, TETROMINO_HOLDER_WIDTH, TETROMINO_HOLDER_HEIGHT);
    }
}