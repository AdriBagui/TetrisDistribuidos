package tetris.tetrominoes;

import tetris.GamePanel;

import java.awt.*;

public class TetrominoCell {
    private static final int BORDER_WIDTH = GamePanel.CELL_SIZE/6;
    private static final int[][] XPOINTS = {
            {0, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE - BORDER_WIDTH, 0 + BORDER_WIDTH},
            {0, 0, 0 + BORDER_WIDTH, 0 + BORDER_WIDTH},
            {GamePanel.CELL_SIZE, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE - BORDER_WIDTH, GamePanel.CELL_SIZE - BORDER_WIDTH},
            {0, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE-BORDER_WIDTH, 0 + BORDER_WIDTH}
    };
    private static final int[][] YPOINTS = {
            {0, 0, 0 + BORDER_WIDTH, 0 + BORDER_WIDTH},
            {0, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE - BORDER_WIDTH, 0 + BORDER_WIDTH},
            {0, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE - BORDER_WIDTH, 0 + BORDER_WIDTH},
            {GamePanel.CELL_SIZE, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE - BORDER_WIDTH, GamePanel.CELL_SIZE - BORDER_WIDTH}

    };
    private static final int NPOINTS = 4;

    public static void draw(Graphics2D g2, int x, int y, Color c) {
        drawClassicCell(g2, x, y, c);
    }

    public static void drawMinimal(Graphics2D g2, int x, int y, Color c) {
        // Draw Cell Fill
        g2.setColor(c);
        g2.fillRect(x, y, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE);

        // Draw Cell Border
        g2.setColor(c.darker());
        g2.drawRect(x, y, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE);
    }

    public static void drawClassicCell(Graphics2D g2, int x, int y, Color c) {
        // Draw Cell Fill
        g2.setColor(c);
        g2.fillRect(x, y, GamePanel.CELL_SIZE, GamePanel.CELL_SIZE);

        // Draw Cell Border
        int[] xPoints = new int[NPOINTS];
        int[] yPoints = new int[NPOINTS];
        Color borderColor = new Color(255, 255, 255, 160*c.getAlpha()/255);

        for (int i = 0; i < XPOINTS.length; i++) {
            for (int j = 0; j < NPOINTS; j++) {
                xPoints[j] = XPOINTS[i][j] + x;
                yPoints[j] = YPOINTS[i][j] + y;
            }

            if (i % 2 == 0) {
                borderColor.darker();
            }

            g2.setColor(borderColor);
            g2.fillPolygon(xPoints, yPoints, NPOINTS);
            borderColor = borderColor.darker();
        }
    }
}
