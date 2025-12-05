package distributedServices.server;

import java.io.*;
import java.net.Socket;

public class PlayerCommunicationHandler implements Runnable{
    Socket sender;
    Socket receiver;

    public PlayerCommunicationHandler(Socket sender, Socket receiver){
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Sends the inputs from {@code sender} to the {@code receiver}
     */
    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(sender.getInputStream());
            DataOutputStream dos = new DataOutputStream(receiver.getOutputStream());
            byte byteRead;
            while (true) { //TODO: modificar para detectar el fin de una partida
                byteRead = dis.readByte();
                dos.write(byteRead);
                dos.flush();
            }
        }
        catch (EOFException eofe) { eofe.printStackTrace(); }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}
