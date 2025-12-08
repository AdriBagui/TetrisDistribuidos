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
        byte byteRead;
        DataInputStream dis;
        DataOutputStream dos;

        try {
            dis = new DataInputStream(sender.getInputStream());
            dos = new DataOutputStream(receiver.getOutputStream());
            byteRead = dis.readByte();

            while (true) { // This finishes when the socket closes because dis.readByte() throws an EOFException
                dos.write(byteRead);
                dos.flush();
                byteRead = dis.readByte();
            }
        }
        catch (EOFException eofe) {
            try {
                sender.shutdownInput();
                receiver.shutdownOutput();
            }
            catch (IOException ioe) { System.out.println("FATAL ERROR while trying to shutdown input from sender and output from receiver"); } // This should never happen, if it does your computer is broken sry
        }
        catch (IOException ioe) {
            try { sender.close(); }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown sender socket when receiver disconnected"); } // This should never happen, if it does your computer is broken sry

            try { receiver.close(); }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to shutdown receiver socket when sender disconnected"); } // This should never happen, if it does your computer is broken sry
        }
    }
}
