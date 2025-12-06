package menus;

import main.MainPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    // Clase StartMenuOnlineButtonsPanel

    private void playOnlineAsHost() {
        // 1. Obtener la referencia a la ventana principal (JFrame) para centrar el diálogo
        JFrame ownerFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // 2. Crear y mostrar el diálogo modal
        CreateRoomDialog dialog = new CreateRoomDialog(ownerFrame);
        dialog.setVisible(true);

        // 3. El flujo del código se detiene aquí hasta que el diálogo se cierra.
        int roomNumber = dialog.getRoomNumber();

        if (roomNumber != -1) {
            // TODO: iniciar una sala con dicho código
        } else {
            // El usuario canceló la creación de la sala (roomNumber es -1)
            System.out.println("Room hosting cancelled.");
        }
    }

    private void playOnlineAsClient() { mainPanel.startOnlineGameAsClient(); }
}