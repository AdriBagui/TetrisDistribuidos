package client.userInterface.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static client.userInterface.panels.MainPanel.BOLD_DEFAULT_FONT;

public class ModernButton extends JButton {
    private Color hoverColor = new Color(100, 100, 100, 150);
    private Color normalColor = new Color(0, 0, 0, 150);
    private Color pressedColor = new Color(50, 50, 50, 200);

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
                setBackground(hoverColor);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        if (getModel().isPressed()) {
            g2.setColor(pressedColor);
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
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
