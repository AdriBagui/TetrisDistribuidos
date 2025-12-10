package client;

import client.userInterface.dialogs.CustomMessageDialog;
import client.userInterface.dialogs.LobbySearchDialog;
import client.userInterface.panels.MainPanel;
import client.userInterface.panels.WaitingOpponentPanel;
import server.GameMode;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Handles the logic for connecting the client to the game server.
 * <p>
 * This class is responsible for establishing the initial socket connection, performing the
 * handshake/negotiation protocol (selecting game mode, hosting/joining lobbies), and
 * transitioning the UI state based on the connection result.
 * </p>
 */
public class ServerConnector {
    /**
     * The IP address binding for the server (localhost for local testing).
     */
    public static final String SERVER_IP = "localhost";

    private final MainPanel mainPanel;
    private final WaitingOpponentPanel waitingOpponentPanel;
    private String serverIp;
    private int serverPort;

    /**
     * Creates a ServerConnector.
     *
     * @param mainPanel            The main panel controller for navigation.
     * @param waitingOpponentPanel The panel used to display connection status messages.
     * @param serverIp             The target IP address of the server.
     * @param serverPort           The target port of the server.
     */
    public ServerConnector(MainPanel mainPanel, WaitingOpponentPanel waitingOpponentPanel, String serverIp, int serverPort) {
        this.mainPanel = mainPanel;
        this.waitingOpponentPanel = waitingOpponentPanel;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void setServerIp(String serverIp) { this.serverIp = serverIp; }
    public void setServerPort(int serverPort) { this.serverPort = serverPort; }

    /**
     * Initiates a connection to the server in a separate thread.
     * <p>
     * This method starts the handshake process based on the selected {@link GameMode}.
     * It updates the UI with progress messages ("Connecting...", "Waiting for opponent...")
     * and handles any connection errors.
     * </p>
     *
     * @param gameMode The game mode to initiate (Quick Play, Host, or Join).
     */
    public void connect(GameMode gameMode){
        new Thread(() -> {
            Socket boardsSocket = null;
            DataInputStream dis;
            DataOutputStream dos;
            boolean succesfulNegotiation = true;
            byte statusByte;
            long seed;

            waitingOpponentPanel.setMessage("Connecting to server...");

            try {
                boardsSocket = new Socket(serverIp, serverPort);
                waitingOpponentPanel.setMessage("Waiting for an opponent...");

                dis = new DataInputStream(boardsSocket.getInputStream());
                dos = new DataOutputStream(boardsSocket.getOutputStream());

                sendGameModeMessage(dos, gameMode);

                switch (gameMode) {
                    case HOST_GAME:
                        hostGameNegotiation(dis);
                        break;
                    case JOIN_GAME:
                        succesfulNegotiation = joinGameNegotiation(dis, dos);
                        break;
                }

                if (succesfulNegotiation) {
                    // Server keeps checking connection online by sending 0s as bytes until a player is found and then
                    // server sends a 1 as a byte to notify of successful connection.
                    statusByte = dis.readByte();
                    while (statusByte != 1) {
                        statusByte = dis.readByte();
                    }

                    // Server sends the seed when a player is found and this is also the start game signal
                    seed = dis.readLong();
                    mainPanel.startOnlineGame(seed, boardsSocket, gameMode);
                }
                else mainPanel.backToStartMenu();
            } catch (IOException ioe){
                try {
                    succesfulNegotiation = false;
                    if (boardsSocket != null) boardsSocket.close();
                    mainPanel.backToStartMenu();
                    serverDownErrorHandling();
                }
                catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket after failing quick play negotiation with server"); } // This should never happen, if it does your computer is broken sry
            }

        }).start();
    }

    /**
     * Sends the selected game mode ordinal to the server.
     *
     * @param dos      The output stream to the server.
     * @param gameMode The selected connection mode.
     * @throws IOException If the write fails.
     */
    private void sendGameModeMessage(DataOutputStream dos, GameMode gameMode) throws IOException {
        dos.writeInt(gameMode.ordinal());
        dos.flush();
    }

    /**
     * Handles the specific protocol for hosting a private game.
     * Waits for the server to return a generated Room ID and displays it.
     *
     * @param dis The input stream from the server.
     * @throws IOException If the read fails.
     */
    private void hostGameNegotiation(DataInputStream dis) throws IOException {
        int roomId = dis.readInt(); // Server sends the roomId when a lobby is created

        waitingOpponentPanel.setRoomId(roomId);
        waitingOpponentPanel.setRoomIdVisibility(true);
    }

    /**
     * Handles the specific protocol for joining a private game.
     * Prompts the user for a Room ID, sends it to the server, and checks if it exists.
     *
     * @param dis The input stream from the server.
     * @param dos The output stream to the server.
     * @return {@code true} if the lobby exists and join was successful, {@code false} otherwise.
     * @throws IOException If network IO fails.
     */
    private boolean joinGameNegotiation(DataInputStream dis, DataOutputStream dos) throws IOException {
        LobbySearchDialog lobbySearchDialog;
        boolean lobbyExists;

        // Show input dialog on the EDT (Event Dispatch Thread) would be ideal,
        // but since we are in a background thread, we must be careful.
        // Dialogs block the current thread if modal, but here we are in a worker thread.
        // Swing components should theoretically be created on EDT.
        lobbySearchDialog = new LobbySearchDialog(mainPanel);
        lobbySearchDialog.setVisible(true);

        // Send room ID
        dos.writeInt(lobbySearchDialog.getRoomId());
        dos.flush();

        // Read if it exists
        lobbyExists = dis.readBoolean();

        if (!lobbyExists && lobbySearchDialog.getRoomId() != -1) {
            CustomMessageDialog.showMessage(mainPanel,
                    "WARNING: The room doesn't exist.",
                    "No room found",
                    JOptionPane.WARNING_MESSAGE);
        }

        return lobbyExists;
    }

    /**
     * Displays an error message when the server cannot be reached.
     */
    private void serverDownErrorHandling() {
        CustomMessageDialog.showMessage(mainPanel,
                "ERROR: Unable to connect to the server.",
                "Unable to connect",
                JOptionPane.ERROR_MESSAGE);
        mainPanel.backToStartMenu();
    }
}