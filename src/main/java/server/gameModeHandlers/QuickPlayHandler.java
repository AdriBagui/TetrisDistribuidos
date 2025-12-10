package server.gameModeHandlers;

import java.net.Socket;

/**
 * Manages the matchmaking queue for Quick Play modes.
 * <p>
 * This class acts as a simple thread-safe holder for a waiting player.
 * Since the game is 1v1, we only need to store one "waiting" socket.
 * If a new player arrives and someone is waiting, a match is made.
 * If no one is waiting, the new player becomes the waiting player.
 * </p>
 */
public class QuickPlayHandler {
    private Socket waitingPlayer;

    /**
     * Creates a new QuickPlayHandler with no players waiting.
     */
    public QuickPlayHandler() {
        waitingPlayer = null;
    }

    /**
     * If there is a waiting player returns the waiting player. Otherwise, sets the given player as a waiting player and returns null
     *
     * @param player The player to handle (either returning its rival or putting it to wait)
     * @return If there is a waiting player, it returns its socket, otherwise, it returns null.
     */
    public synchronized Socket handlePlayer(Socket player) {
        Socket opponent = null;

        if (waitingPlayer == null) waitingPlayer = player;
        else {
            opponent = waitingPlayer;
            waitingPlayer = null;
        }

        return opponent;
    }
}