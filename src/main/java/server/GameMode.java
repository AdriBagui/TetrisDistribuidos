package server;

/**
 * Enumeration representing the different types of game modes a client can request.
 * <p>
 * This is used during the initial handshake in {@link MatchmakingHandler} to determine
 * how to route the client (e.g., put them in a queue or create a lobby).
 * </p>
 */
public enum GameMode {
    /**
     * Request to find a random opponent for Modern Tetris.
     */
    MODERN_TETRIS_QUICK_PLAY,

    /**
     * Request to find a random opponent for NES (Classic) Tetris.
     */
    NES_QUICK_PLAY,

    /**
     * Request to create a private lobby and wait for a specific opponent.
     */
    HOST_GAME,

    /**
     * Request to join an existing private lobby using a Room ID.
     */
    JOIN_GAME
}