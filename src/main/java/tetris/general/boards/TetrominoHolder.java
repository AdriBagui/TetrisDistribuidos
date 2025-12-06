package tetris.general.boards;

import tetris.general.tetrominoes.Tetromino;

import java.awt.*;

import static tetris.Config.*;

public class TetrominoHolder {
    private boolean locked;
    private Tetromino heldTetromino;
    private TetrominoesQueue tetrominoesQueue;
    private int x, y;

    public TetrominoHolder(int x, int y, TetrominoesQueue tetrominoesQueue) {
        this.x = x;
        this.y = y;
        this.tetrominoesQueue = tetrominoesQueue;
        locked = false;
        heldTetromino = null;
    }

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

    public void unlockHold() { locked = false; }

    public boolean isLocked() { return locked; }

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
