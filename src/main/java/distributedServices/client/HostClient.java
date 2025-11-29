package distributedServices.client;

import tetris.MainFrame;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HostClient implements Runnable {
    // TODO: GET IP FROM SERVER
    public static final String HOST_CLIENT_IP = "localhost";
    // TODO: CHANGE TO PRIVATE
    public static final int HOST_CLIENT_PORT = 7777;

    private MainFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new HostClient());

    }

    @Override
    public void run() {
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);

        try(ServerSocket serverSocket = new ServerSocket(HOST_CLIENT_PORT)){
            try(Socket client = serverSocket.accept()){
                InputManager inputManager = new InputManager(client.getInputStream(), mainFrame);
                OutputManager outputManager = new OutputManager(client.getOutputStream(), mainFrame);

                // TODO: Recibir XML con historial actualizado
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
