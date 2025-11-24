package tetris;

import tetris.tetrominoes.Tetromino;

import java.awt.*;

public class Hold {
    public static final int WIDTH = GamePanel.CELL_SIZE * 5;
    public static final int HEIGHT = GamePanel.CELL_SIZE * (3 + 2);

    private boolean locked;
    private Tetromino heldTetromino;
    private Queue queue;
    private int x, y;

    public Hold(int x, int y, Queue queue) {
        this.x = x;
        this.y = y;
        this.queue = queue;
        locked = false;
        heldTetromino = null;
    }

    public Tetromino hold(Tetromino t) {
        if (locked) return null;

        Tetromino aux;

        locked = true;

        if (heldTetromino == null) {
            aux = queue.getNext();
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
        g2.drawString("Hold:", x + GamePanel.CELL_SIZE, y + GamePanel.CELL_SIZE);


        if (heldTetromino != null) {
            Color aux = heldTetromino.getColor();

            if(locked) heldTetromino.setColor(Color.GRAY);

            heldTetromino.draw(g2);
            heldTetromino.setColor(aux);
        }

        // Draw Border
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, WIDTH, HEIGHT);
    }
}
