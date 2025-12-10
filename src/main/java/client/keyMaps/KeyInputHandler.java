package client.keyMaps;

import client.userInterface.panels.MainPanel;
import tetris.boards.BoardWithPhysics;
import tetris.boards.modernTetris.ModernTetrisBoardWithPhysics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles keyboard input for the application.
 * <p>
 * This class intercepts {@link KeyEvent}s and maps them to game actions ({@link InputAction})
 * based on configurable key bindings. It supports two players and specific control schemes
 * like NES or Modern Tetris styles.
 * </p>
 */
public class KeyInputHandler extends KeyAdapter {
    private final Map<Integer, InputAction> player1ActionsKeyEventMap;
    private final Map<Integer, InputAction> player2ActionsKeyEventMap;
    private int goBackToMenuKeyEvent;
    private final MainPanel mainPanel;
    private BoardWithPhysics player1Board;
    private BoardWithPhysics player2Board;
    private boolean player2ControlsEnabled;
    private boolean NESControlsEnabled;
    private boolean ModernTetrisControlsEnabled;
    private boolean goBackToStartMenuEnabled;

    /**
     * Constructs a new KeyInputHandler.
     *
     * @param mainPanel The main application panel, used for navigation callbacks (e.g., back to menu).
     */
    public KeyInputHandler(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        player1ActionsKeyEventMap = new HashMap<>();
        player2ActionsKeyEventMap = new HashMap<>();
        goBackToMenuKeyEvent = -1;
        player2ControlsEnabled = false;
        player1Board = null;
        player2Board = null;
        NESControlsEnabled = false;
        ModernTetrisControlsEnabled = false;
        goBackToStartMenuEnabled = false;
    }

    /**
     * Invoked when a key has been pressed.
     * Checks if the key corresponds to a bound action and triggers it.
     *
     * @param e The key event.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        boolean actionPerformed = false;

        if (goBackToStartMenuEnabled) {
            if (e.getKeyCode() == goBackToMenuKeyEvent) {
                mainPanel.backToStartMenu();
                actionPerformed = true;
            }
        }
        if (!actionPerformed) tryToPerformKeyInput(e.getKeyCode(), true);
    }

    /**
     * Invoked when a key has been released.
     * Used for actions that depend on holding a key (e.g., movement or soft drop).
     *
     * @param e The key event.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        tryToPerformKeyInput(e.getKeyCode(), false);
    }

    /**
     * Helper method to find the key code currently bound to a specific action.
     * Needed for the Settings UI to display current bindings.
     *
     * @param action  The action to look up.
     * @param player1 {@code true} to check Player 1's bindings, {@code false} for Player 2.
     * @return The key code bound to the action, or -1 if not bound.
     */
    public int getKeyForAction(InputAction action, boolean player1) {
        Map<Integer, InputAction> map = player1 ? player1ActionsKeyEventMap : player2ActionsKeyEventMap;
        for (Map.Entry<Integer, InputAction> entry : map.entrySet()) {
            if (entry.getValue() == action) {
                return entry.getKey();
            }
        }
        return -1; // Not bound
    }

    /**
     * Gets the key code currently bound to the "Go Back to Menu" action.
     *
     * @return The key code.
     */
    public int getGoBackToMenuKey() { return goBackToMenuKeyEvent; }

    /**
     * Checks if a specific key code is currently bound to any action.
     *
     * @param key The key code to check.
     * @return {@code true} if the key is bound, {@code false} otherwise.
     */
    public boolean isKeyBound(int key) {
        return (key == goBackToMenuKeyEvent
                || player1ActionsKeyEventMap.containsKey(key)
                || player2ActionsKeyEventMap.containsKey(key));
    }

    /**
     * Binds a specific key to a player action.
     *
     * @param key     The key code to bind.
     * @param action  The action to trigger.
     * @param player1 {@code true} for Player 1, {@code false} for Player 2.
     */
    public void bindKeyToPlayerAction(int key, InputAction action, boolean player1) {
        if (player1) player1ActionsKeyEventMap.put(key, action);
        else player2ActionsKeyEventMap.put(key, action);
    }

    /**
     * Binds a specific key to the global "Go Back to Menu" action.
     *
     * @param key The key code to bind.
     */
    public void bindGoBackToMenuKey(int key) { goBackToMenuKeyEvent = key; }

    /**
     * Unbinds a specific key, removing its association with any action.
     *
     * @param key The key code to unbind.
     */
    public void unbindKey(int key) {
        if (key == goBackToMenuKeyEvent) goBackToMenuKeyEvent = -1;
        else if (player1ActionsKeyEventMap.remove(key) == null)
            player2ActionsKeyEventMap.remove(key);
    }

    /**
     * Unbinds a specific action for a player, effectively removing the key associated with it.
     *
     * @param action  The action to unbind.
     * @param player1 {@code true} for Player 1, {@code false} for Player 2.
     */
    public void unbindPlayerAction(InputAction action, boolean player1) {
        Map<Integer, InputAction> map = player1 ? player1ActionsKeyEventMap : player2ActionsKeyEventMap;

        for (Integer key : map.keySet()) {
            if (action == map.get(key)) {
                map.remove(key);
                break;
            }
        }
    }

    /**
     * Unbinds the "Go Back to Menu" action.
     */
    public void unbindGoBackToMenuAction() { goBackToMenuKeyEvent = -1; }

