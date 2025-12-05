package distributedServices.server;

import java.net.Socket;

public class GameComunictionHandler implements Runnable{
    private Socket client1;
    private Socket client2;


    public GameComunictionHandler(Socket client1, Socket client2){
        this.client1 = client1;
        this.client2 = client2;
    }

    @Override
    public void run() {

    }
}
