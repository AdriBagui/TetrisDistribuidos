package client.userInterface.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static client.userInterface.panels.MainPanel.BOLD_DEFAULT_FONT;

/**
 * A custom {@link JButton} implementation with a modern look and feel.
 * <p>
 * This button features transparent backgrounds with hover and press effects,
 * rounded corners, and a custom border style designed for dark themes.
 * </p>
 */
public class ModernButton extends JButton {
    private static final Color HOVER_COLOR = new Color(100, 100, 100, 150);
    private static final Color NORMAL_COLOR = new Color(0, 0, 0, 150);
    private static final Color PRESSED_COLOR = new Color(50, 50, 50, 200);

    /**
     * Constructs a new ModernButton with the specified text.
     *
     * @param text The text to display on the button.
     */
    public ModernButton(String text) {
        super(text);
        super.setContentAreaFilled(false);
        setFocusPainted(false); // Remove the ugly focus border
        setBorderPainted(false); // Remove the standard border
        setOpaque(false);

        // Font styling
        setFont(BOLD_DEFAULT_FONT);
        setForeground(Color.WHITE);

        // Interaction
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_COLOR);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(NORMAL_COLOR);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(PRESSED_COLOR);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(HOVER_COLOR);
            }
        });
    }

    /**
     * Paints the component with custom graphics (rounded rectangle and dynamic colors).
     *
     * @param g The {@link Graphics} context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        if (getModel().isPressed()) {
            g2.setColor(PRESSED_COLOR);
        } else if (getModel().isRollover()) {
            g2.setColor(HOVER_COLOR);
        } else {
            g2.setColor(NORMAL_COLOR);
        }

        // Rounded Rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Optional: Add a border
        g2.setColor(new Color(255, 255, 255, 100)); // Semi-transparent white border
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        g2.dispose();

        super.paintComponent(g);
    }
}