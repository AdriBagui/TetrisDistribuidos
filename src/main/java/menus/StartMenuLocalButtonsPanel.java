package menus;

import main.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuLocalButtonsPanel extends JPanel {
    private MainPanel mainPanel;
    private StartMenuPanel startMenuPanel;

    /**
     * Creates the Menu to select your local options
     * @param mainPanel {@code MainPanel} to modify
     * @param startMenuPanel {@link menus.StartMenuPanel} to create the layout
     */
    public StartMenuLocalButtonsPanel(MainPanel mainPanel, StartMenuPanel startMenuPanel) {
        super();

        this.mainPanel = mainPanel;
        this.startMenuPanel = startMenuPanel;

        // CONSTRAINTS FOR LAYOUT (Generados por Eclipse WindowsBuilder)
        GridBagConstraints gbc_localButtons = new GridBagConstraints();
        gbc_localButtons.ipady = 50;
        gbc_localButtons.ipadx = 50;
        gbc_localButtons.fill = GridBagConstraints.BOTH;
        gbc_localButtons.insets = new Insets(0, 0, 0, 5);
        gbc_localButtons.gridx = 0;
        gbc_localButtons.gridy = 1;
        startMenuPanel.add(this, gbc_localButtons);

        // LAYOUT
        GridBagLayout gbl_localButtons = new GridBagLayout();
        gbl_localButtons.columnWidths = new int[] {0};
        gbl_localButtons.rowHeights = new int[] {0};
        gbl_localButtons.columnWeights = new double[]{0.0};
        gbl_localButtons.rowWeights = new double[]{0.0, 0.0};
        setLayout(gbl_localButtons);

        // BOTÓN UN JUGADOR LOCAL
        JButton btnUnJugadorLocal = new JButton("SOLO");
        btnUnJugadorLocal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnUnJugadorLocal.setFont(new Font("Arial", Font.PLAIN, 15));
        GridBagConstraints gbc_btnUnJugadorLocal = new GridBagConstraints();
        gbc_btnUnJugadorLocal.ipadx = 75;
        gbc_btnUnJugadorLocal.ipady = 25;
        gbc_btnUnJugadorLocal.fill = GridBagConstraints.BOTH;
        gbc_btnUnJugadorLocal.insets = new Insets(0, 0, 12, 5);
        gbc_btnUnJugadorLocal.gridx = 0;
        gbc_btnUnJugadorLocal.gridy = 0;
        add(btnUnJugadorLocal, gbc_btnUnJugadorLocal);

        // BOTÓN DOS JUGADORES LOCAL
        JButton btnDosJugadoresLocal = new JButton("1 VS 1");
        btnDosJugadoresLocal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playLocalHandler();
            }
        });
        btnDosJugadoresLocal.setFont(new Font("Arial", Font.PLAIN, 15));
        GridBagConstraints gbc_btnDosJugadoresLocal = new GridBagConstraints();
        gbc_btnDosJugadoresLocal.insets = new Insets(0, 0, 0, 5);
        gbc_btnDosJugadoresLocal.ipady = 25;
        gbc_btnDosJugadoresLocal.ipadx = 75;
        gbc_btnDosJugadoresLocal.fill = GridBagConstraints.BOTH;
        gbc_btnDosJugadoresLocal.gridx = 0;
        gbc_btnDosJugadoresLocal.gridy = 1;
        add(btnDosJugadoresLocal, gbc_btnDosJugadoresLocal);
    }

    /**
     * Starts a 1vs1 local game
     */
    private void playLocalHandler() {
        mainPanel.start1vs1LocalGame();
    }
}
