package server;

import server.gameModeHandlers.LobbiesHandler;
import server.gameModeHandlers.QuickPlayHandler;
import server.playerHandlers.GameCommunicationHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static server.GameMode.*;

/**
 * Handles the initial negotiation (handshake) with a connected client.
 * <p>
 * This class implements {@link Runnable} to run on a separate thread. It reads the
 * client's intended {@link GameMode} (e.g., Quick Play, Host, Join) and routes
 * the client to the appropriate handler.
 * </p>
 */
public class MatchmakingHandler implements Runnable {
    private Socket player;
    private DataOutputStream dos;
    private DataInputStream dis;
    private final QuickPlayHandler modernTetrisQuickPlayHandler;
    private final QuickPlayHandler nesQuickPlayHandler;
    private final LobbiesHandler lobbiesHandler;

    /**
     * Creates a new MatchmakingHandler.
     *
     * @param player                       The socket connection to the client.
     * @param modernTetrisQuickPlayHandler The handler for Modern Tetris quick play queue.
     * @param nesQuickPlayHandler          The handler for NES Tetris quick play queue.
     * @param lobbiesHandler               The handler for managing private lobbies.
     */
    public MatchmakingHandler(Socket player, QuickPlayHandler modernTetrisQuickPlayHandler, QuickPlayHandler nesQuickPlayHandler, LobbiesHandler lobbiesHandler){
        this.player = player;
        this.modernTetrisQuickPlayHandler = modernTetrisQuickPlayHandler;
        this.nesQuickPlayHandler = nesQuickPlayHandler;
        this.lobbiesHandler = lobbiesHandler;
    }

    /**
     * Executes the matchmaking logic.
     * <p>
     * Reads an integer from the client representing the {@link GameMode} ordinal,
     * then executes the logic corresponding to that mode.
     * </p>
     */
    @Override
    public void run() {
        int gameModeSelectedId;
        GameMode gameModeSelected;

        try {
            dis = new DataInputStream(player.getInputStream());
            dos = new DataOutputStream(player.getOutputStream());

            // 1. Read the requested game mode
            gameModeSelectedId = dis.readInt();

            // Validate the input range
            if (gameModeSelectedId < 0 || gameModeSelectedId >= GameMode.values().length) {
                closeSocket(player);
                return;
            }

            gameModeSelected = GameMode.values()[gameModeSelectedId];

            // 2. Route to the specific logic
            switch (gameModeSelected) {
                case MODERN_TETRIS_QUICK_PLAY:
                case NES_QUICK_PLAY:
                    quickMatchSearch(gameModeSelected);
                    break;
                case HOST_GAME:
                    hostGame();
                    break;
                case JOIN_GAME:
                    joinGame();
                    break;
            }
        }
        catch (IOException ioe) { closeSocket(player); }
    }

    /**
     * Generic logic for Quick Play matchmaking.
     * <p>
     * If no one is waiting, this client becomes the waiting player.
     * If someone is already waiting, a match is formed, and a {@link GameCommunicationHandler} is started.
     * </p>
     *
     * @param gameMode The specific Game Mode (Modern or NES) to use.
     */
    private void quickMatchSearch(GameMode gameMode) {
        QuickPlayHandler quickPlayHandler;
        Socket opponent;
        DataOutputStream opponentDos;
        DataInputStream opponentDis;
        boolean opponentIsOnline;
        boolean playerIsOnline = true;

        if (gameMode == MODERN_TETRIS_QUICK_PLAY) quickPlayHandler = modernTetrisQuickPlayHandler;
        else quickPlayHandler = nesQuickPlayHandler;

        // synchronized check to prevent race conditions is handled inside the handler methods
        opponent = quickPlayHandler.handlePlayer(player);

        if (opponent != null) {
            opponentIsOnline = true;

            // Check if player 1 is still online
            try {
                opponentDos = new DataOutputStream(opponent.getOutputStream());
                opponentDis = new DataInputStream(opponent.getInputStream());

                opponentDos.writeByte(0);
                opponentDos.flush();
                opponentDis.readByte();
            }
            catch (IOException ioe) {
                opponentIsOnline = false;
                closeSocket(opponent);
            }

            // Check if player 2 is still online
            try {
                dos.writeByte(0);
                dos.flush();
                dis.readByte();
            }
            catch (IOException ioe) {
                playerIsOnline = false;
                closeSocket(player);
            }

            if (opponentIsOnline && playerIsOnline) new GameCommunicationHandler(opponent, player).startCommunicationBetweenPlayers();
            else if (playerIsOnline) quickMatchSearch(gameMode);
            else if (opponentIsOnline) {
                setPlayer(opponent);
                quickMatchSearch(gameMode);
                new Thread(new MatchmakingHandler(opponent, modernTetrisQuickPlayHandler, quickPlayHandler, lobbiesHandler)).start();
            }
        }
    }

