package distributedServices.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 7777;

    /**
     * (V.1) Creates the server that connects two consecutive players.
     * @param args no args are needed
     */
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try(ServerSocket server = new ServerSocket(SERVER_PORT);) {
            while(true){
                Socket client1 = server.accept();
                Socket client2 = server.accept();
                pool.execute(new GameComunictionHandler(client1, client2));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
