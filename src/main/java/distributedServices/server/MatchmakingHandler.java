package distributedServices.server;

import distributedServices.ConnectionMode;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MatchmakingHandler implements Runnable{
    Socket client;
    QuickSocketHandler socketHandler;
    QuickSocketHandler socketHandlerNES;
    private ConcurrentHashMap<Integer, Socket> lobbies;
    private static final int MAX_NUMBER_OF_ROOMS = 1000;


    public MatchmakingHandler(Socket client, QuickSocketHandler socketHandler, QuickSocketHandler socketHandlerNES, ConcurrentHashMap<Integer, Socket> lobbies){
        this.client = client;
        this.socketHandler = socketHandler;
        this.socketHandlerNES = socketHandlerNES;
        this.lobbies = lobbies;
    }

    /**
     * This method receives an {@code int} which represents the game mode the client wants to play.
     */
    @Override
    public void run() {
        ConnectionMode gameModeSelected;
        
        try{
            DataInputStream dis = new DataInputStream(client.getInputStream());
            gameModeSelected = ConnectionMode.values()[dis.readInt()];
            switch (gameModeSelected) {
                case QUICK_MATCH_MODE:
                    if(!socketHandler.isThereAPlayingWaiting())
                        socketHandler.setQuickPlayPlayer(client);
                    else {
                        new GameCommunicationHandler(client, socketHandler.getAndRemoveQuickPlayPlayer());
                    }
                    break;
                case QUICK_MATCH_NES_MODE:
                    if(!socketHandlerNES.isThereAPlayingWaiting())
                        socketHandlerNES.setQuickPlayPlayer(client);
                    else {
                        new GameCommunicationHandler(client, socketHandlerNES.getAndRemoveQuickPlayPlayer());
                    }
                case HOST_GAME_MODE:
                    synchronized (lobbies){
                        Random random = new Random();
                        int roomId = random.nextInt(0, MAX_NUMBER_OF_ROOMS);
                        while(lobbies.containsKey(roomId)){
                            roomId = random.nextInt(0, MAX_NUMBER_OF_ROOMS);
                        }
                        lobbies.put(roomId,client);
                    };
                    hostGameSearch();
                    break;
                case JOIN_GAME_MODE:
                    joinGameSearch();
                    break;
            }

        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void hostGameSearch() {

    }

    private void joinGameSearch() {

    }
}
