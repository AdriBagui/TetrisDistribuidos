package server;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MatchmakingHandler implements Runnable{
    Socket client;
    QuickPlayHandler modernTetrisQuickPlayHandler;
    QuickPlayHandler nesQuickPlayHandler;
    LobbiesHandler lobbiesHandler;

    public MatchmakingHandler(Socket client, QuickPlayHandler modernTetrisQuickPlayHandler, QuickPlayHandler nesQuickPlayHandler, LobbiesHandler lobbiesHandler){
        this.client = client;
        this.modernTetrisQuickPlayHandler = modernTetrisQuickPlayHandler;
        this.nesQuickPlayHandler = nesQuickPlayHandler;
        this.lobbiesHandler = lobbiesHandler;
    }

    /**
     * This method receives an {@code int} which represents the game mode the client wants to play.
     */
    @Override
    public void run() {
        DataInputStream dis;
        DataOutputStream dos;
        int gameModeSelectedId;
        ConnectionMode gameModeSelected;

        try{
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

            gameModeSelectedId = dis.readInt();
            gameModeSelected = ConnectionMode.values()[gameModeSelectedId];

            switch (gameModeSelected) {
                case MODERN_TETRIS_QUICK_PLAY -> quickMatchSearch();
                case NES_QUICK_PLAY -> quickMatchNESSearch();
                case HOST_GAME -> hostGameSearch(dos);
                case JOIN_GAME -> joinLobbySearch(dis, dos);
            }
        } catch (IOException ioe){
            closeSocket();
        }
    }

    private void quickMatchSearch() {
        if(modernTetrisQuickPlayHandler.noPlayerWaiting())
            modernTetrisQuickPlayHandler.setWaitingPlayer(client);
        else {
            new Thread(new GameCommunicationHandler(client, modernTetrisQuickPlayHandler.getAndRemoveWaitingPlayer())).start();
        }
    }

    private void quickMatchNESSearch() {
        if(nesQuickPlayHandler.noPlayerWaiting())
            nesQuickPlayHandler.setWaitingPlayer(client);
        else {
            new Thread(new GameCommunicationHandler(client, nesQuickPlayHandler.getAndRemoveWaitingPlayer())).start();
        }
    }

    private void hostGameSearch(DataOutputStream dos) {
        int roomId = lobbiesHandler.createLobby(client);

        try{
            dos.writeInt(roomId);
            dos.flush();
        } catch (IOException ioe) {
            // CRITICAL: Remove the lobby so players don't try to join a dead room.
            lobbiesHandler.getAndRemoveLobby(roomId);
            closeSocket();
        }
    }

    private void joinLobbySearch(DataInputStream dis, DataOutputStream dos) {
        int roomId;

        try{
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
