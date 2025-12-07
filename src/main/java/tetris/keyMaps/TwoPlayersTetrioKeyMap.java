package tetris.keyMaps;

import main.MainPanel;
import tetris.boards.tetrio.TetrioBoardWithPhysics;
import tetris.panels.TetrisPanel;

import java.awt.event.KeyEvent;

public class TwoPlayersTetrioKeyMap extends TwoPlayersKeyMap {
    public TwoPlayersTetrioKeyMap(MainPanel mainPanel) {
        super(mainPanel);
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
            case KeyEvent.VK_W -> player1Board.hardDropPressed();
            case KeyEvent.VK_S -> player1Board.softDropPressed();
            case KeyEvent.VK_J -> player1Board.rotateLeftPressed();
            case KeyEvent.VK_K -> player1Board.rotateRightPressed();
            case KeyEvent.VK_L -> player1Board.flipPressed();
            case KeyEvent.VK_SHIFT ->  ((TetrioBoardWithPhysics) player1Board).hold();
            case KeyEvent.VK_LEFT -> player2Board.moveLeftPressed();
            case KeyEvent.VK_RIGHT -> player2Board.moveRightPressed();
            case KeyEvent.VK_UP -> player2Board.hardDropPressed();
            case KeyEvent.VK_DOWN -> player2Board.softDropPressed();
            case KeyEvent.VK_NUMPAD1 -> player2Board.rotateLeftPressed();
            case KeyEvent.VK_NUMPAD2 -> player2Board.rotateRightPressed();
            case KeyEvent.VK_NUMPAD3 -> player2Board.flipPressed();
            case KeyEvent.VK_CONTROL -> ((TetrioBoardWithPhysics) player2Board).hold();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player1Board.moveLeftReleased();
            case KeyEvent.VK_D -> player1Board.moveRightReleased();
            case KeyEvent.VK_W -> player1Board.hardDropReleased();
            case KeyEvent.VK_S -> player1Board.softDropReleased();
            case KeyEvent.VK_J -> player1Board.rotateLeftReleased();
            case KeyEvent.VK_K -> player1Board.rotateRightReleased();
            case KeyEvent.VK_L -> player1Board.flipReleased();
            case KeyEvent.VK_LEFT -> player2Board.moveLeftReleased();
            case KeyEvent.VK_RIGHT -> player2Board.moveRightReleased();
            case KeyEvent.VK_UP -> player2Board.hardDropReleased();
            case KeyEvent.VK_DOWN -> player2Board.softDropReleased();
            case KeyEvent.VK_NUMPAD1 -> player2Board.rotateLeftReleased();
            case KeyEvent.VK_NUMPAD2 -> player2Board.rotateRightReleased();
            case KeyEvent.VK_NUMPAD3 -> player2Board.flipReleased();
        }
    }
}
