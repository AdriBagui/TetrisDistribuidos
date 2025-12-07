package tetris.keyMaps;

import main.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.boards.tetrio.TetrioBoardWithPhysics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyInputHandler extends KeyAdapter {
    private final Map<Integer, InputAction> player1ActionsKeyEventMap;
    private final Map<Integer, InputAction> player2ActionsKeyEventMap;
    private int goBackToMenuKeyEvent;
    private final MainPanel mainPanel;
    private BoardWithPhysics player1Board;
    private BoardWithPhysics player2Board;
    private boolean player2ControlsEnabled;
    private boolean NESControlsEnabled;
    private boolean TetrioControlsEnabled;
    private boolean goBackToStartMenuEnabled;

    public KeyInputHandler(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        player1ActionsKeyEventMap = new HashMap<>();
        player2ActionsKeyEventMap = new HashMap<>();
        goBackToMenuKeyEvent = -1;
        player2ControlsEnabled = false;
        player1Board = null;
        player2Board = null;
        NESControlsEnabled = false;
        TetrioControlsEnabled = false;
        goBackToStartMenuEnabled = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        InputAction action;
        boolean actionPerformed = false;

        if (goBackToStartMenuEnabled) {
            if (e.getKeyCode() == goBackToMenuKeyEvent) {
                mainPanel.backToStartMenu();
                actionPerformed = true;
            }
        }
        if (!actionPerformed) tryToPerformKeyInput(e.getKeyCode(), true);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        tryToPerformKeyInput(e.getKeyCode(), false);
    }

    public void bindKeyToPlayerAction(int key, InputAction action, boolean player1) {
        if (player1) player1ActionsKeyEventMap.put(key, action);
        else player2ActionsKeyEventMap.put(key, action);
    }
    public void bindGoBackToMenuKey(int key) { goBackToMenuKeyEvent = key; }
    public void unbindKey(int key) {
        if (key == goBackToMenuKeyEvent) goBackToMenuKeyEvent = -1;
        else if (player1ActionsKeyEventMap.remove(key) == null)
            player2ActionsKeyEventMap.remove(key);
    }
    public void unbindPlayerAction(InputAction action, boolean player1) {
        if (player1) {
            for (Integer key : player1ActionsKeyEventMap.keySet()) {
                if (action == player1ActionsKeyEventMap.get(key)) {
                    player1ActionsKeyEventMap.remove(key);
                    break;
                }
            }
        }
        else {
            for (Integer key : player2ActionsKeyEventMap.keySet()) {
                if (action == player2ActionsKeyEventMap.get(key)) {
                    player2ActionsKeyEventMap.remove(key);
                    break;
                }
            }
        }
    }
    public void unbindGoBackToMenuAction(InputAction action, boolean player1) { goBackToMenuKeyEvent = -1; }
    public boolean isKeyBound(int key) {
        return (key == goBackToMenuKeyEvent
                || player1ActionsKeyEventMap.containsKey(key)
                || player2ActionsKeyEventMap.containsKey(key));
    }

    public void setPlayer1Board(BoardWithPhysics player1Board) { this.player1Board = player1Board; }
    public void setPlayer2Board(BoardWithPhysics player2Board) { this.player2Board = player2Board; }

    public void enablePlayer2Controls() { player2ControlsEnabled = true; }
    public void disablePlayer2Controls() { player2ControlsEnabled = false; }
    public void enableNESControls() { NESControlsEnabled = true; }
    public void disableNESControls() { NESControlsEnabled = false; }
    public void enableTetrioControls() { TetrioControlsEnabled = true; }
    public void disableTetrioControls() { TetrioControlsEnabled = false; }
    public void enableGoBackToStartMenuControls() { goBackToStartMenuEnabled = true; }
    public void disableGoBackToStartMenuControls() { goBackToStartMenuEnabled = false; }

    private void tryToPerformKeyInput(Integer key, boolean press) {
        InputAction action;
        boolean actionPerformed = false;

        if (player1ActionsKeyEventMap.containsKey(key)) {
            action = player1ActionsKeyEventMap.get(key);

            actionPerformed = tryToPerformGeneralTetrisAction(action, player1Board, press);
            if (!actionPerformed && TetrioControlsEnabled)
                tryToPerformTetrioAction(action, (TetrioBoardWithPhysics) player1Board, press);
        }
        else if (player2ControlsEnabled && player2ActionsKeyEventMap.containsKey(key)) {
            action = player2ActionsKeyEventMap.get(key);

            actionPerformed = tryToPerformGeneralTetrisAction(action, player2Board, press);
            if (!actionPerformed && TetrioControlsEnabled)
                tryToPerformTetrioAction(action, (TetrioBoardWithPhysics) player2Board, press);
        }
    }
    private boolean tryToPerformGeneralTetrisAction(InputAction action, BoardWithPhysics board, boolean press) {
        if (!NESControlsEnabled && !TetrioControlsEnabled) return false;

        boolean actionPerformed = false;

        actionPerformed = switch (action) {
            case MOVE_LEFT -> {
                if (press) board.moveLeftPressed();
                else board.moveLeftReleased();
                yield true;
            }
            case MOVE_RIGHT -> {
                if (press) board.moveRightPressed();
                else board.moveRightReleased();
                yield true;
            }
            case SOFT_DROP -> {
                if (press) board.softDropPressed();
                else board.softDropReleased();
                yield true;
            }
            case ROTATE_LEFT -> {
                if (press) board.rotateLeftPressed();
                else board.rotateLeftReleased();
                yield true;
            }
            case ROTATE_RIGHT -> {
                if (press) board.rotateRightPressed();
                else board.rotateRightReleased();
                yield true;
            }
            default -> false;
        };

        return actionPerformed;
    }
    private boolean tryToPerformTetrioAction(InputAction action, TetrioBoardWithPhysics board, boolean press) {
        boolean actionPerformed = false;

        actionPerformed = switch (action) {
            case HARD_DROP -> {
                if (press) board.hardDropPressed();
                else board.hardDropReleased();
                yield true;
            }
            case FLIP -> {
                if (press) board.flipPressed();
                else board.flipReleased();
                yield true;
            }
            case HOLD -> {
                if (press) board.hold();
                yield true;
            }
            default -> false;
        };

        return actionPerformed;
    }
}
