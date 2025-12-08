package client.userInterface.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

import static client.userInterface.panels.MainPanel.BACKGROUND_COLOR;

/**
 * A custom {@link BasicScrollBarUI} to provide a modern, dark-themed scrollbar.
 * <p>
 * This implementation removes the scroll buttons and customizes the track
 * and thumb painting for a sleek, minimal appearance.
 * </p>
 */
public class ModernScrollBar extends BasicScrollBarUI {
    private final Dimension d = new Dimension();

    /**
     * Creates the decrease (up/left) button.
     * Overridden to return a zero-size button, effectively hiding it.
     *
     * @param orientation The orientation of the button.
     * @return A zero-size {@link JButton}.
     */
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() { return d; }
        };
    }

    /**
     * Creates the increase (down/right) button.
     * Overridden to return a zero-size button, effectively hiding it.
     *
     * @param orientation The orientation of the button.
     * @return A zero-size {@link JButton}.
     */
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() { return d; }
        };
    }

    /**
     * Paints the scrollbar track.
     *
     * @param g           The {@link Graphics} context.
     * @param c           The component being painted.
     * @param trackBounds The boundaries of the track.
     */
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    /**
     * Paints the scrollbar thumb (the draggable part).
     * Renders a rounded rectangle with antialiasing.
     *
     * @param g           The {@link Graphics} context.
     * @param c           The component being painted.
     * @param thumbBounds The boundaries of the thumb.
     */
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(80, 80, 80));
        g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);
        g2.dispose();
    }
}