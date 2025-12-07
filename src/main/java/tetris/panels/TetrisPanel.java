package tetris.panels;

import main.MainPanel;
import tetris.boards.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import static main.MainPanel.PANEL_HEIGHT;
import static main.MainPanel.PANEL_WIDTH;
import static tetris.Config.*;

public abstract class TetrisPanel extends JPanel {
    public static final double FPS = 62.5;
    public static final int MILLISECONDS_PER_FRAME = (int) (1000/ FPS);
    public static final double NES_FPS = 50.0070;

    protected MainPanel mainPanel;
    protected Board[] boards;
    protected KeyListener keyMap;
    private Timer gameLoopTimer;
    protected boolean gameOver;

    public TetrisPanel(MainPanel mainPanel, KeyListener keyMap) {
        this.mainPanel = mainPanel;
        this.keyMap = keyMap;

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(22, 22, 22));
        setFocusable(true);

        boards = null;
        gameOver = false;

        addKeyListener(keyMap);
    }

    public abstract void update();
    protected abstract void initializeBoards();

    public boolean isGameOver() { return gameOver; }

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
        initializeBoards();
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


