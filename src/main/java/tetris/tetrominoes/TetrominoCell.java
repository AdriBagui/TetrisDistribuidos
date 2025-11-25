package tetris.tetrominoes;

import tetris.GamePanel;

import java.awt.*;

public class TetrominoCell {
    public static void draw(Graphics2D g2, int x, int y, Color c) {
        // Draw Cell Fill
        g2.setColor(c);
        g2.fillRect(x, y, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE);

        // Drain Internal Square Border
        int padding = 5;
        g2.setColor(c.darker());
        g2.fillRect(x + padding, y + padding, GamePanel.CELL_SIZE - 2*padding + 1, GamePanel.CELL_SIZE - 2*padding + 1);

        // Draw Cell Border
        g2.setColor(c.darker());
        g2.drawRect(x, y, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE);
    }
}
