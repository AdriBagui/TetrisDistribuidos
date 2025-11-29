package distributedServices.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HostClient {
    // TODO: GET IP FROM SERVER
    public static final String HOST_CLIENT_IP = "localhost";
    // TODO: CHANGE TO PRIVATE
    public static final int HOST_CLIENT_PORT = 7777;

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(HOST_CLIENT_PORT)){
            try(Socket cliente = serverSocket.accept()){

            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
