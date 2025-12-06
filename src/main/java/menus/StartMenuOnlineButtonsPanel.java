package menus;

import main.MainPanel;
import javax.swing.*;
import java.awt.*;

public class StartMenuOnlineButtonsPanel extends JPanel {
    private MainPanel mainPanel;
    private StartMenuPanel startMenuPanel;

    public StartMenuOnlineButtonsPanel(MainPanel mainPanel, StartMenuPanel startMenuPanel) {
        super();
        this.mainPanel = mainPanel;
        this.startMenuPanel = startMenuPanel;

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
        add(btnJugarOnlineNES, gbc);

        ModernButton btnCrearSala = new ModernButton("HOST GAME");
        btnCrearSala.addActionListener(e -> playOnlineAsHost());
        add(btnCrearSala, gbc);

        ModernButton btnUnirseASala = new ModernButton("JOIN GAME");
        btnUnirseASala.addActionListener(e -> playOnlineAsClient());
        add(btnUnirseASala, gbc);

        // --- FILLER COMPONENT ---
        GridBagConstraints gbcFiller = new GridBagConstraints();
        gbcFiller.weighty = 1.0;
        gbcFiller.gridwidth = GridBagConstraints.REMAINDER;
        add(Box.createGlue(), gbcFiller);
    }

    private void playOnline() { mainPanel.connectToOnlineGame(); }
    private void playOnlineAsHost() { mainPanel.startOnlineGameAsHost(); }
    private void playOnlineAsClient() { mainPanel.startOnlineGameAsClient(); }
}