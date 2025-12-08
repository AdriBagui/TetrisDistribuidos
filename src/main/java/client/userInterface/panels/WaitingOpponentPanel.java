package client.userInterface.panels;

import javax.swing.*;
import java.awt.*;

import static client.userInterface.panels.MainPanel.HEADER_FONT;
import static client.userInterface.panels.MainPanel.MEDIUM_MESSAGE_FONT;
import static client.userInterface.panels.MainPanel.TEXT_COLOR;

/**
 * A panel displayed while the client is negotiating a connection with the server.
 * <p>
 * This screen shows a loading spinner and status messages (e.g., "Connecting...",
 * "Waiting for opponent..."). If the user is hosting a game, it also displays the
 * generated Room ID.
 * </p>
 */
public class WaitingOpponentPanel extends JPanel {
    JLabel roomIdLabel;
    JLabel waitingLabel;
    private static final Color BACKGROUND_COLOR = new Color(22, 22, 22, 255);

    /**
     * Creates the waiting screen UI.
     *
     */
    public WaitingOpponentPanel() {
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
        waitingLabel.setFont(HEADER_FONT);
        waitingLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 1;
        this.add(waitingLabel, gbc);

        roomIdLabel = new JLabel();
        roomIdLabel.setFont(MEDIUM_MESSAGE_FONT);
        roomIdLabel.setForeground(TEXT_COLOR);
        roomIdLabel.setVisible(false);

        gbc.gridy = 2;
        this.add(roomIdLabel, gbc);
    }

    /**
     * Updates the status message displayed to the user.
     *
     * @param message The new status text (e.g., "Waiting for an opponent...").
     */
    public void setMessage(String message) { waitingLabel.setText(message); }

    /**
     * Sets the Room ID text to be displayed when hosting a lobby.
     *
     * @param roomId The unique identifier for the created room.
     */
    public void setRoomId(int roomId) { roomIdLabel.setText("Room ID: " + roomId); }

    /**
     * Toggles the visibility of the Room ID label.
     * Should only be true when the player is hosting a private game.
     *
     * @param visible {@code true} to show the ID, {@code false} to hide it.
     */
    public void setRoomIdVisibility(boolean visible) { roomIdLabel.setVisible(visible); }
}