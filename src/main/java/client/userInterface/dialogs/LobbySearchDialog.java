package client.userInterface.dialogs;

import client.userInterface.panels.MainPanel;
import client.userInterface.components.ModernButton;

import javax.swing.*;
import java.awt.*;

public class LobbySearchDialog extends JDialog {
    private MainPanel mainPanel;
    private int roomId = -1;
    private JTextField roomNumberField;
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 220);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private final Dimension BIG_BUTTON_SIZE = new Dimension(220, 50);

    /**
     * Creates a RoomDialog to create a room
     * @param mainPanel {@code JFrame} owner of the dialog
     */
    public LobbySearchDialog(MainPanel mainPanel) {
        // Locks the dialog
        super((JFrame) SwingUtilities.getWindowAncestor(mainPanel),"Lobby Search", true);

        this.mainPanel = mainPanel;

        setLayout(new BorderLayout());
        setSize(450, 250);
        setLocationRelativeTo(mainPanel);
        setResizable(false);

        // Main panel
        JPanel panel = createInputView();
        add(panel, BorderLayout.CENTER);
    }

    /**
     * Creates the view to enter the room code
     * @return {@code JPanel} where you enter the room code
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
        instructionLabel.setFont(LABEL_FONT);

        // Serialized room number field
        roomNumberField = new JTextField(15);
        roomNumberField.setFont(LABEL_FONT);
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
        cancelButton.addActionListener(e -> {
            dispose();
        });

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        getRootPane().setDefaultButton(createButton);

        return mainPanel;
    }

    /**
     * Attempts to join the room in the TextField
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

    public int getRoomId() {
        return roomId;
    }
}