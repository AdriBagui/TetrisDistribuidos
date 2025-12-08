package tetris.tetrominoes;

import java.awt.*;

import static client.userInterface.panels.MainPanel.CELL_SIZE;
import static client.userInterface.panels.MainPanel.TETROMINO_BORDER_WIDTH;

/**
 * Utility class for rendering individual blocks (cells) of a Tetromino.
 * <p>
 * This class encapsulates the drawing logic to give the blocks their distinctive style
 * (e.g., the "classic" bevel effect).
 * </p>
 */
public class TetrominoCell {
    /** * Polygon coordinates for the bevel border effect.
     * These define 4 trapezoids for the Top, Right, Bottom, and Left borders.
     */
    private static final int[][] XPOINTS = {
            {0, CELL_SIZE, CELL_SIZE - TETROMINO_BORDER_WIDTH, TETROMINO_BORDER_WIDTH}, // Top
            {0, 0, TETROMINO_BORDER_WIDTH, TETROMINO_BORDER_WIDTH},                     // Left
            {CELL_SIZE, CELL_SIZE, CELL_SIZE - TETROMINO_BORDER_WIDTH, CELL_SIZE - TETROMINO_BORDER_WIDTH}, // Right
            {0, CELL_SIZE, CELL_SIZE- TETROMINO_BORDER_WIDTH, TETROMINO_BORDER_WIDTH}   // Bottom
    };

    private static final int[][] YPOINTS = {
            {0, 0, TETROMINO_BORDER_WIDTH, TETROMINO_BORDER_WIDTH}, // Top
            {0, CELL_SIZE, CELL_SIZE - TETROMINO_BORDER_WIDTH, TETROMINO_BORDER_WIDTH}, // Left
            {0, CELL_SIZE, CELL_SIZE - TETROMINO_BORDER_WIDTH, TETROMINO_BORDER_WIDTH}, // Right
            {CELL_SIZE, CELL_SIZE, CELL_SIZE - TETROMINO_BORDER_WIDTH, CELL_SIZE - TETROMINO_BORDER_WIDTH} // Bottom
    };

    private static final int NPOINTS = 4;

    /**
     * Draws a standard Tetromino cell.
     * * @param g2 The graphics context.
     * @param x  The absolute screen X coordinate.
     * @param y  The absolute screen Y coordinate.
     * @param c  The base color of the block.
     */
    public static void draw(Graphics2D g2, int x, int y, Color c) {
        drawClassicCell(g2, x, y, c);
    }

    /**
     * Draws a simplified cell with a simple border.
     * Useful for debugging or low-detail rendering.
     *
     * @param g2 The graphics context.
     * @param x  Screen X.
     * @param y  Screen Y.
     * @param c  Color.
     */
    public static void drawMinimal(Graphics2D g2, int x, int y, Color c) {
        g2.setColor(c);
        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

        g2.setColor(c.darker());
        g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
    }

    /**
     * Draws a "Classic" style cell with 3D-like beveled edges.
     * <p>
     * The method calculates a semi-transparent border color derived from the base color's
     * alpha channel to create a lighting effect (lighter on top/left, darker on bottom/right).
     * </p>
     *
     * @param g2 The graphics context.
     * @param x  Screen X.
     * @param y  Screen Y.
     * @param c  Base color.
     */
    public static void drawClassicCell(Graphics2D g2, int x, int y, Color c) {
        // 1. Fill the center
        g2.setColor(c);
        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

        // 2. Draw the bevel borders
        int[] xPoints = new int[NPOINTS];
        int[] yPoints = new int[NPOINTS];

        // Calculate border brightness based on alpha to allow for transparency (ghost piece)
        Color borderColor = new Color(255, 255, 255, 160 * c.getAlpha() / 255);

        for (int i = 0; i < XPOINTS.length; i++) {
            // Translate polygon to current position
            for (int j = 0; j < NPOINTS; j++) {
                xPoints[j] = XPOINTS[i][j] + x;
                yPoints[j] = YPOINTS[i][j] + y;
            }

            // Simple shading logic: alternating transparency/brightness
            if (i % 2 == 0) {
                borderColor.darker();
            }

            g2.setColor(borderColor);
            g2.fillPolygon(xPoints, yPoints, NPOINTS);

            // Darken for the next side to simulate shadow
            borderColor = borderColor.darker();
        }
    }
}