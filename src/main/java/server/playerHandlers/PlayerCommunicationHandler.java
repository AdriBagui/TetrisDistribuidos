package server.playerHandlers;

import java.io.*;
import java.net.Socket;

/**
 * A unidirectional relay that forwards data from one player to another.
 * <p>
 * This class reads bytes from the 'sender' socket and writes them immediately to the
 * 'receiver' socket. Two instances of this class are needed to establish full-duplex
 * communication for a game.
 * </p>
 */
public class PlayerCommunicationHandler implements Runnable {
    private final Socket sender;
    private final Socket receiver;

    /**
     * Creates a relay between two sockets.
     *
     * @param sender   The socket acting as the source of data.
     * @param receiver The socket acting as the destination of data.
     */
    public PlayerCommunicationHandler(Socket sender, Socket receiver){
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Continuously relays bytes until a connection is closed.
     */
    @Override
    public void run() {
        byte byteRead;
        DataInputStream dis;
        DataOutputStream dos;

        try {
            dis = new DataInputStream(sender.getInputStream());
            dos = new DataOutputStream(receiver.getOutputStream());

            // Read first byte to start the loop
            byteRead = dis.readByte();

            while (true) {
                // Forward the byte to the opponent
                dos.write(byteRead);
                dos.flush();

                // Wait for the next byte
                byteRead = dis.readByte();
            }
        }
        catch (EOFException eofe) {
            // Normal termination: Sender closed the connection (e.g., game over or quit)
            handleDisconnection();
        }
        catch (IOException ioe) {
            // Abnormal termination: Network error
            handleDisconnection();
        }
    }

    /**
     * Closes connections safely when the relay loop ends.
     * <p>
     * When one player disconnects, we must ensure the other player's socket is also
     * closed or notified so their game ends gracefully.
     * </p>
     */
    private void handleDisconnection() {
        try {
            if (!sender.isClosed()) sender.shutdownInput();
        } catch (IOException e) { /* Ignore */ }

        try {
            if (!receiver.isClosed()) receiver.shutdownOutput();
        } catch (IOException e) { /* Ignore */ }

        // Optionally close sockets fully if shutdown isn't sufficient for your protocol
        try { sender.close(); } catch (IOException e) { /* Ignore */ }
        try { receiver.close(); } catch (IOException e) { /* Ignore */ }
    }
}