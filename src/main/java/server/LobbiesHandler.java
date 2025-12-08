package server;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

public class LobbiesHandler {
    private HashMap<Integer, Socket> lobbies;
    private static final int MAX_NUMBER_OF_LOBBIES = 1000;

    public LobbiesHandler(){
        lobbies = new HashMap<Integer, Socket>();
    }

    /**
     * Adds a lobby to the list of lobbies
     * @return Returns the room id of the lobby create
     */
    public synchronized int createLobby(Socket host){
        Random random = new Random();

        while (lobbies.size() >= MAX_NUMBER_OF_LOBBIES) {
            try { Thread.sleep(100); }
            catch (InterruptedException e) { // This should never happen, no one is interrupting this thread
                try { host.close(); }
                catch (IOException ioe) { System.out.println("FATAL ERROR while trying to create a lobby"); } // This should never happen, if it does your computer is broken sry
            }
        }
        int roomId = random.nextInt(0, MAX_NUMBER_OF_LOBBIES - lobbies.size());
        while(lobbies.containsKey(roomId)) {
            roomId = (roomId + 1)%MAX_NUMBER_OF_LOBBIES;
        }
        lobbies.put(roomId,host);

        return roomId;
    }

    /**
     * Room must exist. Used to get the {@code Socket} of the lobby host and removes the lobby
     * @param roomId Id of the room
     * @return Socket of the lobby's host
     */
    public synchronized Socket getAndRemoveLobby(int roomId){
        Socket aux = lobbies.get(roomId);
        lobbies.remove(roomId);
        return aux;
    }

    public synchronized boolean containsLobby(int roomId){
        return lobbies.containsKey(roomId);
    }
}
