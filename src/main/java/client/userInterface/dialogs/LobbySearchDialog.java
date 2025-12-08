package client.userInterface.dialogs;

import client.userInterface.panels.MainPanel;
import client.userInterface.components.ModernButton;

import javax.swing.*;
import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

/**
 * A dialog allowing the user to enter a specific Room ID to join an online lobby.
 * <p>
 * This modal dialog captures integer input from the user, validates it, and stores it
 * for the {@link client.ServerConnector} to use during the connection negotiation.
 * </p>
 */
public class LobbySearchDialog extends JDialog {
    private int roomId = -1;
    private JTextField roomNumberField;

    /**
     * Creates a new LobbySearchDialog.
     *
     * @param mainPanel The {@link MainPanel} acting as the owner of this dialog.
     */
    public LobbySearchDialog(MainPanel mainPanel) {
        // Locks the dialog
        super((JFrame) SwingUtilities.getWindowAncestor(mainPanel),"Lobby Search", true);

        setLayout(new BorderLayout());
        setSize(450, 250);
        setLocationRelativeTo(mainPanel);
        setResizable(false);

        // Main panel
        JPanel panel = createInputView();
        add(panel, BorderLayout.CENTER);
    }

    /**
     * Creates the internal view panel containing instructions, the input field, and action buttons.
     *
     * @return The constructed {@code JPanel}.
     */
    private JPanel createInputView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Entry panel
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel instructionLabel = new JLabel("Enter the desired Room Number (ID):");
        instructionLabel.setForeground(TEXT_COLOR);
        instructionLabel.setFont(MEDIUM_MESSAGE_FONT);

        // Serialized room number field
        roomNumberField = new JTextField(15);
        roomNumberField.setFont(MEDIUM_MESSAGE_FONT);
        roomNumberField.setBackground(new Color(35, 35, 35));
        roomNumberField.setForeground(TEXT_COLOR);
        roomNumberField.setCaretColor(TEXT_COLOR);
        roomNumberField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));

        inputPanel.add(instructionLabel);
        inputPanel.add(roomNumberField);
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        JButton createButton = new ModernButton("JOIN ROOM");
        JButton cancelButton = new ModernButton("CANCEL");

        // Listeners
        createButton.addActionListener(e -> attemptToJoinRoom());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        getRootPane().setDefaultButton(createButton);

        return mainPanel;
    }

    /**
     * Validates the input from the text field and closes the dialog if successful.
     * <p>
     * If the input is not a valid positive integer, an error message is shown
     * and the dialog remains open.
     * </p>
     */
    private void attemptToJoinRoom() {
        String text = roomNumberField.getText().trim();

        try {
            int number = Integer.parseInt(text);

            if (number < 0) {
                CustomMessageDialog.showMessage(this,
                        "ERROR: Room ID must be a positive number.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                roomNumberField.requestFocusInWindow();
                return;
            }

            roomId = number;
            dispose();

        } catch (NumberFormatException ex) {
            CustomMessageDialog.showMessage(this,
                    "ERROR: Please enter only valid numbers.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            roomNumberField.requestFocusInWindow();
        }
    }

    /**
     * Gets the Room ID entered by the user.
     *
     * @return The valid Room ID entered, or -1 if the dialog was cancelled or no valid input was provided.
     */
    public int getRoomId() {
        return roomId;
    }
}