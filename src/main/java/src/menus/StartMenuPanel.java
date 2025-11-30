package src.menus;

import src.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuPanel extends JPanel {
    private MainPanel mainPanel;

    public StartMenuPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        JButton btnPlayLocal = new JButton("JUGAR EN LOCAL");
        btnPlayLocal.setFont(new Font("Arial", Font.BOLD, 18));
        btnPlayLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playLocalHandler();
            }
        });
        add(btnPlayLocal);

        JButton btnPlayOnlineAsHost = new JButton("JUGAR ONLINE (COMO HOST)");
        btnPlayOnlineAsHost.setFont(new Font("Arial", Font.BOLD, 18));
        btnPlayOnlineAsHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playOnlineAsHost();
            }
        });
        add(btnPlayOnlineAsHost);

        JButton btnPlayOnlineAsClient = new JButton("JUGAR ONLINE (COMO CLIENTE)");
        btnPlayOnlineAsClient.setFont(new Font("Arial", Font.BOLD, 18));
        btnPlayOnlineAsClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playOnlineAsClient();
            }
        });
        add(btnPlayOnlineAsClient);

//        JButton btnCreateRoom = new JButton("CREAR SALA");
//        btnCreateRoom.setFont(new Font("Arial", Font.BOLD, 18));
//        btnCreateRoom.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                createRoomHandler();
//            }
//        });
//        add(btnCreateRoom);
//
//        JButton btnJoinRoom = new JButton("UNIRSE A SALA");
//        btnJoinRoom.setFont(new Font("Arial", Font.BOLD, 18));
//        btnJoinRoom.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                joinRoomHandler();
//            }
//        });
//        add(btnJoinRoom);
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
//
//    }
}
