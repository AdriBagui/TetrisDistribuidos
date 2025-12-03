package src.menus;

import src.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuPanel extends JPanel {
    private MainPanel mainPanel;

    public StartMenuPanel(MainPanel mainPanel) {
        super();

        this.mainPanel = mainPanel;

        GridBagLayout gbl_startMenu = new GridBagLayout();
        gbl_startMenu.columnWidths = new int[] {0};
        gbl_startMenu.rowHeights = new int[] {0};
        gbl_startMenu.columnWeights = new double[]{1.0, 1.0};
        gbl_startMenu.rowWeights = new double[]{0.0, 1.0};
        setLayout(gbl_startMenu);

        JLabel lblLocal = new JLabel("LOCAL");
        lblLocal.setFont(new Font("Arial", Font.PLAIN, 30));
        lblLocal.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblLocal = new GridBagConstraints();
        gbc_lblLocal.ipady = 50;
        gbc_lblLocal.ipadx = 50;
        gbc_lblLocal.fill = GridBagConstraints.HORIZONTAL;
        gbc_lblLocal.insets = new Insets(25, 0, 5, 5);
        gbc_lblLocal.gridx = 0;
        gbc_lblLocal.gridy = 0;
        add(lblLocal, gbc_lblLocal);

        JLabel lblOnline = new JLabel("ONLINE");
        lblOnline.setFont(new Font("Arial", Font.PLAIN, 30));
        lblOnline.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints gbc_lblOnline = new GridBagConstraints();
        gbc_lblOnline.ipady = 50;
        gbc_lblOnline.ipadx = 50;
        gbc_lblOnline.fill = GridBagConstraints.BOTH;
        gbc_lblOnline.insets = new Insets(25, 0, 5, 0);
        gbc_lblOnline.gridx = 1;
        gbc_lblOnline.gridy = 0;
        add(lblOnline, gbc_lblOnline);

        JPanel localButtons = new JPanel();
        GridBagConstraints gbc_localButtons = new GridBagConstraints();
        gbc_localButtons.ipady = 50;
        gbc_localButtons.ipadx = 50;
        gbc_localButtons.fill = GridBagConstraints.BOTH;
        gbc_localButtons.insets = new Insets(0, 0, 0, 5);
        gbc_localButtons.gridx = 0;
        gbc_localButtons.gridy = 1;
        add(localButtons, gbc_localButtons);
        GridBagLayout gbl_localButtons = new GridBagLayout();
        gbl_localButtons.columnWidths = new int[] {0};
        gbl_localButtons.rowHeights = new int[] {0};
        gbl_localButtons.columnWeights = new double[]{0.0};
        gbl_localButtons.rowWeights = new double[]{0.0, 0.0};
        localButtons.setLayout(gbl_localButtons);

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
        localButtons.add(btnUnJugadorLocal, gbc_btnUnJugadorLocal);

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
        localButtons.add(btnDosJugadoresLocal, gbc_btnDosJugadoresLocal);

        JPanel onlineButtons = new JPanel();
        GridBagConstraints gbc_onlineButtons = new GridBagConstraints();
        gbc_onlineButtons.ipady = 50;
        gbc_onlineButtons.ipadx = 50;
        gbc_onlineButtons.fill = GridBagConstraints.BOTH;
        gbc_onlineButtons.gridx = 1;
        gbc_onlineButtons.gridy = 1;
        add(onlineButtons, gbc_onlineButtons);
        GridBagLayout gbl_onlineButtons = new GridBagLayout();
        gbl_onlineButtons.columnWidths = new int[] {0};
        gbl_onlineButtons.rowHeights = new int[] {0, 0, 0};
        gbl_onlineButtons.columnWeights = new double[]{0.0};
        gbl_onlineButtons.rowWeights = new double[]{0.0, 0.0, 0.0};
        onlineButtons.setLayout(gbl_onlineButtons);

        JButton btnJugarOnline = new JButton("JUGAR ONLINE");
        btnJugarOnline.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnJugarOnline.setFont(new Font("Arial", Font.PLAIN, 15));
        GridBagConstraints gbc_btnJugarOnline = new GridBagConstraints();
        gbc_btnJugarOnline.fill = GridBagConstraints.BOTH;
        gbc_btnJugarOnline.ipadx = 50;
        gbc_btnJugarOnline.ipady = 25;
        gbc_btnJugarOnline.insets = new Insets(0, 0, 12, 5);
        gbc_btnJugarOnline.gridx = 0;
        gbc_btnJugarOnline.gridy = 0;
        onlineButtons.add(btnJugarOnline, gbc_btnJugarOnline);

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
        onlineButtons.add(btnCrearSala, gbc_btnCrearSala);

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
        onlineButtons.add(btnUnirseASala, gbc_btnUnirseASala);
    }

    private void playLocalHandler() {
        mainPanel.startLocalGame();
    }

    private void playOnlineAsHost() {
        mainPanel.startOnlineGameAsHost();
    }

    private void playOnlineAsClient() {
        mainPanel.startOnlineGameAsClient();
    }

//    private void createRoomHandler() {
//        // TODO: Modify create a room
//    }
//
//    private void joinRoomHandler() {
//        // TODO: Modify to join a room
//    }
}
