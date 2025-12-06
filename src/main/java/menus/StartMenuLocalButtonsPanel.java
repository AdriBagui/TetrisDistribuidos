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

        ModernButton btnUnJugadorLocalNES = new ModernButton("SOLO (NES)");
        btnUnJugadorLocalNES.addActionListener(e -> playOnePlayerNESLocalHandler());
        add(btnUnJugadorLocalNES, gbc);

        ModernButton btnUnJugadorLocal = new ModernButton("SOLO (MODERN)");
        btnUnJugadorLocal.addActionListener(e -> playOnePlayerTetrioLocalHandler());
        add(btnUnJugadorLocal, gbc);

        ModernButton btnDosJugadoresLocal = new ModernButton("1 VS 1 LOCAL");
        btnDosJugadoresLocal.addActionListener(e -> playTwoPlayerTetrioLocalHandler());
        add(btnDosJugadoresLocal, gbc);

        // --- FILLER COMPONENT ---
        // This component consumes all remaining vertical space, pushing buttons up
        GridBagConstraints gbcFiller = new GridBagConstraints();
        gbcFiller.weighty = 1.0;
        gbcFiller.gridwidth = GridBagConstraints.REMAINDER;
        add(Box.createGlue(), gbcFiller);
    }

    private void playOnePlayerNESLocalHandler() {
        mainPanel.startOnePlayerNESGame();
    }
    private void playOnePlayerTetrioLocalHandler() {
        mainPanel.startOnePlayerGame();
    }
    private void playTwoPlayerTetrioLocalHandler() {
        mainPanel.start1vs1LocalGame();
    }
}