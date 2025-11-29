package distributedServices.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static distributedServices.server.Config.SERVER_PORT;

public class Server {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try(ServerSocket server = new ServerSocket(SERVER_PORT)){
            while(true){
                Socket cliente1 = server.accept();
                Socket cliente2 = server.accept();
                pool.execute(new GameThread(cliente1, cliente2));
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
