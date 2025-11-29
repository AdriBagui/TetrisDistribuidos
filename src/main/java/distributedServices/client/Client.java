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
        /*
        1. Obtengo mi tetromino
        2. Obtengo la información del tetromino
        3. Paso la información del tetromino en forma de 3 ints (x,y,rot)
        4. A la vez envío
        */
        try(Socket socket = new Socket(HostClient.HOST_CLIENT_IP,HostClient.HOST_CLIENT_PORT)){
            InputManager inputManager = new InputManager(socket.getInputStream(), mainFrame);
            OutputManager outputManager = new OutputManager(socket.getOutputStream(), mainFrame);

            // TODO: Recibir XML con historial actualizado
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
