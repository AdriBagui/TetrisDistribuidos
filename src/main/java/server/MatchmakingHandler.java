package server;

import server.gameModeHandlers.LobbiesHandler;
import server.gameModeHandlers.QuickPlayHandler;
import server.playerHandlers.GameCommunicationHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles the initial negotiation (handshake) with a connected client.
 * <p>
 * This class implements {@link Runnable} to run on a separate thread. It reads the
 * client's intended {@link ConnectionMode} (e.g., Quick Play, Host, Join) and routes
 * the client to the appropriate handler.
 * </p>
 */
public class MatchmakingHandler implements Runnable {
    private final Socket client;
    private final QuickPlayHandler modernTetrisQuickPlayHandler;
    private final QuickPlayHandler nesQuickPlayHandler;
    private final LobbiesHandler lobbiesHandler;

    /**
     * Creates a new MatchmakingHandler.
     *
     * @param client                       The socket connection to the client.
     * @param modernTetrisQuickPlayHandler The handler for Modern Tetris quick play queue.
     * @param nesQuickPlayHandler          The handler for NES Tetris quick play queue.
     * @param lobbiesHandler               The handler for managing private lobbies.
     */
    public MatchmakingHandler(Socket client, QuickPlayHandler modernTetrisQuickPlayHandler, QuickPlayHandler nesQuickPlayHandler, LobbiesHandler lobbiesHandler){
        this.client = client;
        this.modernTetrisQuickPlayHandler = modernTetrisQuickPlayHandler;
        this.nesQuickPlayHandler = nesQuickPlayHandler;
        this.lobbiesHandler = lobbiesHandler;
    }

    /**
     * Executes the matchmaking logic.
     * <p>
     * Reads an integer from the client representing the {@link ConnectionMode} ordinal,
     * then executes the logic corresponding to that mode.
     * </p>
     */
    @Override
    public void run() {
        DataInputStream dis;
        DataOutputStream dos;
        int gameModeSelectedId;
        ConnectionMode gameModeSelected;

        try {
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

            // 1. Read the requested game mode
            gameModeSelectedId = dis.readInt();

            // Validate the input range
            if (gameModeSelectedId < 0 || gameModeSelectedId >= ConnectionMode.values().length) {
                closeSocket();
                return;
            }

            gameModeSelected = ConnectionMode.values()[gameModeSelectedId];

            // 2. Route to the specific logic
            switch (gameModeSelected) {
                case MODERN_TETRIS_QUICK_PLAY -> quickMatchSearch(modernTetrisQuickPlayHandler);
                case NES_QUICK_PLAY -> quickMatchSearch(nesQuickPlayHandler);
                case HOST_GAME -> hostGameSearch(dos);
                case JOIN_GAME -> joinLobbySearch(dis, dos);
            }
        } catch (IOException ioe) {
            closeSocket();
        }
    }

    /**
     * Generic logic for Quick Play matchmaking.
     * <p>
     * If no one is waiting, this client becomes the waiting player.
     * If someone is already waiting, a match is formed, and a {@link GameCommunicationHandler} is started.
     * </p>
     *
     * @param handler The specific QuickPlayHandler (Modern or NES) to use.
     */
    private void quickMatchSearch(QuickPlayHandler handler) {
        // synchronized check to prevent race conditions is handled inside the handler methods
        if (handler.noPlayerWaiting()) {
            handler.setWaitingPlayer(client);
        } else {
            // Retrieve the waiting player and start the game session
            Socket opponent = handler.getAndRemoveWaitingPlayer();
            new Thread(new GameCommunicationHandler(client, opponent)).start();
        }
    }

    /**
     * Handles the logic for hosting a private game lobby.
     * <p>
     * Requests a new Room ID from the {@link LobbiesHandler} and sends it back to the client.
     * The client socket is then stored in the lobby map waiting for a joiner.
     * </p>
     *
     * @param dos The output stream to send the Room ID to the host.
     */
    private void hostGameSearch(DataOutputStream dos) {
        int roomId = lobbiesHandler.createLobby(client);

        try {
            dos.writeInt(roomId);
            dos.flush();
        } catch (IOException ioe) {
            // CRITICAL: Remove the lobby so players don't try to join a dead room.
            lobbiesHandler.getAndRemoveLobby(roomId);
            closeSocket();
        }
    }

    /**
     * Handles the logic for joining a private game lobby.
     * <p>
     * Reads the Room ID requested by the client. If valid, retrieves the host's socket
     * and starts a {@link GameCommunicationHandler}. Sends a boolean confirmation to the client.
     * </p>
     *
     * @param dis The input stream to read the Room ID.
     * @param dos The output stream to send success/failure confirmation.
     */
    private void joinLobbySearch(DataInputStream dis, DataOutputStream dos) {
        int roomId;

        try {
            roomId = dis.readInt();

            if (lobbiesHandler.containsLobby(roomId)) {
                // Notify client that room exists
                dos.writeBoolean(true);
                dos.flush();

                // Retrieve host and start game
                Socket host = lobbiesHandler.getAndRemoveLobby(roomId);
                new Thread(new GameCommunicationHandler(client, host)).start();
            } else {
                // Notify client that room does not exist
                dos.writeBoolean(false);
                dos.flush();
                // We do not close the socket here immediately; the client might try again or go back.
                // However, in this implementation, the thread ends here.
            }
        } catch (IOException ioe) {
            closeSocket();
        }
    }

    /**
     * Closes the client socket safely.
     */
    private void closeSocket() {
        try {
            if (client != null && !client.isClosed()) client.close();
        } catch (IOException e) {
            System.out.println("Error closing client socket in MatchmakingHandler.");
        }
    }
}