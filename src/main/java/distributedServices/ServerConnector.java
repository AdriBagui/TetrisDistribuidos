package distributedServices;

import distributedServices.server.Server;

import java.io.IOException;
import java.net.Socket;

public class ServerConnector {
    private Socket socket;

    /**
     * Connects to the server to get the socket for the rest of the code.
     */
    public ServerConnector() {
        try{
            this.socket = new Socket(Server.SERVER_IP, Server.SERVER_PORT);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
