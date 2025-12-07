package tetris.keyMaps;

import main.MainPanel;
import tetris.panels.TetrisPanel;

import java.awt.event.KeyEvent;

public class TwoPlayersNESKeyMap extends TwoPlayersKeyMap {
    public TwoPlayersNESKeyMap(MainPanel mainPanel, TetrisPanel tetrisPanel) {
        super(mainPanel, tetrisPanel);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!player1Board.isAlive() || !player2Board.isAlive()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                mainPanel.backToStartMenu();
            }
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player1Board.moveLeftPressed();
            case KeyEvent.VK_D -> player1Board.moveRightPressed();
            case KeyEvent.VK_W -> player1Board.rotateRightPressed();
            case KeyEvent.VK_S -> player1Board.softDropPressed();
            case KeyEvent.VK_LEFT -> player2Board.moveLeftPressed();
            case KeyEvent.VK_RIGHT -> player2Board.moveRightPressed();
            case KeyEvent.VK_UP -> player2Board.rotateRightPressed();
            case KeyEvent.VK_DOWN -> player2Board.softDropPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player1Board.moveLeftReleased();
            case KeyEvent.VK_D -> player1Board.moveRightReleased();
            case KeyEvent.VK_W -> player1Board.rotateRightReleased();
            case KeyEvent.VK_S -> player1Board.softDropReleased();
            case KeyEvent.VK_LEFT -> player2Board.moveLeftReleased();
            case KeyEvent.VK_RIGHT -> player2Board.moveRightReleased();
            case KeyEvent.VK_UP -> player2Board.rotateRightReleased();
            case KeyEvent.VK_DOWN -> player2Board.softDropReleased();
        }
    }
}
