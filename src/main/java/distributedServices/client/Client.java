package distributedServices.client;

import tetris.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client implements Runnable {
    private MainFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }

    @Override
    public void run() {
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);

        try(Socket socket = new Socket(HostClient.HOST_CLIENT_IP,HostClient.HOST_CLIENT_PORT)){

        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
