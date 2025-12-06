package menus;

import main.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuOnlineButtonsPanel extends JPanel {
    private MainPanel mainPanel;
    private StartMenuPanel startMenuPanel;

    /**
     * Creates the Menu to select your online options
     * @param mainPanel {@code MainPanel} to modify
     * @param startMenuPanel {@link menus.StartMenuPanel} to create the layout
     */
    public StartMenuOnlineButtonsPanel(MainPanel mainPanel, StartMenuPanel startMenuPanel) {
        super();

        this.mainPanel = mainPanel;
        this.startMenuPanel = startMenuPanel;

        // CONSTRAINTS FOR LAYOUT (Generados por Eclipse WindowsBuilder)
        GridBagConstraints gbc_onlineButtons = new GridBagConstraints();
        gbc_onlineButtons.ipady = 50;
        gbc_onlineButtons.ipadx = 50;
        gbc_onlineButtons.fill = GridBagConstraints.BOTH;
        gbc_onlineButtons.gridx = 1;
        gbc_onlineButtons.gridy = 1;
        startMenuPanel.add(this, gbc_onlineButtons);

        // LAYOUT
        GridBagLayout gbl_onlineButtons = new GridBagLayout();
        gbl_onlineButtons.columnWidths = new int[] {0};
        gbl_onlineButtons.rowHeights = new int[] {0, 0, 0};
        gbl_onlineButtons.columnWeights = new double[]{0.0};
        gbl_onlineButtons.rowWeights = new double[]{0.0, 0.0, 0.0};
        setLayout(gbl_onlineButtons);

        // BOTÓN JUGAR ONLINE
        JButton btnJugarOnline = new JButton("JUGAR ONLINE");
        btnJugarOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { playOnline(); }
        });
        btnJugarOnline.setFont(new Font("Arial", Font.PLAIN, 15));
        GridBagConstraints gbc_btnJugarOnline = new GridBagConstraints();
        gbc_btnJugarOnline.fill = GridBagConstraints.BOTH;
        gbc_btnJugarOnline.ipadx = 50;
        gbc_btnJugarOnline.ipady = 25;
        gbc_btnJugarOnline.insets = new Insets(0, 0, 12, 5);
        gbc_btnJugarOnline.gridx = 0;
        gbc_btnJugarOnline.gridy = 0;
        add(btnJugarOnline, gbc_btnJugarOnline);

        // BOTÓN CREAR SALA
        JButton btnCrearSala = new JButton("CREAR SALA");
        btnCrearSala.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playOnlineAsHost();
            }
        });
        btnCrearSala.setFont(new Font("Arial", Font.PLAIN, 15));
        GridBagConstraints gbc_btnCrearSala = new GridBagConstraints();
        gbc_btnCrearSala.fill = GridBagConstraints.BOTH;
        gbc_btnCrearSala.ipady = 25;
        gbc_btnCrearSala.ipadx = 50;
        gbc_btnCrearSala.insets = new Insets(0, 0, 12, 5);
        gbc_btnCrearSala.gridx = 0;
        gbc_btnCrearSala.gridy = 1;
        add(btnCrearSala, gbc_btnCrearSala);

        JButton btnUnirseASala = new JButton("UNIRSE A SALA");
        btnUnirseASala.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playOnlineAsClient();
            }
        });
        btnUnirseASala.setFont(new Font("Arial", Font.PLAIN, 15));
        GridBagConstraints gbc_btnUnirseASala = new GridBagConstraints();
        gbc_btnUnirseASala.fill = GridBagConstraints.BOTH;
        gbc_btnUnirseASala.ipady = 25;
        gbc_btnUnirseASala.ipadx = 50;
        gbc_btnUnirseASala.insets = new Insets(0, 0, 0, 5);
        gbc_btnUnirseASala.gridx = 0;
        gbc_btnUnirseASala.gridy = 2;
        add(btnUnirseASala, gbc_btnUnirseASala);
    }

    /**
     * Connects to a random opponent online
     */
    private void playOnline() { mainPanel.connectToOnlineGame(); }

    /**
     * Creates a room for someone to join and play against
     */
    private void playOnlineAsHost() {
        mainPanel.startOnlineGameAsHost();
    }

    /**
     * Looks for a room to join and play against
     */
    private void playOnlineAsClient() {
        mainPanel.startOnlineGameAsClient();
    }
}
