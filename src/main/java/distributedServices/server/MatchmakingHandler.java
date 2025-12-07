package distributedServices.server;

import distributedServices.ConnectionMode;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class MatchmakingHandler implements Runnable{
    Socket client;
    private Vector<Socket> quickPlayPlayers;
    private Vector<Socket> quickPlayNESPlayers;
    private ConcurrentHashMap<Integer, Socket> lobbies;


    public MatchmakingHandler(Socket client, Vector<Socket> quickPlayPlayers, Vector<Socket> quickPlayNESPlayers, ConcurrentHashMap<Integer, Socket> lobbies){
        this.client = client;
        this.quickPlayPlayers = quickPlayPlayers;
        this.quickPlayNESPlayers = quickPlayNESPlayers;
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
                    synchronized (quickPlayPlayers) { quickPlayPlayers.add(client); }
                    quickModeSearch();
                    break;
                case QUICK_MATCH_NES_MODE:
                    synchronized (quickPlayNESPlayers) { quickPlayNESPlayers.add(client); }
                    quickModeNESSearch();
                    break;
                case HOST_GAME_MODE:
                    // TODO: ver como se da una id a la sala antes del search
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

    /**
     * Searchs for a game of Quick Mode
     */
    private void quickModeSearch() {
        Socket opponentSocket = null;
        while(opponentSocket == null && quickPlayPlayers.contains(client)){
            opponentSocket = lookForOpponent(quickPlayPlayers);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(opponentSocket != null){
            new Thread(new GameCommunicationHandler(client, opponentSocket)).start();
        }
    }

    /**
     * Searchs for a game of Quick Mode (NES)
     */
    private void quickModeNESSearch() {
        Socket opponentSocket = null;
        while(opponentSocket == null && quickPlayNESPlayers.contains(client)){
            opponentSocket = lookForOpponent(quickPlayNESPlayers);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(opponentSocket != null){
            new Thread(new GameCommunicationHandler(client, opponentSocket)).start();
        }
    }

    /**
     * Looks for a player to connect in quick mode searching. It removes the {@link this.client} and the opponent if an opponent is found
     * @return Socket of the opponent to connect and play
     */
    private Socket lookForOpponent(Vector<Socket> socketsVector){
        Socket opponentSocket = null;
        synchronized (socketsVector){
            if(socketsVector.size()>1){ // There's someone other than you
                // Get and remove the opponent
                int i = 0;
                while(socketsVector.get(i).equals(client)){
                    i++;
                }
                opponentSocket = socketsVector.get(i);
                socketsVector.remove(i);
                // Remove the client
                i = 0;
                while(!socketsVector.get(i).equals(client)){
                    i++;
                }
                socketsVector.remove(i);
            }
        }
        return opponentSocket;
    }

    private void hostGameSearch() {

    }

    private void joinGameSearch() {

    }
}
