package client.userInterface.panels.tetris;

import client.userInterface.panels.MainPanel;
import tetris.boards.Board;
import client.keyMaps.KeyInputHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static client.userInterface.panels.MainPanel.*;

public abstract class TetrisPanel extends JPanel {
    public static final double FPS = 62.5;
    public static final int MILLISECONDS_PER_FRAME = (int) (1000/ FPS);
    public static final double NES_FPS = 50.0070;

    protected KeyInputHandler keyInputHandler;
    protected MainPanel mainPanel;
    protected Board[] boards;
    private Timer gameLoopTimer;
    protected boolean gameOver;

    public TetrisPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);

        boards = null;
        gameOver = false;
    }

    protected abstract void updateBoards();
    protected abstract boolean checkGameOver();
    protected abstract void initializeBoards();

    public void setKeyInputHandler(KeyInputHandler keyInputHandler) { this.keyInputHandler = keyInputHandler; }

    public boolean isGameOver() { return gameOver; }

    public void startGame() {
        resetGame();
        gameLoopTimer = new Timer();
        gameLoopTimer.scheduleAtFixedRate(new GameLoop(), 0, MILLISECONDS_PER_FRAME);
    }

    public void update() {
        updateBoards();
        if (checkGameOver()) setGameOver(true);
    }

    public void draw(Graphics2D g2) {
        for (Board board: boards) {
            if (board != null) board.draw(g2);
        }
    }

    protected void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    protected void resetGame() {
        keyInputHandler.disableGoBackToStartMenuControls();
        keyInputHandler.setPlayer1Board(null);
        keyInputHandler.setPlayer2Board(null);
        setGameOver(false);
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

            if (isGameOver()) {
                gameLoopTimer.cancel();
                keyInputHandler.enableGoBackToStartMenuControls();
                keyInputHandler.disablePlayer2Controls();
                keyInputHandler.disableNESControls();
                keyInputHandler.disableTetrioControls();
            }
        }
    }
}


