package client;

import client.userInterface.dialogs.CustomMessageDialog;
import client.userInterface.dialogs.LobbySearchDialog;
import client.userInterface.panels.MainPanel;
import client.userInterface.panels.WaitingOpponentPanel;
import server.ConnectionMode;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnector {
    private final MainPanel mainPanel;
    private final WaitingOpponentPanel waitingOpponentPanel;
    private String serverIp;
    private int serverPort;

    public ServerConnector(MainPanel mainPanel, WaitingOpponentPanel waitingOpponentPanel, String serverIp, int serverPort) {
        this.mainPanel = mainPanel;
        this.waitingOpponentPanel = waitingOpponentPanel;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void setServerIp(String serverIp) { this.serverIp = serverIp; }
    public void setServerPort(int serverPort) { this.serverPort = serverPort; }

    /**
     * Connects to the server and starts an online game using the introduced game mode
     */
    public void connect(ConnectionMode gameMode){
        new Thread() {
            @Override
            public void run() {
                Socket boardsSocket = null;
                DataInputStream dis;
                DataOutputStream dos;
                boolean succesfulNegotiation = true;
                long seed;

                waitingOpponentPanel.setMessage("Connecting to server...");

                try {
                    boardsSocket = new Socket(serverIp, serverPort);
                    waitingOpponentPanel.setMessage("Waiting for an opponent...");

                    dis = new DataInputStream(boardsSocket.getInputStream());
                    dos = new DataOutputStream(boardsSocket.getOutputStream());

                    sendGameModeMessage(dos, gameMode);

                    switch (gameMode) {
                        case HOST_GAME -> hostGameNegotiation(dis);
                        case JOIN_GAME -> succesfulNegotiation = joinGameNegotiation(dis, dos);
                    }

                    if (succesfulNegotiation) {
                        seed = dis.readLong(); // Server sends the seed when a player is found and this is also the start game signal
                        mainPanel.startOnlineGame(seed, boardsSocket, gameMode);
                    }
                } catch (IOException ioe){
                    try {
                        succesfulNegotiation = false;
                        if (boardsSocket != null) boardsSocket.close();
                        serverDownErrorHandling();
                    }
                    catch (IOException e) { System.out.println("FATAL ERROR while trying to close socket after failing quick play negotiation with server"); } // This should never happen, if it does your computer is broken sry
                }

            }
        }.start();
    }

    private void sendGameModeMessage(DataOutputStream dos, ConnectionMode gameMode) throws IOException {
        dos.writeInt(gameMode.ordinal());
        dos.flush();
    }

    private void hostGameNegotiation(DataInputStream dis) throws IOException {
        int roomId = dis.readInt(); // Server sends the roomId when a lobby is created

        waitingOpponentPanel.setRoomId(roomId);
        waitingOpponentPanel.setRoomIdVisibility(true);
    }

    private boolean joinGameNegotiation(DataInputStream dis, DataOutputStream dos) throws IOException {
        LobbySearchDialog lobbySearchDialog;
        boolean lobbyExists;

        // Show input dialog
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

    private void serverDownErrorHandling() {
        CustomMessageDialog.showMessage(mainPanel,
                "ERROR: Unable to connect to the server.",
                "Unable to connect",
                JOptionPane.ERROR_MESSAGE);
        mainPanel.backToStartMenu();
    }
}
