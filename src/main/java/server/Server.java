package server;

import server.gameModeHandlers.LobbiesHandler;
import server.gameModeHandlers.QuickPlayHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main entry point for the Tetris Server.
 * <p>
 * This class is responsible for setting up the server socket and listening for incoming
 * client connections. It uses a cached thread pool to handle multiple connection requests
 * concurrently, delegating the specific handshake logic to {@link MatchmakingHandler}.
 * </p>
 */
public class Server {
    // public static final String SERVER_IP = "88.13.205.169";
    /**
     * The IP address binding for the server (localhost for local testing).
     */
    public static final String SERVER_IP = "localhost";

    /**
     * The port number on which the server listens for connections.
     */
    public static final int SERVER_PORT = 7777;

    /**
     * The main method that starts the server.
     * <p>
     * It initializes the handlers for different game modes (Quick Play, Lobbies) and enters
     * an infinite loop to accept client connections. Each accepted connection is processed
     * in a separate thread.
     * </p>
     *
     * @param args Command line arguments if there is an arg, it will be used as the server port.
     */
    public static void main(String[] args) {
        int serverPort;

        if (args != null && args.length > 0) serverPort = Integer.parseInt(args[0]);
        else serverPort = SERVER_PORT;

        // Shared resources for matchmaking
        QuickPlayHandler modernTetrisQuickPlayHandler = new QuickPlayHandler();
        QuickPlayHandler nesQuickPlayHandler = new QuickPlayHandler();
        LobbiesHandler lobbiesHandler = new LobbiesHandler();

        try (ExecutorService pool = Executors.newCachedThreadPool();
             ServerSocket server = new ServerSocket(serverPort)) {
            System.out.println("Server is running on port " + serverPort);

            while (true) {
                try {
                    // Block until a new connection is made
                    Socket client = server.accept();
                    System.out.println("New client connected: " + client.getInetAddress());

                    // Hand off the connection to a worker thread
                    pool.execute(new MatchmakingHandler(client, modernTetrisQuickPlayHandler, nesQuickPlayHandler, lobbiesHandler));
                }
                catch (IOException ioe) {
                    System.out.println("FATAL ERROR while trying to accept client socket: " + ioe.getMessage());
                }
            }
        }
        catch (IOException ioe) {
            System.out.println("FATAL ERROR while trying to create server socket on port " + SERVER_PORT);
        }
    }
}