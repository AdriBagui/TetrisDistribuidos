package client.userInterface.panels;

import server.Server;
import client.userInterface.dialogs.CustomMessageDialog;
import client.userInterface.dialogs.LobbySearchDialog;
import server.ConnectionMode;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WaitingOpponentPanel extends JPanel {
    private MainPanel mainPanel;
    JLabel roomIdLabel;
    JLabel waitingLabel;
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
        waitingLabel = new JLabel("Connecting to server...");
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

    public void setMessage(String message) { waitingLabel.setText(message); }
    public void setRoomId(int roomId) { roomIdLabel.setText("Room ID: " + roomId); }
    public void setRoomIdVisibility(boolean visible) { roomIdLabel.setVisible(visible); }
}