    /**
     * Handles the logic for hosting a private game lobby.
     * <p>
     * Requests a new Room ID from the {@link LobbiesHandler} and sends it back to the client.
     * The client socket is then stored in the lobby map waiting for a joiner.
     * </p>
     */
    private void hostGame() {
        // This synchronized is because there is a scenario where the lobby with roomId is created, then a player
        // joins the lobby (therefore removes the lobby from the lobbiesHandler), then a third player creates a
        // lobby with the same id and finally this player throws and IOException and removes the third player's
        // lobby from the lobbiesHandler. It wanted to remove the original lobby (the one the second player
        // is connected to), however, it removes the third's player lobby leaving him to wait for eternity for someone
        // to connect.
        synchronized (lobbiesHandler) {
            int roomId = lobbiesHandler.createLobby(player);

            try {
                dos.writeInt(roomId);
                dos.flush();
            } catch (IOException ioe) {
                // CRITICAL: Remove the lobby so players don't try to join a dead room.
                lobbiesHandler.getAndRemoveLobby(roomId);
                closeSocket(player);
            }
        }
    }

    /**
     * Handles the logic for joining a private game lobby.
     * <p>
     * Reads the Room ID requested by the client. If valid, retrieves the host's socket
     * and starts a {@link GameCommunicationHandler}. Sends a boolean confirmation to the client.
     * </p>
     */
    private void joinGame() {
        int roomId;
        Socket host;
        DataOutputStream hostDos;
        DataInputStream hostDis;
        boolean hostIsOnline;

        try {
            roomId = dis.readInt();

            // This synchronized is because there is a scenario where te player tries to connect to the lobby
            // but fails, in that case the lobby will be removed and added again, and we don't want for that lobby
            // id to be given to other players
            synchronized (lobbiesHandler) {
                if ((host = lobbiesHandler.getAndRemoveLobby(roomId)) != null) {
                    hostIsOnline = true;

                    // Check if room host is still online
                    try {
                        hostDos = new DataOutputStream(host.getOutputStream());
                        hostDis = new DataInputStream(host.getInputStream());

                        hostDos.writeByte(0);
                        hostDos.flush();
                        hostDis.readByte();
                    }
                    catch (IOException ioe) {
                        hostIsOnline = false;
                        closeSocket(host);
                    }

                    if (hostIsOnline) {
                        try {
                            // Notify client that room exists
                            dos.writeBoolean(true);
                            dos.flush();
                        }
                        catch (IOException ioe) {
                            // If player error happens communicating with player restore lobby
                            lobbiesHandler.restoreLobby(roomId, host);
                            closeSocket(player);
                        }

                        // Starts the game
                        startGame(host, player);
                    }
                    else {
                        // Notify client that room does not exist
                        dos.writeBoolean(false);
                        dos.flush();
                    }
                } else {
                    // Notify client that room does not exist
                    dos.writeBoolean(false);
                    dos.flush();
                }
            }
        }
        catch (IOException ioe) { closeSocket(player); }
    }

    /**
     * Starts the communications between players
     *
     * @param player1 The socket of the first player (e.g., host or first in queue).
     * @param player2 The socket of the second player (e.g., joiner or second in queue).
     */
    private void startGame(Socket player1, Socket player2) {
        new GameCommunicationHandler(player1, player2).startCommunicationBetweenPlayers();
    }

    /**
     * Sets the given player as the player to search a game for and updates the streams
     * (input and output) to be the one's associated with the given player's socket.
     *
     * @param player The socket of the player to search a game for
     */
    private void setPlayer(Socket player) {
        this.player = player;

        try {
            dis = new DataInputStream(player.getInputStream());
            dos = new DataOutputStream(player.getOutputStream());
        }
        catch (IOException ioe) { closeSocket(player); }
    }

    /**
     * Closes the client socket safely.
     */
    private void closeSocket(Socket player) {
        try { player.close(); }
        catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket after failing negotiation with client."); } // This should never happen, if it does your computer is broken sry
    }
}