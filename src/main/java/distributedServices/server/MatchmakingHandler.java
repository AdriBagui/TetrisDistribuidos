package distributedServices.server;

import distributedServices.ConnectionMode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MatchmakingHandler implements Runnable{
    Socket client;
    QuickPlayHandler socketHandler;
    QuickPlayHandler socketHandlerNES;
    LobbiesHandler lobbiesHandler;

    public MatchmakingHandler(Socket client, QuickPlayHandler socketHandler, QuickPlayHandler socketHandlerNES, LobbiesHandler lobbiesHandler){
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
        DataInputStream dis;

        try{
            dis = new DataInputStream(client.getInputStream());
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
            closeSocket();
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
        DataOutputStream dos;

        try{
            dos = new DataOutputStream(client.getOutputStream());
            dos.writeInt(roomId);
            dos.flush();
        } catch (IOException ioe){
            // CRITICAL: Remove the lobby so players don't try to join a dead room.
            lobbiesHandler.getAndRemoveLobby(roomId);
            closeSocket();
        }
    }

    private void joinLobbySearch(DataInputStream dis) {
        int roomId;

        try{
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
            roomId = dis.readInt();

            if(lobbiesHandler.containsLobby(roomId)){
                dos.writeBoolean(true);
                dos.flush();
                new Thread(new GameCommunicationHandler(client, lobbiesHandler.getAndRemoveLobby(roomId))).start();
            }
            else {
                dos.writeBoolean(false);
                dos.flush();
            }
        } catch (IOException ioe) {
            closeSocket();
        }
    }

    private void closeSocket() {
        try { if (client != null && !client.isClosed()) client.close(); }
        catch (IOException e) { System.out.println("FATAL ERROR while trying to close client socket"); } // This should never happen, if it does your computer is broken sry
    }
}
