package menus;

import distributedServices.ServerConnector;
import main.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class WaitingOpponentPanel extends JPanel {
    private MainPanel mainPanel;
    private int roomId = -1;
    private boolean showRoomId = false;
    private static final Color BACKGROUND_COLOR = new Color(22, 22, 22, 255);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font WAITING_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font ROOM_ID_FONT = new Font("Segoe UI", Font.PLAIN, 14);


    /**
     * Creates the waiting screen for someone to connect
     * @param mainPanel Panel which contains it
     */
    public WaitingOpponentPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        setLayout(new GridBagLayout());
        this.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();

        // 1. We use a spinner to give some sense of time passing
        JProgressBar spinner = new JProgressBar();
        spinner.setIndeterminate(true);
        spinner.setPreferredSize(new Dimension(80, 20));

        // Changes the spinner color
        spinner.setForeground(new Color(60, 130, 255));
        spinner.setBackground(new Color(35, 35, 35));

        gbc.gridy = 0;
        this.add(spinner, gbc);

        // 2. Waiting message
        JLabel waitingLabel = new JLabel("Waiting for an opponent...");
        waitingLabel.setFont(WAITING_FONT);
        waitingLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 1;
        this.add(waitingLabel, gbc);

        // 3. Show room ID
        if(showRoomId){
            JLabel roomIDLabel = new JLabel("Room ID: " + roomId);
            roomIDLabel.setFont(ROOM_ID_FONT);
            roomIDLabel.setForeground(TEXT_COLOR);

            gbc.gridy = 2;
            this.add(roomIDLabel, gbc);
        }
    }

    /**
     * Connects to the server and starts an online game
     */
    public void connect(int gameMode){
        new Thread(){
            @Override
            public void run() {
                System.out.println("Me conecto");
                ServerConnector serverConnector = new ServerConnector();
                Socket boardsSocket = serverConnector.getSocket();
                long seed;
                try{
                    DataInputStream dis = new DataInputStream(boardsSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(boardsSocket.getOutputStream());
                    seed = dis.readLong();
                    dos.writeInt(gameMode);
                    dos.flush();
                    mainPanel.startOnlineGame(seed,boardsSocket);
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }.start();
    }
    
}