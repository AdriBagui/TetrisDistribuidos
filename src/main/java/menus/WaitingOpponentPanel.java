package menus;

import distributedServices.ConnectionMode;
import distributedServices.ServerConnector;
import main.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ContentHandler;
import java.net.Socket;

public class WaitingOpponentPanel extends JPanel {
    private MainPanel mainPanel;
    JLabel roomIdLabel;
    private static final Color BACKGROUND_COLOR = new Color(22, 22, 22, 255);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font WAITING_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font ROOM_ID_FONT = new Font("Segoe UI", Font.BOLD, 20);


    /**
     * Creates the waiting screen for someone to connect
     * @param mainPanel Panel which contains it
     */
    public WaitingOpponentPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        setLayout(new GridBagLayout());
        this.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();

        // We use a spinner to give some sense of time passing
        JProgressBar spinner = new JProgressBar();
        spinner.setIndeterminate(true);
        spinner.setPreferredSize(new Dimension(80, 20));

        // Changes the spinner color
        spinner.setForeground(new Color(60, 130, 255));
        spinner.setBackground(new Color(35, 35, 35));

        gbc.gridy = 0;
        this.add(spinner, gbc);

        // Waiting message
        JLabel waitingLabel = new JLabel("Waiting for an opponent...");
        waitingLabel.setFont(WAITING_FONT);
        waitingLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 1;
        this.add(waitingLabel, gbc);

        roomIdLabel = new JLabel();
        roomIdLabel.setFont(ROOM_ID_FONT);
        roomIdLabel.setForeground(TEXT_COLOR);
        roomIdLabel.setVisible(false);

        gbc.gridy = 2;
        this.add(roomIdLabel, gbc);
    }

    /**
     * Connects to the server and starts an online game using the introduced game mode
     */
    public void connect(ConnectionMode gameMode){
        ServerConnector serverConnector = new ServerConnector();
        Socket boardsSocket = serverConnector.getSocket();

        if(boardsSocket != null){
            switch (gameMode){
                case QUICK_MATCH_MODE -> quickModeConnection(ConnectionMode.QUICK_MATCH_MODE, boardsSocket);
                case QUICK_MATCH_NES_MODE -> quickModeConnection(ConnectionMode.QUICK_MATCH_NES_MODE, boardsSocket);
                case HOST_GAME_MODE -> hostConnection(boardsSocket);
                case JOIN_GAME_MODE -> joinConnection(boardsSocket);
            }
        }
        else{
            CustomMessageDialog.showMessage(mainPanel,
                    "ERROR: Unable to connect to the server.",
                    "Unable to connect",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Connects to the server using the quick mode logic
     * @param gameMode Game mode used for the game
     */
    private void quickModeConnection(ConnectionMode gameMode, Socket boardsSocket){
        new Thread(){
            @Override
            public void run() {
                long seed;
                try{
                    DataInputStream dis = new DataInputStream(boardsSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(boardsSocket.getOutputStream());
                    dos.writeInt(gameMode.ordinal());
                    dos.flush();
                    seed = dis.readLong(); // Server sends the seed when a player is found
                    switch (gameMode){
                        case QUICK_MATCH_MODE:
                            mainPanel.startOnlineGame(seed,boardsSocket);
                            break;
                        case QUICK_MATCH_NES_MODE:
                            mainPanel.startOnlineNESGame(seed,boardsSocket);
                            break;
                    }

                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Connects to the server using the host mode logic
     */
    private void hostConnection(Socket boardsSocket){
        new Thread(){
            @Override
            public void run() {
                ConnectionMode gameMode = ConnectionMode.HOST_GAME_MODE;
                long seed;
                try{
                    DataInputStream dis = new DataInputStream(boardsSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(boardsSocket.getOutputStream());
                    dos.writeInt(gameMode.ordinal());
                    dos.flush();
                    roomIdLabel.setText("Room ID: " + dis.readInt());
                    roomIdLabel.setVisible(true);
                    seed = dis.readLong(); // Server sends the seed when a player is found
                    mainPanel.startOnlineGame(seed,boardsSocket);
                    roomIdLabel.setText("");
                    roomIdLabel.setVisible(false);
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Connects to the server using the join mode logic
     */
    private void joinConnection(Socket boardsSocket) {
        new Thread(){
            @Override
            public void run() {
                LobbySearchDialog lobbySearchDialog = new LobbySearchDialog(mainPanel);
                lobbySearchDialog.setVisible(true);
                ConnectionMode gameMode = ConnectionMode.JOIN_GAME_MODE;
                long seed;
                try{
                    DataInputStream dis = new DataInputStream(boardsSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(boardsSocket.getOutputStream());
                    dos.writeInt(gameMode.ordinal());
                    dos.flush();
                    // Send room ID
                    dos.writeInt(lobbySearchDialog.getRoomId());
                    dos.flush();
                    // Read if it exists
                    boolean exists = dis.readBoolean();
                    if(exists){
                        seed = dis.readLong(); // Server sends the seed when a player is found
                        mainPanel.startOnlineGame(seed,boardsSocket);
                    } else {
                        CustomMessageDialog.showMessage(mainPanel,
                                "WARNING: The room doesn't exist.",
                                "No room found",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }.start();
    }
}