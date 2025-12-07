package distributedServices.server;

import java.io.*;
import java.net.Socket;

public class PlayerCommunicationHandler implements Runnable{
    Socket sender;
    Socket receiver;
    private final byte endGameByte = (byte) 127;

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
            byteRead = dis.readByte();
            while (byteRead != endGameByte) { //TODO: modificar para detectar el fin de una partida
                dos.write(byteRead);
                dos.flush();
                byteRead = dis.readByte();
            }
        }
        catch (EOFException eofe) { eofe.printStackTrace(); }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
}
