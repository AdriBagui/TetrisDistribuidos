package distributedServices.server;

import java.io.*;
import java.net.Socket;

// Versión 1: Esta clase se encarga de enviar paquetes del sender al reciver.
public class PlayerComunicationHandler implements Runnable{
    Socket sender;
    Socket reciver;

    public PlayerComunicationHandler(Socket sender, Socket reciver){
        this.sender = sender;
        this.reciver = reciver;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(sender.getInputStream());
            DataOutputStream dos = new DataOutputStream(reciver.getOutputStream());
            byte byteRead;
            while (true) { //TODO: modificar para detectar el fin de una partida
                byteRead = dis.readByte();
                dos.write(byteRead);
                dos.flush();
            }
        } catch (EOFException eofe){
            eofe.printStackTrace();
        }catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
