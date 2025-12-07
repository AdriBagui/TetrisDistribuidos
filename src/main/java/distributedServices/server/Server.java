package distributedServices.server;

import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    // public static final String SERVER_IP = "localhost";
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 7777;

    private static Vector<Socket> quickPlayPlayers = new Vector<>();
    private static Vector<Socket> quickPlayNESPlayers = new Vector<>();
    private static ConcurrentHashMap<Integer, Socket> lobbies = new ConcurrentHashMap<>();

    /**
     * Creates the server that connects players.
     * @param args no args are needed
     */
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket server = new ServerSocket(SERVER_PORT);) {
            while (true) {
                try {
                    Socket client = server.accept();
                    pool.execute(new MatchmakingHandler(client, quickPlayPlayers, quickPlayNESPlayers, lobbies));
                }
                catch (IOException ioe) { ioe.printStackTrace(); }
            }
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
        finally {
            pool.shutdown();
            pool.close();
        }
    }
}
