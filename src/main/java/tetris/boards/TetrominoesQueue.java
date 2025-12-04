package tetris.boards;

import tetris.tetrominoes.generators.TetrominoesGenerator;
import tetris.tetrominoes.Tetromino;

import java.awt.*;

import static tetris.Config.CELL_SIZE;
import static tetris.Config.TETROMINOES_QUEUE_WIDTH;

public class TetrominoesQueue {
    private Tetromino[] tetrominoesQueue;
    private TetrominoesGenerator tetrominoesGenerator;
    private int x, y;

    public TetrominoesQueue(int queueSize, TetrominoesGenerator tetrominoesGenerator, int x, int y) {
        this.tetrominoesQueue = new Tetromino[queueSize];
        this.tetrominoesGenerator = tetrominoesGenerator;
        this.x = x;
        this.y = y;

        for(int i = 0; i < queueSize; i++) {
            setTetrominoPosition(tetrominoesGenerator.getNext(x, y), i);
        }
    }

    public Tetromino getNext() {
        Tetromino next = tetrominoesQueue[0];
        for (int i = 0; i < tetrominoesQueue.length - 1; i++) {
            setTetrominoPosition(tetrominoesQueue[i+1], i);
        }
        setTetrominoPosition(tetrominoesGenerator.getNext(x, y), tetrominoesQueue.length - 1);

        return next;
    }

    public void draw(Graphics2D g2) {
        int queueHeight = CELL_SIZE * (tetrominoesQueue.length * 3 + 2);

        g2.drawString("Next:", x + CELL_SIZE, y + CELL_SIZE);

        for (int i = 0; i < tetrominoesQueue.length; i++) {
            tetrominoesQueue[i].draw(g2);
        }

        // Draw Border
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, TETROMINOES_QUEUE_WIDTH, queueHeight);
    }

    private void setTetrominoPosition(Tetromino t, int i) {
        double offX = 0.5 * (5-t.getApparentWidth());
        double offY = 2 - 0.5 * (2-t.getApparentHeight());

        t.setXY(offX, offY + 3*i);
        tetrominoesQueue[i] = t;
    }
}
