package tetris.tetrominoes;

import tetris.GamePanel;

import java.awt.*;

public class TetrominoCell {
    public static void draw(Graphics2D g2, int x, int y, Color c) {
        // Draw Cell Fill
        g2.setColor(c);
        g2.fillRect(x, y, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE);
        // Draw Cell Border
        g2.setColor(c.darker());
        g2.drawRect(x, y, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE);
    }
}
