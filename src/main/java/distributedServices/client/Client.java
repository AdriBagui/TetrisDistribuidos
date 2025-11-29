package distributedServices.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try(Socket socket = new Socket(HostClient.HOST_CLIENT_IP,HostClient.HOST_CLIENT_PORT)){

        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
