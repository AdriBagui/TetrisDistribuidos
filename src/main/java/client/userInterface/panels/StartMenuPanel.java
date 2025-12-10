package client.userInterface.panels;

import server.GameMode;
import client.userInterface.components.ModernButton;

import javax.swing.*;
import java.awt.*;

import static client.userInterface.panels.MainPanel.*;

/**
 * The initial screen of the application, displaying the game title and navigation options.
 * <p>
 * This panel provides access to Single Player, Local Multiplayer, and Online Multiplayer modes,
 * as well as global settings and exit options.
 * </p>
 */
public class StartMenuPanel extends JPanel {
    private final MainPanel mainPanel;

    /**
     * Constructs the Start Menu with its layout and buttons.
     *
     * @param mainPanel The main controller panel, used to navigate to other screens.
     */
    public StartMenuPanel(MainPanel mainPanel) {
        super();
        this.mainPanel = mainPanel;

        setOpaque(false);

        // --- LAYOUT SETUP ---
        // Row 0: "TETRIS DISTRIBUIDOS" (Title)
        // Row 1: "LOCAL" / "ONLINE" (Sub-headers)
        // Row 2: Buttons
        // Row 3: Global Options
        GridBagLayout gbl_startMenu = new GridBagLayout();
        gbl_startMenu.columnWidths = new int[] {0, 0};
        gbl_startMenu.rowHeights = new int[] {0, 0, 0};
        gbl_startMenu.columnWeights = new double[]{1.0, 1.0};
        gbl_startMenu.rowWeights = new double[]{0.8, 0.0, 1.0, 0.2};
        setLayout(gbl_startMenu);

        // --- 1. MAIN TITLE ---
        ShadowLabel lblTitle = new ShadowLabel("TETRIS DISTRIBUIDOS");
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc_title = new GridBagConstraints();
        gbc_title.gridwidth = 2; // Span across both columns
        gbc_title.insets = new Insets(30, 0, 10, 0);
        gbc_title.fill = GridBagConstraints.HORIZONTAL;
        gbc_title.gridx = 0;
        gbc_title.gridy = 0;
        add(lblTitle, gbc_title);

        // --- 2. SUB-HEADERS ---

        JLabel lblLocal = new JLabel("LOCAL");
        lblLocal.setFont(SUBTITLE_FONT);
        lblLocal.setForeground(TEXT_COLOR);
        lblLocal.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc_lblLocal = new GridBagConstraints();
        gbc_lblLocal.insets = new Insets(10, 0, 10, 0);
        gbc_lblLocal.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblLocal.gridx = 0;
        gbc_lblLocal.gridy = 1;
        add(lblLocal, gbc_lblLocal);

        JLabel lblOnline = new JLabel("ONLINE");
        lblOnline.setFont(SUBTITLE_FONT);
        lblOnline.setForeground(TEXT_COLOR);
        lblOnline.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc_lblOnline = new GridBagConstraints();
        gbc_lblOnline.insets = new Insets(10, 0, 10, 0);
        gbc_lblOnline.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblOnline.gridx = 1;
        gbc_lblOnline.gridy = 1;
        add(lblOnline, gbc_lblOnline);

        // --- 3. BUTTON PANELS ---

        StartMenuLocalButtonsPanel localButtons = new StartMenuLocalButtonsPanel();
        GridBagConstraints gbc_localButtons = new GridBagConstraints();
        gbc_localButtons.fill = GridBagConstraints.BOTH;
        gbc_localButtons.gridx = 0;
        gbc_localButtons.gridy = 2;
        add(localButtons, gbc_localButtons);

        StartMenuOnlineButtonsPanel onlineButtons = new StartMenuOnlineButtonsPanel();
        GridBagConstraints gbc_onlineButtons = new GridBagConstraints();
        gbc_onlineButtons.fill = GridBagConstraints.BOTH;
        gbc_onlineButtons.gridx = 1;
        gbc_onlineButtons.gridy = 2;
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
        gbc_global.gridy = 3;
        gbc_global.insets = new Insets(20, 0, 20, 0);

        add(globalOptionsPanel, gbc_global);
    }

