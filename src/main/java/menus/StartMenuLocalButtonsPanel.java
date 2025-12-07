package menus;

import main.MainPanel;
import javax.swing.*;
import java.awt.*;

public class StartMenuLocalButtonsPanel extends JPanel {
    private MainPanel mainPanel;
    private StartMenuPanel startMenuPanel;

    public StartMenuLocalButtonsPanel(MainPanel mainPanel, StartMenuPanel startMenuPanel) {
        super();
        this.mainPanel = mainPanel;
        this.startMenuPanel = startMenuPanel;

        setOpaque(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // End row
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 50, 5, 50); // Reduced top/bottom margin slightly
        gbc.ipady = 15;
        gbc.anchor = GridBagConstraints.NORTH; // Anchor components to the top
        gbc.weighty = 0; // Buttons should not stretch vertically

        // --- BUTTONS ---
        ModernButton btnSinglePlayerTetrio = new ModernButton("SOLO");
        btnSinglePlayerTetrio.addActionListener(e -> btnSinglePlayerTetrioHandler());
        add(btnSinglePlayerTetrio, gbc);

        ModernButton btnTwoPlayersTetrio = new ModernButton("1 VS 1");
        btnTwoPlayersTetrio.addActionListener(e -> btnTwoPlayersTetrioHandler());
        add(btnTwoPlayersTetrio, gbc);

        ModernButton btnSinglePlayerNES = new ModernButton("SOLO (NES)");
        btnSinglePlayerNES.addActionListener(e -> btnSinglePlayerNESHandler());
        add(btnSinglePlayerNES, gbc);

        ModernButton btnTwoPlayersNES = new ModernButton("   1 VS 1 (NES)   ");
        btnTwoPlayersNES.addActionListener(e -> btnTwoPlayersNESHandler());
        add(btnTwoPlayersNES, gbc);

        // --- FILLER COMPONENT ---
        // This component consumes all remaining vertical space, pushing buttons up
        GridBagConstraints gbcFiller = new GridBagConstraints();
        gbcFiller.weighty = 1.0;
        gbcFiller.gridwidth = GridBagConstraints.REMAINDER;
        add(Box.createGlue(), gbcFiller);
    }

    private void btnSinglePlayerTetrioHandler() {
        mainPanel.startSinglePlayerTetrioGame();
    }
    private void btnTwoPlayersTetrioHandler() {
        mainPanel.startTwoPlayersTetrioGame();
    }
    private void btnSinglePlayerNESHandler() {
        mainPanel.startSinglePlayerNESGame();
    }
    private void btnTwoPlayersNESHandler() {
        mainPanel.startTwoPlayersNESGame();
    }
}