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
            socket = new Socket(Server.SERVER_IP, Server.SERVER_PORT);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * Returns the socket obtained from the connection
     * @return Socket to the server
     */
    public Socket getSocket() {
        return socket;
    }
}
