package menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomMessageDialog extends JDialog {

    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 220);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font MESSAGE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Color ERROR_COLOR = new Color(255, 80, 80);
    private static final Color WARNING_COLOR = new Color(255, 200, 0);
    private static final Color INFO_COLOR = new Color(60, 130, 255);

    /**
     * We only use the ShowMessage, so we keep the constructor as private
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
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. Message ---
        JLabel messageLabel = new JLabel(message); // Permite salto de línea con HTML
        messageLabel.setFont(MESSAGE_FONT);
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

    private Color getBorderColor(int type) {
        switch (type) {
            case JOptionPane.ERROR_MESSAGE: return ERROR_COLOR;
            case JOptionPane.WARNING_MESSAGE: return WARNING_COLOR;
            default: return INFO_COLOR;
        }
    }

    private String getIconText(int type) {
        switch (type) {
            case JOptionPane.ERROR_MESSAGE: return " \u2718 ";
            case JOptionPane.WARNING_MESSAGE: return " \u26A0 ";
            default: return " \u2139 ";
        }
    }

    /**
     * Shows a message with the specified options
     * @param parent Parent of the message
     * @param message Text used for the message
     * @param title Dialog title
     * @param messageType
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