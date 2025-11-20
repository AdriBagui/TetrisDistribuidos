package classictetris;

import javax.swing.*;
import java.awt.*;

// ==========================================
// CLASS: Canvas
// Handles drawing both boards and scores
// ==========================================
public class Canvas extends JPanel {
    private MainFrame mainFrame;
    private Board p1Board;
    private Board p2Board;
    private boolean gameOver = false;

    public static final int CELL_SIZE = 25;
    private static final int BOARD_WIDTH_PIXELS = 10 * CELL_SIZE;
    private static final int BOARD_HEIGHT_PIXELS = 20 * CELL_SIZE;

    public Canvas(MainFrame frame) {
        this.mainFrame = frame;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.DARK_GRAY);

        // Initialize Boards
        p1Board = new Board(1, frame);
        p2Board = new Board(2, frame);
    }

    public void reset() {
        gameOver = false;
        p1Board = new Board(1, mainFrame);
        p2Board = new Board(2, mainFrame);
        repaint();
    }

    public void updateBoards() {
        if (p1Board.isAlive()) p1Board.update();
        if (p2Board.isAlive()) p2Board.update();

        if (!p1Board.isAlive() && !p2Board.isAlive()) {
            gameOver = true;
        }
    }

    public boolean isGameOver() { return gameOver; }
    public Board getP1Board() { return p1Board; }
    public Board getP2Board() { return p2Board; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw Player 1 Area
        drawBoard(g2, p1Board, 50, 50);

        // Draw Player 2 Area
        drawBoard(g2, p2Board, 450, 50);

        // Draw Controls/Info
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Player 1 (WASD)", 50, 40);
        g2.drawString("Player 2 (Arrows)", 450, 40);

        if (gameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            String msg = "GAME OVER";
            String winner = "";
            if (p1Board.getScore() > p2Board.getScore()) winner = "Player 1 Wins!";
            else if (p2Board.getScore() > p1Board.getScore()) winner = "Player 2 Wins!";
            else winner = "It's a Tie!";

            g2.drawString(msg, 300, 250);
            g2.drawString(winner, 270, 300);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Press 'R' to Restart", 310, 350);
        }
    }

    private void drawBoard(Graphics2D g, Board board, int offsetX, int offsetY) {
        // Draw Grid Background
        g.setColor(Color.BLACK);
        g.fillRect(offsetX, offsetY, BOARD_WIDTH_PIXELS, BOARD_HEIGHT_PIXELS);

        // Draw Static Blocks
        int[][] grid = board.getGrid();
        for (int r = 0; r < 20; r++) {
            for (int c = 0; c < 10; c++) {
                if (grid[r][c] != 0) {
                    drawCell(g, offsetX + c * CELL_SIZE, offsetY + r * CELL_SIZE, grid[r][c]);
                }
            }
        }

        // Draw Current Falling Block
        Block curr = board.getCurrentBlock();
        if (curr != null) {
            drawBlock(g, curr, offsetX, offsetY);
        }

        // Draw Border
        g.setColor(Color.WHITE);
        g.drawRect(offsetX, offsetY, BOARD_WIDTH_PIXELS, BOARD_HEIGHT_PIXELS);

        // Draw Score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + board.getScore(), offsetX, offsetY + BOARD_HEIGHT_PIXELS + 30);

        // Draw Next Piece Preview
        g.drawString("Next:", offsetX + BOARD_WIDTH_PIXELS + 20, offsetY + 50);
        Block next = board.getNextBlock();
        if (next != null) {
            // Create a dummy version at 0,0 for preview rendering
            int previewX = offsetX + BOARD_WIDTH_PIXELS + 30;
            int previewY = offsetY + 60;
            drawBlockPreview(g, next, previewX, previewY);
        }
    }

    private void drawBlock(Graphics2D g, Block b, int offX, int offY) {
        for (Point p : b.getPoints()) {
            int x = offX + (b.getX() + p.x) * CELL_SIZE;
            int y = offY + (b.getY() + p.y) * CELL_SIZE;
            drawCell(g, x, y, b.getColorIndex());
        }
    }

    private void drawBlockPreview(Graphics2D g, Block b, int x, int y) {
        for (Point p : b.getPoints()) {
            drawCell(g, x + p.x * CELL_SIZE, y + p.y * CELL_SIZE, b.getColorIndex());
        }
    }

    private void drawCell(Graphics2D g, int x, int y, int colorIndex) {
        Color c = getColorForIndex(colorIndex);
        g.setColor(c);
        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        g.setColor(c.darker());
        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
    }

    private Color getColorForIndex(int i) {
        return switch (i) {
            case 1 -> Color.CYAN;   // I
            case 2 -> Color.BLUE;   // J
            case 3 -> Color.ORANGE; // L
            case 4 -> Color.YELLOW; // O
            case 5 -> Color.GREEN;  // S
            case 6 -> Color.MAGENTA;// T
            case 7 -> Color.RED;    // Z
            default -> Color.GRAY;
        };
    }
}