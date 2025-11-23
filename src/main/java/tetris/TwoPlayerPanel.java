package tetris;

import tetris.tetrominoes.Tetromino;

import javax.swing.*;
import java.awt.*;

// ==========================================
// CLASS: MainPanel
// Handles drawing both boards and scores
// ==========================================
public class TwoPlayerPanel extends JPanel {
    private MainFrame mainFrame;
    private Board p1Board;
    private Board p2Board;
    private boolean gameOver;

    public static final int CELL_SIZE = 25;

    public TwoPlayerPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(1100, 700));
        setBackground(Color.DARK_GRAY);

        // Initialize Boards
        reset();
    }

    public void reset() {
        gameOver = false;
        long seed = System.currentTimeMillis();
        p1Board = new Board(1, seed, 150, 50);
        p2Board = new Board(2, seed, 700, 50);
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
        p1Board.draw(g2);

        // Draw Player 2 Area
        p2Board.draw(g2);

        // Draw Controls/Info
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Player 1 (WASD)", 150, 40);
        g2.drawString("Player 2 (Arrows)", 700, 40);

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
}