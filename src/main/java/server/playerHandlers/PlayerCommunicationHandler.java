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
    private final GameCommunicationHandler gameCommunicationHandler;
    private final Socket sender;
    private final Socket receiver;
    private final long seed;

    /**
     * Creates a relay between two sockets.
     *
     * @param gameCommunicationHandler The GameCommunicationHandler that manages the game
     * @param sender                   The socket acting as the source of data.
     * @param receiver                 The socket acting as the destination of data.
     * @param seed                     The seed for random generator of tetrominoes for the game.
     */
    public PlayerCommunicationHandler(GameCommunicationHandler gameCommunicationHandler, Socket sender, Socket receiver, long seed){
        this.gameCommunicationHandler = gameCommunicationHandler;
        this.sender = sender;
        this.receiver = receiver;
        this.seed = seed;
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

            // Notify successful connection
            dos.writeByte(1);
            dos.flush();

            // Start game by sending the seed
            dos.writeLong(seed);
            dos.flush();

            while (true) {
                // Wait for the next byte
                byteRead = dis.readByte();

                // Forward the byte to the opponent
                dos.write(byteRead);
                dos.flush();
            }
        }
        catch (EOFException eofe) {
            try {
                sender.shutdownInput();
                receiver.shutdownOutput();
                gameCommunicationHandler.processUnidirectionalShutdown(sender);
            }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to shutDown unidirectional communication after one player's game being finished."); } // This should never happen, if it does your computer is broken sry
        }
        catch (IOException ioe) {
            gameCommunicationHandler.processBidirectionalShutdown();
        }
    }
}