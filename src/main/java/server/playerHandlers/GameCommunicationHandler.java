package server.playerHandlers;

import server.GameMode;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Initializes and manages a game session between two matched players.
 * <p>
 * This class is responsible for:
 * 1. Generating a shared random seed for deterministic gameplay.
 * 2. Sending the seed to both players.
 * 3. Spawning two {@link PlayerCommunicationHandler} threads to relay inputs bi-directionally.
 * </p>
 */
public class GameCommunicationHandler{
    private final Socket player1;
    private final Socket player2;
    private boolean communicationFromPlayer1ToPlayer2Down;
    private boolean communicationFromPlayer2ToPlayer1Down;

    /**
     * Creates a new handler for a match between two players.
     *
     * @param player1 The socket of the first player (e.g., host or first in queue).
     * @param player2 The socket of the second player (e.g., joiner or second in queue).
     */
    public GameCommunicationHandler(Socket player1, Socket player2){
        this.player1 = player1;
        this.player2 = player2;
        communicationFromPlayer1ToPlayer2Down = false;
        communicationFromPlayer2ToPlayer1Down = false;
    }

    /**
     * Starts the game initialization process.
     */
    public void startCommunicationBetweenPlayers() {
        // We need 2 threads: Client1 -> Client2 AND Client2 -> Client1
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // Generate a seed based on server time to ensure both players generate the same pieces
        long seed = System.currentTimeMillis();

        // Start the input relays
        pool.execute(new PlayerCommunicationHandler(this, player1, player2, seed));
        pool.execute(new PlayerCommunicationHandler(this, player2, player1, seed));

        System.out.println("Game started. Seed: " + seed);

        pool.shutdown();
    }

    /**
     * Stores that the shutDownStarter socket has received a close notification (other end has closed its output)
     * and that the other end of the other player's socket has been notified that the communication has ended (by closing its output)
     * And if it detects that both ways communications have finished, it closes the sockets.
     * (So this method should be called after shutting down the input of the shutDownStarter and the output of the other player's socket)
     *
     * @param shutDownStarter  The socket that receives the close notification
     */
    public synchronized void processUnidirectionalShutdown(Socket shutDownStarter) {
        if (shutDownStarter.equals(player1)) {
            communicationFromPlayer1ToPlayer2Down = true;
        }
        else {
            communicationFromPlayer2ToPlayer1Down = true;
        }

        if (communicationFromPlayer1ToPlayer2Down && communicationFromPlayer2ToPlayer1Down) {
            try {
                player1.close();
                player2.close();
            }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket after game being finished."); } // This should never happen, if it does your computer is broken sry
        }
    }

    public synchronized void processBidirectionalShutdown() {
        communicationFromPlayer1ToPlayer2Down = true;
        communicationFromPlayer2ToPlayer1Down = true;

        try {
            player1.close();
            player2.close();
        }
        catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket after disconnection of one player."); } // This should never happen, if it does your computer is broken sry
    }
}