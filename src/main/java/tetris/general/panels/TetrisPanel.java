package tetris.general.panels;

import main.MainPanel;
import tetris.general.boards.Board;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static tetris.Config.*;

public abstract class TetrisPanel extends JPanel {
    protected MainPanel mainPanel;
    protected Board[] boards;
    private Timer gameLoopTimer;
    protected boolean gameOver;

    public TetrisPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(22, 22, 22));
        setFocusable(true);

        boards = null;
        gameOver = false;
    }

    public abstract void update();
    protected abstract void initializeGame();

    public void startGame() {
        resetGame();
        requestFocusInWindow();
        gameLoopTimer = new Timer();
        gameLoopTimer.scheduleAtFixedRate(new GameLoop(), 0, MILLISECONDS_PER_FRAME);
    }

    public void draw(Graphics2D g2) {
        for (Board board: boards) {
            if (board != null) board.draw(g2);
        }
    }

    protected void resetGame() {
        gameOver = false;
        initializeGame();
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


