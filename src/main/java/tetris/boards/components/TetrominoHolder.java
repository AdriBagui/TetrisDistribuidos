package tetris.boards.components;

import tetris.tetrominoes.Tetromino;

import java.awt.*;

import static tetris.Config.*;

public class TetrominoHolder {
    private boolean locked;
    private Tetromino heldTetromino;
    private TetrominoesQueue tetrominoesQueue;
    private int x, y;

    /**
     * Creates the component that allows the player to hold/swap a tetromino.
     * @param x the x coordinate of the holder on the screen.
     * @param y the y coordinate of the holder on the screen.
     * @param tetrominoesQueue the queue from which to pull a new piece if the holder is empty.
     */
    public TetrominoHolder(int x, int y, TetrominoesQueue tetrominoesQueue) {
        this.x = x;
        this.y = y;
        this.tetrominoesQueue = tetrominoesQueue;
        locked = false;
        heldTetromino = null;
    }

    /**
     * Attempts to swap the currently falling tetromino with the held tetromino.
     * If no piece is held, the current piece is stored and the next one is pulled from the queue.
     * This action locks the holder until the new piece is placed.
     * @param t the currently falling tetromino to put into hold.
     * @return the tetromino that was previously held, or a new one from the queue if the hold was empty. Returns null if the holder is locked.
     */
    public Tetromino hold(Tetromino t) {
        if (locked) return null;

        Tetromino aux;

        locked = true;

        if (heldTetromino == null) {
            aux = tetrominoesQueue.getNext();
        } else {
            aux = heldTetromino;
        }

        heldTetromino = t;
        heldTetromino.setXY(0.5 * (5-t.getWidth()), 2);
        heldTetromino.setParentXY(x, y);
        heldTetromino.setRotationIndex(0);

        return aux;
    }

    /**
     * Unlocks the holder, allowing the player to swap again.
     * Usually called when a piece locks onto the board.
     */
    public void unlock() { locked = false; }

    /**
     * Checks if the holder is currently used in this turn.
     * @return true if the player has already swapped in this turn.
     */
    public boolean isLocked() { return locked; }

    /**
     * Renders the holder box and the held piece.
     * If locked, the held piece is drawn in gray.
     * @param g2 the Graphics2D context.
     */
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(220, 220 , 220));
        g2.drawString("Hold:", x + CELL_SIZE, y + CELL_SIZE);


        if (heldTetromino != null) {
            Color aux = heldTetromino.getColor();

            if(locked) heldTetromino.setColor(Color.GRAY);

            heldTetromino.draw(g2);
            heldTetromino.setColor(aux);
        }

        // Draw Border
        g2.setColor(new Color(110, 110 , 110));
        g2.drawRect(x, y, TETROMINO_HOLDER_WIDTH, TETROMINO_HOLDER_HEIGHT);
    }
}