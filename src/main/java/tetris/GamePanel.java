package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import static tetris.Config.*;

// ==========================================
// CLASS: MainPanel
// Handles drawing both boards and scores
// ==========================================
public class GamePanel extends JPanel {
    private MainFrame mainFrame;
    private Timer gameLoop;
    private Timer paintLoop;
    private Board p1Board;
    private Board p2Board;
    private boolean gameOver;

    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.DARK_GRAY);

        // Initialize Boards
        reset();

        // Game Loop (approx 60fps (62.5fps) for smoothness, logic updates slower)
        new Timer().scheduleAtFixedRate(new GameLoop(), 0, MILIS_PER_FRAME);
    }

    public void reset() {
        gameOver = false;
        long seed = System.currentTimeMillis();
        p1Board = new Board(1, seed, BOARD1_X, BOARD1_Y);
        p2Board = new Board(2, seed, BOARD2_X, BOARD2_Y);
        p1Board.setEnemyBoard(p2Board);
        p2Board.setEnemyBoard(p1Board);
        repaint();
    }

    public void updateBoards() {
        if (p1Board.isAlive()) p1Board.update();
        if (p2Board.isAlive()) p2Board.update();

        if (!p1Board.isAlive() || !p2Board.isAlive()) {
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
        //g2.setColor(Color.WHITE);
        //g2.setFont(new Font("Arial", Font.BOLD, 16));
        //g2.drawString("Player 1 (WASD)", BOARD1_X, FRAME_PADDING - 16);
        //g2.drawString("Player 2 (Arrows)", BOARD2_X, FRAME_PADDING - 16);

        if (gameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            String msg = "GAME OVER";
            String winner = "";
            if (p1Board.isAlive()) winner = "Player 1 Wins!";
            else if (p2Board.isAlive()) winner = "Player 2 Wins!";
            else winner = "It's a Tie!";

            g2.drawString(msg, 300, 250);
            g2.drawString(winner, 270, 300);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Press 'R' to Restart", 310, 350);
        }
    }

    public void restartGame() {
        reset();
    }

    private class GameLoop extends TimerTask {
        @Override
        public void run() {
            if (!isGameOver()) {
                updateBoards();

            }
        }
    }

    private class PainLoop extends TimerTask {
        @Override
        public void run() {
            repaint();
        }
    }
}