    /**
     * Removes the old binding for an action and sets a new one.
     * If the new key was used elsewhere, it is unbound from that previous action.
     *
     * @param action     The action to rebind.
     * @param newKeyCode The new key code to assign.
     * @param player1    {@code true} for Player 1, {@code false} for Player 2.
     */
    public void changeKeyBinding(InputAction action, int newKeyCode, boolean player1) {
        // 1. Remove any existing key bound to this action for this player
        unbindPlayerAction(action, player1);

        // 2. Remove the new key if it was bound to something else (optional, prevents conflicts)
        unbindKey(newKeyCode);

        // 3. Bind the new key
        bindKeyToPlayerAction(newKeyCode, action, player1);
    }

    /**
     * Sets the board instance for Player 1 to control.
     *
     * @param player1Board The board instance.
     */
    public void setPlayer1Board(BoardWithPhysics player1Board) { this.player1Board = player1Board; }

    /**
     * Sets the board instance for Player 2 to control.
     *
     * @param player2Board The board instance.
     */
    public void setPlayer2Board(BoardWithPhysics player2Board) { this.player2Board = player2Board; }

    /**
     * Enables controls for Player 2.
     */
    public void enablePlayer2Controls() { player2ControlsEnabled = true; }

    /**
     * Disables controls for Player 2.
     */
    public void disablePlayer2Controls() { player2ControlsEnabled = false; }

    /**
     * Enables NES-style specific controls and mechanics.
     */
    public void enableNESControls() { NESControlsEnabled = true; }

    /**
     * Disables NES-style specific controls.
     */
    public void disableNESControls() { NESControlsEnabled = false; }

    /**
     * Enables Modern Tetris style specific controls and mechanics.
     */
    public void enableModernTetrisControls() { ModernTetrisControlsEnabled = true; }

    /**
     * Disables Modern Tetris style specific controls.
     */
    public void disableModernTetrisControls() { ModernTetrisControlsEnabled = false; }

    /**
     * Enables the ability to return to the start menu via the bound key.
     */
    public void enableGoBackToStartMenuControls() { goBackToStartMenuEnabled = true; }

    /**
     * Disables the ability to return to the start menu via the bound key.
     */
    public void disableGoBackToStartMenuControls() { goBackToStartMenuEnabled = false; }

    /**
     * Attempts to perform an action based on the input key code.
     * Determines which player pressed the key and delegates to the specific logic.
     *
     * @param key   The key code pressed or released.
     * @param press {@code true} if key press, {@code false} if key release.
     */
    private void tryToPerformKeyInput(Integer key, boolean press) {
        InputAction action;
        boolean actionPerformed;

        if ((action = player1ActionsKeyEventMap.get(key)) != null) {

            actionPerformed = tryToPerformGeneralTetrisAction(action, player1Board, press);
            if (!actionPerformed && ModernTetrisControlsEnabled)
                tryToPerformModernTetrisAction(action, (ModernTetrisBoardWithPhysics) player1Board, press);
        }
        else if (player2ControlsEnabled &&
                (action = player2ActionsKeyEventMap.get(key)) != null) {

            actionPerformed = tryToPerformGeneralTetrisAction(action, player2Board, press);
            if (!actionPerformed && ModernTetrisControlsEnabled)
                tryToPerformModernTetrisAction(action, (ModernTetrisBoardWithPhysics) player2Board, press);
        }
    }

    /**
     * Attempts to perform general Tetris actions (Move, Drop, Rotate).
     *
     * @param action The action to perform.
     * @param board  The board on which to perform the action.
     * @param press  {@code true} if key press, {@code false} if key release.
     * @return {@code true} if the action was handled, {@code false} otherwise.
     */
    private boolean tryToPerformGeneralTetrisAction(InputAction action, BoardWithPhysics board, boolean press) {
        if (!NESControlsEnabled && !ModernTetrisControlsEnabled) return false;

        boolean actionPerformed;

        switch (action) {
            case MOVE_LEFT:
                if (press) board.moveLeftPressed();
                else board.moveLeftReleased();
                actionPerformed = true;
                break;
            case MOVE_RIGHT:
                if (press) board.moveRightPressed();
                else board.moveRightReleased();
                actionPerformed = true;
                break;
            case SOFT_DROP:
                if (press) board.softDropPressed();
                else board.softDropReleased();
                actionPerformed = true;
                break;
            case ROTATE_LEFT:
                if (press) board.rotateLeftPressed();
                else board.rotateLeftReleased();
                actionPerformed = true;
                break;
            case ROTATE_RIGHT:
                if (press) board.rotateRightPressed();
                else board.rotateRightReleased();
                actionPerformed = true;
                break;
            default:
                actionPerformed = false;
                break;
        };

        return actionPerformed;
    }

    /**
     * Attempts to perform Modern Tetris specific actions (Hard Drop, Hold, Flip).
     *
     * @param action The action to perform.
     * @param board  The board on which to perform the action.
     * @param press  {@code true} if key press, {@code false} if key release.
     * @return {@code true} if the action was handled, {@code false} otherwise.
     */
    private boolean tryToPerformModernTetrisAction(InputAction action, ModernTetrisBoardWithPhysics board, boolean press) {
        boolean actionPerformed;

        switch (action) {
            case HARD_DROP:
                if (press) board.hardDropPressed();
                else board.hardDropReleased();
                actionPerformed = true;
                break;
            case FLIP:
                if (press) board.flipPressed();
                else board.flipReleased();
                actionPerformed = true;
                break;
            case HOLD:
                if (press) {
                    board.hold();
                    actionPerformed = true;
                }
                else actionPerformed = false;
                break;
            default:
                actionPerformed = false;
                break;
        };

        return actionPerformed;
    }
}