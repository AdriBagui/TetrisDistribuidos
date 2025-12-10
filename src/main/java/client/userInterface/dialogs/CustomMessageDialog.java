package client.userInterface.dialogs;

import client.userInterface.components.ModernButton;

import javax.swing.*;
import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

/**
 * A custom modal dialog for displaying messages to the user.
 * <p>
 * This class serves as a stylistically consistent replacement for the standard {@link JOptionPane}.
 * It matches the application's dark theme and provides a unified look for errors, warnings,
 * and information messages.
 * </p>
 */
public class CustomMessageDialog extends JDialog {

    /**
     * Private constructor to enforce the use of the static {@link #showMessage} method.
     * Initializes the dialog's UI components based on the message type.
     *
     * @param owner       The {@code JFrame} owner of this dialog, blocking input to it while open.
     * @param title       The title string to appear at the top of the dialog.
     * @param message     The body text of the message (supports HTML for formatting).
     * @param messageType The type of message (e.g., {@link JOptionPane#ERROR_MESSAGE}), which determines the icon and color scheme.
     */
    private CustomMessageDialog(JFrame owner, String title, String message, int messageType) {
        super(owner, title, true);

        setUndecorated(true); // Removes border
        setResizable(false);

        // --- Main panel ---
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createLineBorder(getBorderColor(messageType), 4));

        // --- 1. Title and icon ---
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        // Icon config
        JLabel iconLabel = new JLabel(getIconText(messageType));
        iconLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        iconLabel.setForeground(getBorderColor(messageType));
        headerPanel.add(iconLabel, BorderLayout.WEST);

        // Dialog title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. Message ---
        JLabel messageLabel = new JLabel(message); // Permite salto de línea con HTML
        messageLabel.setFont(DEFAULT_FONT);
        messageLabel.setForeground(TEXT_COLOR);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        mainPanel.add(messageLabel, BorderLayout.CENTER);

        // --- 3. Accept button ---
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setOpaque(false);
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        JButton okButton = new ModernButton("OK");
        okButton.setPreferredSize(new Dimension(100, 40));
        okButton.addActionListener(e -> dispose());

        southPanel.add(okButton);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(owner);
        getRootPane().setDefaultButton(okButton);
    }

    /**
     * Determines the border and icon color based on the message severity.
     *
     * @param type The message type constant (e.g., {@link JOptionPane#ERROR_MESSAGE}).
     * @return The {@link Color} associated with that severity.
     */
    private Color getBorderColor(int type) {
        Color borderColor;

        switch (type) {
            case JOptionPane.ERROR_MESSAGE:
                borderColor =ERROR_COLOR;
                break;
            case JOptionPane.WARNING_MESSAGE:
                borderColor = WARNING_COLOR;
                break;
            default:
                borderColor = INFO_COLOR;
                break;
        }

        return borderColor;
    }

    /**
     * Returns a Unicode character string representing an icon for the message type.
     *
     * @param type The message type constant.
     * @return A string containing a Unicode symbol (e.g., checkmark, warning sign).
     */
    private String getIconText(int type) {
        String message;

        switch (type) {
            case JOptionPane.ERROR_MESSAGE:
                message = " ✘ ";
                break;
            case JOptionPane.WARNING_MESSAGE:
                message = " ⚠ ";
                break;
            default:
                message = " ℹ ";
                break;
        }

        return message;
    }

    /**
     * Displays a modal custom message dialog.
     * <p>
     * This is the primary entry point for using the dialog. It automatically finds the
     * parent frame to ensure the dialog is centered and blocks interaction correctly.
     * </p>
     *
     * @param parent      The component used to locate the parent window.
     * @param message     The text content of the message.
     * @param title       The title of the dialog window.
     * @param messageType The {@link JOptionPane} constant representing the message type (e.g., ERROR, WARNING, INFORMATION).
     */
    public static void showMessage(Component parent, String message, String title, int messageType) {
        // We look for the main frame
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(parent);
        if (owner == null) {
            owner = (JFrame) parent;
        }

        CustomMessageDialog dialog = new CustomMessageDialog(owner, title, message, messageType);
        dialog.setVisible(true);
    }
}