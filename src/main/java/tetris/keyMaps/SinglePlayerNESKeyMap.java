package tetris.keyMaps;

import main.MainPanel;

import java.awt.event.KeyEvent;

public class SinglePlayerNESKeyMap extends SinglePlayerKeyMap {
    public SinglePlayerNESKeyMap(MainPanel mainPanel) {
        super(mainPanel);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!board.isAlive()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                mainPanel.backToStartMenu();
            }
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> board.moveLeftPressed();
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> board.moveRightPressed();
            case KeyEvent.VK_W, KeyEvent.VK_UP -> board.rotateRightPressed();
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> board.softDropPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> board.moveLeftReleased();
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> board.moveRightReleased();
            case KeyEvent.VK_W, KeyEvent.VK_UP -> board.rotateRightReleased();
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> board.softDropReleased();
        }
    }
}
