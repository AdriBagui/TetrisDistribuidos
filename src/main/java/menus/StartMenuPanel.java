package menus;

import main.MainPanel;

import javax.swing.*;
import java.awt.*;

public class StartMenuPanel extends JPanel {
    private MainPanel mainPanel;

    public StartMenuPanel(MainPanel mainPanel) {
        super();
        this.mainPanel = mainPanel;

        setOpaque(false);

        // --- LAYOUT SETUP ---
        // We now have 3 rows:
        // Row 0: "TETRIS DISTRIBUIDOS" (Title)
        // Row 1: "LOCAL" / "ONLINE" (Sub-headers)
        // Row 2: Buttons
        GridBagLayout gbl_startMenu = new GridBagLayout();
        gbl_startMenu.columnWidths = new int[] {0, 0};
        gbl_startMenu.rowHeights = new int[] {0, 0, 0}; // 3 Rows
        gbl_startMenu.columnWeights = new double[]{1.0, 1.0};
        gbl_startMenu.rowWeights = new double[]{0.8, 0.0, 1.0, 0.2}; // Row 0 gets some space
        setLayout(gbl_startMenu);

        // --- 1. MAIN TITLE ---
        // We use a custom label (defined below) for the shadow effect
        ShadowLabel lblTitle = new ShadowLabel("TETRIS DISTRIBUIDOS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 48)); // Large Font
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc_title = new GridBagConstraints();
        gbc_title.gridwidth = 2; // Span across both columns
        gbc_title.insets = new Insets(30, 0, 10, 0); // Padding top/bottom
        gbc_title.fill = GridBagConstraints.HORIZONTAL;
        gbc_title.gridx = 0;
        gbc_title.gridy = 0; // Top row
        add(lblTitle, gbc_title);

        // --- 2. SUB-HEADERS (Local / Online) ---
        Font subHeaderFont = new Font("Segoe UI", Font.BOLD, 24);
        Color subHeaderColor = new Color(220, 220, 220); // Light gray

        JLabel lblLocal = new JLabel("LOCAL");
        lblLocal.setFont(subHeaderFont);
        lblLocal.setForeground(subHeaderColor);
        lblLocal.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc_lblLocal = new GridBagConstraints();
        gbc_lblLocal.insets = new Insets(10, 0, 10, 0);
        gbc_lblLocal.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblLocal.gridx = 0;
        gbc_lblLocal.gridy = 1; // Second row
        add(lblLocal, gbc_lblLocal);

        JLabel lblOnline = new JLabel("ONLINE");
        lblOnline.setFont(subHeaderFont);
        lblOnline.setForeground(subHeaderColor);
        lblOnline.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc_lblOnline = new GridBagConstraints();
        gbc_lblOnline.insets = new Insets(10, 0, 10, 0);
        gbc_lblOnline.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblOnline.gridx = 1;
        gbc_lblOnline.gridy = 1; // Second row
        add(lblOnline, gbc_lblOnline);

        // --- 3. BUTTON PANELS ---

        // Pass 'this' as the StartMenuPanel reference
        StartMenuLocalButtonsPanel localButtons = new StartMenuLocalButtonsPanel(mainPanel, this);
        GridBagConstraints gbc_localButtons = new GridBagConstraints();
        gbc_localButtons.fill = GridBagConstraints.BOTH;
        gbc_localButtons.gridx = 0;
        gbc_localButtons.gridy = 2; // Third row
        add(localButtons, gbc_localButtons);

        StartMenuOnlineButtonsPanel onlineButtons = new StartMenuOnlineButtonsPanel(mainPanel, this);
        GridBagConstraints gbc_onlineButtons = new GridBagConstraints();
        gbc_onlineButtons.fill = GridBagConstraints.BOTH;
        gbc_onlineButtons.gridx = 1;
        gbc_onlineButtons.gridy = 2; // Third row
        add(onlineButtons, gbc_onlineButtons);

        // --- 4. GLOBAL OPTIONS (SETTINGS) ---
        JPanel globalOptionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        globalOptionsPanel.setOpaque(false);

        ModernButton btnSettings = new ModernButton("SETTINGS");
        btnSettings.addActionListener(e -> mainPanel.openSettings());

        ModernButton btnExit = new ModernButton("EXIT");
        btnExit.addActionListener(e -> System.exit(0));

        globalOptionsPanel.add(btnSettings);
        globalOptionsPanel.add(Box.createHorizontalStrut(20));
        globalOptionsPanel.add(btnExit);

        GridBagConstraints gbc_global = new GridBagConstraints();
        gbc_global.gridwidth = 2; // Span both columns
        gbc_global.fill = GridBagConstraints.HORIZONTAL;
        gbc_global.gridx = 0;
        gbc_global.gridy = 3; // New Row 3
        gbc_global.insets = new Insets(20, 0, 20, 0); // Margin top/bottom

        add(globalOptionsPanel, gbc_global);
    }

    /**
     * Internal helper class to create text with a drop-shadow.
     * This makes the text readable regardless of the background image colors.
     */
    private class ShadowLabel extends JLabel {
        public ShadowLabel(String text) {
            super(text);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String text = getText();
            FontMetrics metrics = g2.getFontMetrics(getFont());
            int x = (getWidth() - metrics.stringWidth(text)) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();

            g2.setFont(getFont());

            // 1. Draw Shadow (Black, offset by 3 pixels)
            g2.setColor(new Color(0, 0, 0, 180));
            g2.drawString(text, x + 3, y + 3);

            // 2. Draw Main Text (White)
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);
        }
    }
}