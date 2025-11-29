package distributedServices.server;

import java.io.*;
import java.net.Socket;

public class GameThread implements Runnable{
    private Socket client1;
    private Socket client2;

    public GameThread(Socket socket1, Socket socket2){
        this.client1 = socket1;
        this.client2 = socket2;
    }

    @Override
    public void run() {
        String client1Name;
        String client2Name;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client1.getInputStream()));
            client1Name = br.readLine();
            br = new BufferedReader(new InputStreamReader((client2.getInputStream())));
            client2Name = br.readLine();
                // TODO: CONNECT CLIENTS
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
