package tetris;

import tetris.generators.TetrominoFactory;
import tetris.generators.TetrominoesGenerator;
import tetris.tetrominoes.Tetromino;

import java.awt.*;

public class Queue {
    private Tetromino[] tetrominoesQueue;
    private TetrominoesGenerator tetrominoesGenerator;
    private int x, y;

    public Queue(int queueSize, TetrominoesGenerator tetrominoesGenerator, int x, int y) {
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
        int queueWidth = TwoPlayerPanel.CELL_SIZE * 5;
        int queueHeight = TwoPlayerPanel.CELL_SIZE * (tetrominoesQueue.length * 3 + 2);

        g2.drawString("Next:", x + TwoPlayerPanel.CELL_SIZE, y + TwoPlayerPanel.CELL_SIZE);

        for (int i = 0; i < tetrominoesQueue.length; i++) {
            tetrominoesQueue[i].draw(g2);
        }

        // Draw Border
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, queueWidth, queueHeight);
    }

    private void setTetrominoPosition(Tetromino t, int i) {
        double offX = 0.5 * (5-t.getApparentWidth());
        double offY = 2 - 0.5 * (2-t.getApparentHeight());

        t.setXY(offX, offY + 3*i);
        tetrominoesQueue[i] = t;
    }
}