    /**
     * Internal helper class to create text with a drop-shadow.
     * This ensures the text remains readable against various background images.
     */
    private static class ShadowLabel extends JLabel {
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

    /**
     * Inner panel organizing the local game mode buttons.
     */
    private class StartMenuLocalButtonsPanel extends JPanel {

        public StartMenuLocalButtonsPanel() {
            super();
            setOpaque(false);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 50, 5, 50);
            gbc.ipady = 15;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.weighty = 0;

            // --- BUTTONS ---
            ModernButton btnSinglePlayerModernTetris = new ModernButton("SOLO");
            btnSinglePlayerModernTetris.addActionListener(e -> btnSinglePlayerModernTetrisHandler());
            add(btnSinglePlayerModernTetris, gbc);

            ModernButton btnTwoPlayersModernTetris = new ModernButton("1 VS 1");
            btnTwoPlayersModernTetris.addActionListener(e -> btnTwoPlayersModernTetrisHandler());
            add(btnTwoPlayersModernTetris, gbc);

            ModernButton btnSinglePlayerNES = new ModernButton("SOLO (NES)");
            btnSinglePlayerNES.addActionListener(e -> btnSinglePlayerNESHandler());
            add(btnSinglePlayerNES, gbc);

            ModernButton btnTwoPlayersNES = new ModernButton("   1 VS 1 (NES)   ");
            btnTwoPlayersNES.addActionListener(e -> btnTwoPlayersNESHandler());
            add(btnTwoPlayersNES, gbc);

            // --- FILLER ---
            GridBagConstraints gbcFiller = new GridBagConstraints();
            gbcFiller.weighty = 1.0;
            gbcFiller.gridwidth = GridBagConstraints.REMAINDER;
            add(Box.createGlue(), gbcFiller);
        }

        private void btnSinglePlayerModernTetrisHandler() { mainPanel.startSinglePlayerModernTetrisGame(); }
        private void btnTwoPlayersModernTetrisHandler() { mainPanel.startTwoPlayersModernTetrisGame(); }
        private void btnSinglePlayerNESHandler() { mainPanel.startSinglePlayerNESGame(); }
        private void btnTwoPlayersNESHandler() { mainPanel.startTwoPlayersNESGame(); }
    }

    /**
     * Inner panel organizing the online game mode buttons.
     */
    private class StartMenuOnlineButtonsPanel extends JPanel {
        public StartMenuOnlineButtonsPanel() {
            super();
            setOpaque(false);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 50, 5, 50);
            gbc.ipady = 15;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.weighty = 0;

            // --- BUTTONS ---
            ModernButton btnJugarOnline = new ModernButton("QUICK MATCH");
            btnJugarOnline.addActionListener(e -> playOnline());
            add(btnJugarOnline, gbc);

            ModernButton btnJugarOnlineNES = new ModernButton("QUICK MATCH (NES)");
            btnJugarOnlineNES.addActionListener(e -> playOnlineNES());
            add(btnJugarOnlineNES, gbc);

            ModernButton btnCrearSala = new ModernButton("HOST GAME");
            btnCrearSala.addActionListener(e -> playOnlineAsHost());
            add(btnCrearSala, gbc);

            ModernButton btnUnirseASala = new ModernButton("JOIN GAME");
            btnUnirseASala.addActionListener(e -> playOnlineAsClient());
            add(btnUnirseASala, gbc);

            // --- FILLER ---
            GridBagConstraints gbcFiller = new GridBagConstraints();
            gbcFiller.weighty = 1.0;
            gbcFiller.gridwidth = GridBagConstraints.REMAINDER;
            add(Box.createGlue(), gbcFiller);
        }

        private void playOnline() { mainPanel.connectToOnlineGame(GameMode.MODERN_TETRIS_QUICK_PLAY); }
        private void playOnlineNES() { mainPanel.connectToOnlineGame(GameMode.NES_QUICK_PLAY); }
        private void playOnlineAsHost() { mainPanel.connectToOnlineGame(GameMode.HOST_GAME); }
        private void playOnlineAsClient() { mainPanel.connectToOnlineGame(GameMode.JOIN_GAME); }
    }
}