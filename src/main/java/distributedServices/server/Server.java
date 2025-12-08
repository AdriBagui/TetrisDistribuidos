package distributedServices.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    // public static final String SERVER_IP = "localhost";
    public static final String SERVER_IP = "88.13.205.169";
    public static final int SERVER_PORT = 7777;

    /**
     * Creates the server that connects players.
     * @param args no args are needed
     */
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        QuickPlayHandler socketHandler = new QuickPlayHandler();
        QuickPlayHandler socketHandlerNES = new QuickPlayHandler();
        LobbiesHandler lobbiesHandler = new LobbiesHandler();

        try (ServerSocket server = new ServerSocket(SERVER_PORT);) {
            while (true) {
                try {
                    Socket client = server.accept();
                    pool.execute(new MatchmakingHandler(client, socketHandler, socketHandlerNES, lobbiesHandler));
                }
                catch (IOException ioe) { System.out.println("FATAL ERROR while trying to accept client socket"); } // This should never happen, if it does your computer is broken sry
            }
        }
        catch (IOException ioe) { System.out.println("FATAL ERROR while trying to create server socket"); } // This should never happen, if it does your computer is broken sry (tbh is using the server port for another program probably)
        finally {
            pool.shutdown();
            pool.close();
        }
    }
}
