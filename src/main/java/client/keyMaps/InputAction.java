package client.keyMaps;

/**
 * Enumeration representing the possible actions that a player can perform in the game.
 * These actions are mapped to specific keys via the {@link KeyInputHandler}.
 */
public enum InputAction {
    /**
     * Moves the active piece to the left.
     */
    MOVE_LEFT,

    /**
     * Moves the active piece to the right.
     */
    MOVE_RIGHT,

    /**
     * Increases the falling speed of the piece while the key is held.
     */
    SOFT_DROP,

    /**
     * Instantly drops the piece to the bottom and locks it.
     */
    HARD_DROP,

    /**
     * Rotates the piece counter-clockwise.
     */
    ROTATE_LEFT,

    /**
     * Rotates the piece clockwise.
     */
    ROTATE_RIGHT,

    /**
     * Flips the piece 180 degrees (if supported by the rotation system).
     */
    FLIP,

    /**
     * Holds the current piece in the hold queue for later use.
     */
    HOLD,

    /**
     * Returns to the main start menu.
     */
    GO_BACK_TO_START_MENU
}