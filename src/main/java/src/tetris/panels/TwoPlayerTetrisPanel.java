package src.tetris.panels;

import src.MainPanel;
import src.tetris.boards.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.util.Timer;
import java.util.TimerTask;

import static src.tetris.Config.*;

public abstract class TwoPlayerTetrisPanel extends JPanel {
    protected MainPanel mainPanel;
    protected Board board1;
    protected Board board2;
    private java.util.Timer gameLoopTimer;
    protected boolean gameOver;

    public TwoPlayerTetrisPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);

        addKeyListener(initializeKeyListener());
    }

    protected abstract KeyAdapter initializeKeyListener();
    protected abstract void initializeGame();

    public void startGame() {
        initializeGame();
        requestFocusInWindow();
        gameLoopTimer = new Timer();
        gameLoopTimer.scheduleAtFixedRate(new GameLoop(), 0, MILLISECONDS_PER_FRAME);
    }

    public abstract void update();

    public void draw(Graphics2D g2) {
        board1.draw(g2);
        board2.draw(g2);

        if (gameOver) {
            String gameOverMessage = "GAME OVER";
            String winner;
            int winnerWidth = 18*CELL_SIZE;
            int gameOverWidth = 10*CELL_SIZE;
            int endGameWidth = 13*CELL_SIZE;
            int bannerWidth = winnerWidth + 2*FRAME_PADDING;
            int bannerHeight = 5*CELL_SIZE + 2*FRAME_PADDING;
            int bannerY = 7*CELL_SIZE;

            if (board1.isAlive()) winner = "Ha ganado el jugador 1!";
            else if (board2.isAlive()) winner = "Ha ganado el jugador 2!";
            else {
                winner = "Empate!";
                winnerWidth = 6*CELL_SIZE;
            }

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);
            g2.setColor(Color.WHITE);
            g2.drawRect((PANEL_WIDTH-bannerWidth)/2, bannerY, bannerWidth, bannerHeight);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString(gameOverMessage, (PANEL_WIDTH-gameOverWidth)/2, (int) (bannerY + 1.5*FRAME_PADDING));
            g2.drawString(winner, (PANEL_WIDTH-winnerWidth)/2, (int) (bannerY + 2.5*FRAME_PADDING));

            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString("Presiona 'Enter' para acabar la partida", (PANEL_WIDTH-endGameWidth)/2, (int) (bannerY + 3.5*FRAME_PADDING));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        draw(g2);
    }

    private class GameLoop extends TimerTask {
        @Override
        public void run() {
            update();
            repaint();

            if (gameOver) gameLoopTimer.cancel();
        }
    }
}
