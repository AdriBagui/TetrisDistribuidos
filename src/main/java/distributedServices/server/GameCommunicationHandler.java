package distributedServices.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameCommunicationHandler implements Runnable{
    private final Socket client1;
    private final Socket client2;


    public GameCommunicationHandler(Socket client1, Socket client2){
        this.client1 = client1;
        this.client2 = client2;
    }

    /**
     * Creates the communication channel for {@code client1} and {@code client2} so they can send their inputs
     */
    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        try{
            DataOutputStream dosC1 = new DataOutputStream(client1.getOutputStream());
            DataOutputStream dosC2 = new DataOutputStream(client2.getOutputStream());
            long seed = System.currentTimeMillis();
            dosC1.writeLong(seed);
            dosC2.writeLong(seed);
            dosC1.flush();
            dosC2.flush();
        } catch (IOException ioe){
            closeQuietly(client1);
            closeQuietly(client2);
        }

        pool.execute(new PlayerCommunicationHandler(client1, client2));
        pool.execute(new PlayerCommunicationHandler(client2, client1));

        pool.shutdown();
    }

    private void closeQuietly(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) { System.out.println("FATAL ERROR while close sockets with clients after failing to send seeds"); } // This should never happen, if it does your computer is broken sry
    }
}
