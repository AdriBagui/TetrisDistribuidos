package distributedServices.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class LobbiesHandler {
    private HashMap<Integer, Socket> lobbies;
    private static final int MAX_NUMBER_OF_ROOMS = 1000;

    public LobbiesHandler(){
        lobbies = new HashMap<Integer, Socket>();
    }

    /**
     * Adds a lobby to the list of lobbies
     * @return Returns the room id of the lobby create
     */
    public synchronized int createLobby(Socket host){
        Random random = new Random();
        int roomId = random.nextInt(0, MAX_NUMBER_OF_ROOMS);
        while(lobbies.containsKey(roomId)){
            roomId = random.nextInt(0, MAX_NUMBER_OF_ROOMS);
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
