package distributedServices;

import distributedServices.server.Server;

import java.io.IOException;
import java.net.Socket;

// Esta clase se encarga de realizar la conexión con el servidor para obtener un socket
public class ServerConnector {
    private Socket socket;

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
