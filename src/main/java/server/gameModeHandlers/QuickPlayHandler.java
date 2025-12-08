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
     * Gets the currently waiting player without removing them.
     *
     * @return The waiting {@link Socket}, or {@code null} if queue is empty.
     */
    public synchronized Socket getWaitingPlayer() {
        return waitingPlayer;
    }

    /**
     * Retrieves the currently waiting player and clears the queue (removes them).
     * This is atomic to ensure only one opponent matches with the waiting player.
     *
     * @return The {@link Socket} of the player who was waiting.
     */
    public synchronized Socket getAndRemoveWaitingPlayer() {
        Socket aux = getWaitingPlayer();
        setWaitingPlayer(null);
        return aux;
    }

    /**
     * Sets a player as the current waiting player.
     *
     * @param waitingPlayer The socket of the player joining the queue.
     */
    public synchronized void setWaitingPlayer(Socket waitingPlayer) {
        this.waitingPlayer = waitingPlayer;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return {@code true} if no one is waiting, {@code false} otherwise.
     */
    public synchronized boolean noPlayerWaiting() {
        return waitingPlayer == null;
    }
}