package distributedServices.server;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameComunictionHandler implements Runnable{
    private Socket client1;
    private Socket client2;


    public GameComunictionHandler(Socket client1, Socket client2){
        this.client1 = client1;
        this.client2 = client2;
    }

    /**
     * Creates the communication channel for {@code client1} and {@code client2} so they can send their inputs
     */
    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(new PlayerComunicationHandler(client1, client2));
        pool.execute(new PlayerComunicationHandler(client2, client1));
    }
}
