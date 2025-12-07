package tetris.keyMaps;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;
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
            case KeyEvent.VK_A -> ((BoardWithPhysics) player1Board).moveLeftPressed();
            case KeyEvent.VK_D -> ((BoardWithPhysics) player1Board).moveRightPressed();
            case KeyEvent.VK_W -> ((BoardWithPhysics) player1Board).hardDropPressed();
            case KeyEvent.VK_S -> ((BoardWithPhysics) player1Board).softDropPressed();
            case KeyEvent.VK_J -> ((BoardWithPhysics) player1Board).rotateLeftPressed();
            case KeyEvent.VK_K -> ((BoardWithPhysics) player1Board).rotateRightPressed();
            case KeyEvent.VK_L -> ((BoardWithPhysics) player1Board).flipPressed();
            case KeyEvent.VK_SHIFT ->  ((TetrioBoardWithPhysics) player1Board).hold();
            case KeyEvent.VK_LEFT -> ((BoardWithPhysics) player2Board).moveLeftPressed();
            case KeyEvent.VK_RIGHT -> ((BoardWithPhysics) player2Board).moveRightPressed();
            case KeyEvent.VK_UP -> ((BoardWithPhysics) player2Board).hardDropPressed();
            case KeyEvent.VK_DOWN -> ((BoardWithPhysics) player2Board).softDropPressed();
            case KeyEvent.VK_NUMPAD1 -> ((BoardWithPhysics) player2Board).rotateLeftPressed();
            case KeyEvent.VK_NUMPAD2 -> ((BoardWithPhysics) player2Board).rotateRightPressed();
            case KeyEvent.VK_NUMPAD3 -> ((BoardWithPhysics) player2Board).flipPressed();
            case KeyEvent.VK_CONTROL -> ((TetrioBoardWithPhysics) player2Board).hold();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> ((BoardWithPhysics) player1Board).moveLeftReleased();
            case KeyEvent.VK_D -> ((BoardWithPhysics) player1Board).moveRightReleased();
            case KeyEvent.VK_W -> ((BoardWithPhysics) player1Board).hardDropReleased();
            case KeyEvent.VK_S -> ((BoardWithPhysics) player1Board).softDropReleased();
            case KeyEvent.VK_J -> ((BoardWithPhysics) player1Board).rotateLeftReleased();
            case KeyEvent.VK_K -> ((BoardWithPhysics) player1Board).rotateRightReleased();
            case KeyEvent.VK_L -> ((BoardWithPhysics) player1Board).flipReleased();
            case KeyEvent.VK_LEFT -> ((BoardWithPhysics) player2Board).moveLeftReleased();
            case KeyEvent.VK_RIGHT -> ((BoardWithPhysics) player2Board).moveRightReleased();
            case KeyEvent.VK_UP -> ((BoardWithPhysics) player2Board).hardDropReleased();
            case KeyEvent.VK_DOWN -> ((BoardWithPhysics) player2Board).softDropReleased();
            case KeyEvent.VK_NUMPAD1 -> ((BoardWithPhysics) player2Board).rotateLeftReleased();
            case KeyEvent.VK_NUMPAD2 -> ((BoardWithPhysics) player2Board).rotateRightReleased();
            case KeyEvent.VK_NUMPAD3 -> ((BoardWithPhysics) player2Board).flipReleased();
        }
    }
}
