package server.playerHandlers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Initializes and manages a game session between two matched players.
 * <p>
 * This class is responsible for:
 * 1. Generating a shared random seed for deterministic gameplay.
 * 2. Sending the seed to both clients.
 * 3. Spawning two {@link PlayerCommunicationHandler} threads to relay inputs bi-directionally.
 * </p>
 */
public class GameCommunicationHandler implements Runnable {
    private final Socket client1;
    private final Socket client2;

    /**
     * Creates a new handler for a match between two clients.
     *
     * @param client1 The socket of the first player (e.g., host or first in queue).
     * @param client2 The socket of the second player (e.g., joiner or second in queue).
     */
    public GameCommunicationHandler(Socket client1, Socket client2){
        this.client1 = client1;
        this.client2 = client2;
    }

    /**
     * Starts the game initialization process.
     */
    @Override
    public void run() {
        // We need 2 threads: Client1 -> Client2 AND Client2 -> Client1
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try {
            DataOutputStream dosC1 = new DataOutputStream(client1.getOutputStream());
            DataOutputStream dosC2 = new DataOutputStream(client2.getOutputStream());

            // Generate a seed based on server time to ensure both clients generate the same pieces
            long seed = System.currentTimeMillis();

            // Broadcast seed
            dosC1.writeLong(seed);
            dosC2.writeLong(seed);
            dosC1.flush();
            dosC2.flush();

            System.out.println("Game started. Seed: " + seed);

        } catch (IOException ioe) {
            System.out.println("Error initializing game. Closing sockets.");
            closeQuietly(client1);
            closeQuietly(client2);
            pool.shutdown();
            return;
        }

        // Start the input relays
        pool.execute(new PlayerCommunicationHandler(client1, client2));
        pool.execute(new PlayerCommunicationHandler(client2, client1));

        // Shutdown the pool (it will wait for the threads to finish naturally)
        pool.shutdown();
    }

    /**
     * Helper to close a socket without throwing checked exceptions.
     */
    private void closeQuietly(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            // Log if necessary
        }
    }
}