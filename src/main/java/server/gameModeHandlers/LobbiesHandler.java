package server.gameModeHandlers;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

/**
 * Manages private game lobbies.
 * <p>
 * This class is responsible for creating unique Room IDs, storing the host's socket connection,
 * and retrieving it when a challenger joins. It guarantees thread safety using synchronization,
 * as multiple threads (MatchmakingHandlers) may access the lobby map simultaneously.
 * </p>
 */
public class LobbiesHandler {
    private final HashMap<Integer, Socket> lobbies;
    private static final int MAX_NUMBER_OF_LOBBIES = 1000;

    /**
     * Creates a new LobbiesHandler with an empty registry.
     */
    public LobbiesHandler(){
        lobbies = new HashMap<>();
    }

    /**
     * Creates a new lobby for the provided host socket.
     * <p>
     * Generates a random Room ID (0-999). If the ID is taken or the server is full,
     * it handles collision resolution or waits.
     * </p>
     *
     * @param host The socket of the player hosting the game.
     * @return The unique Room ID generated for this lobby.
     */
    public int createLobby(Socket host){
        Random random = new Random();
        int numberOfLobbies;
        int roomId;

        synchronized (this) {
            numberOfLobbies = lobbies.size();

            if (numberOfLobbies < MAX_NUMBER_OF_LOBBIES) {
                // Generate a unique ID
                roomId = random.nextInt(0, MAX_NUMBER_OF_LOBBIES - lobbies.size());
                // Simple linear probing strategy to find a free slot
                while(lobbies.containsKey(roomId)) {
                    roomId = (roomId + 1) % MAX_NUMBER_OF_LOBBIES;
                }

                lobbies.put(roomId, host);

                return roomId;
            }
        }

        try {
            Thread.sleep(100);
            return createLobby(host);
        } catch (InterruptedException e) {
            System.out.println("FATAL ERROR lobbies handler interrupted.");
            return -1;
        } // This should never happen, if it does your computer is broken sry
    }

    /**
     * Retrieves and removes the host socket associated with a Room ID.
     * <p>
     * This is called when a second player successfully joins the lobby. The lobby is
     * removed immediately to prevent others from joining the same session.
     * </p>
     *
     * @param roomId The ID of the room to join.
     * @return The {@link Socket} of the waiting host, or null if not found.
     */
    public synchronized Socket getAndRemoveLobby(int roomId) {
        return lobbies.remove(roomId);
    }

    /**
     * Restores the lobby due to failure when connecting rival by adding the roomId with its host back to the online
     * lobbies
     *
     * @param roomId The roomId of the lobby to restore
     * @param host   The host of the lobby to restore
     */
    public synchronized void restoreLobby(int roomId, Socket host) {
        lobbies.put(roomId, host);
    }
}