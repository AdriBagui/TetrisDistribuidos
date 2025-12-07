package distributedServices.server;

import distributedServices.ConnectionMode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MatchmakingHandler implements Runnable{
    Socket client;
    QuickSocketHandler socketHandler;
    QuickSocketHandler socketHandlerNES;
    LobbiesHandler lobbiesHandler;

    public MatchmakingHandler(Socket client, QuickSocketHandler socketHandler, QuickSocketHandler socketHandlerNES, LobbiesHandler lobbiesHandler){
        this.client = client;
        this.socketHandler = socketHandler;
        this.socketHandlerNES = socketHandlerNES;
        this.lobbiesHandler = lobbiesHandler;
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
                    quickMatchSearch();
                    break;
                case QUICK_MATCH_NES_MODE:
                    quickMatchNESSearch();
                    break;
                case HOST_GAME_MODE:
                    hostGameSearch();
                    break;
                case JOIN_GAME_MODE:
                    joinLobbySearch(dis);
                    break;
            }

        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void quickMatchSearch() {
        if(!socketHandler.isThereAPlayingWaiting())
            socketHandler.setQuickPlayPlayer(client);
        else {
            new Thread(new GameCommunicationHandler(client, socketHandler.getAndRemoveQuickPlayPlayer())).start();
        }
    }

    private void quickMatchNESSearch() {
        if(!socketHandlerNES.isThereAPlayingWaiting())
            socketHandlerNES.setQuickPlayPlayer(client);
        else {
            new Thread(new GameCommunicationHandler(client, socketHandlerNES.getAndRemoveQuickPlayPlayer())).start();
        }
    }

    private void hostGameSearch() {
        int roomId = lobbiesHandler.createLobby(client);
        try{
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            dos.writeInt(roomId);
            dos.flush();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void joinLobbySearch(DataInputStream dis) {
        try{
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            int roomId = dis.readInt();
            if(lobbiesHandler.containsLobby(roomId)){
                dos.writeBoolean(true);
                dos.flush();
                new Thread(new GameCommunicationHandler(client, lobbiesHandler.getAndRemoveLobby(roomId))).start();
            } else dos.writeBoolean(false);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
