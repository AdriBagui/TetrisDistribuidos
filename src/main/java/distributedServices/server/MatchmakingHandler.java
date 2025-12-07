package distributedServices.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static tetris.Config.*;

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
        int gameModeSelected;
        
        try{
            DataInputStream dis = new DataInputStream(client.getInputStream());
            gameModeSelected = dis.readInt();
            System.out.println(Thread.currentThread().getName() + "He seleccionado el modo: " + gameModeSelected);
            switch (gameModeSelected){
                case QUICK_MATCH_MODE:
                    quickPlayPlayers.add(client);
                    System.out.println(Thread.currentThread().getName() + "He añadido el cliente a la lista");
                    quickModeSearch();
                    break;
                case QUICK_MATCH_NES_MODE:
                    quickPlayNESPlayers.add(client);
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

    private void quickModeSearch() {
        Socket opponentSocket = null;
        while(opponentSocket==null && quickPlayPlayers.contains(client)){
            System.out.println(Thread.currentThread().getName() + "Pruebo a buscar");
            lookForOpponentQuickMode();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if(opponentSocket != null){
            new Thread(new GameCommunicationHandler(client, opponentSocket)).start();
            System.out.println(Thread.currentThread().getName() + "Comienzo partida");
        }
        System.out.println(Thread.currentThread().getName() + "Me encontró otro jugador");
    }

    /**
     * Looks for a player to connect in quickModeSearch. It removes the {@link this.client} and the opponent if an opponent is found
     * @return Socket of the opponent to connect and play
     */
    private Socket lookForOpponentQuickMode(){
        Socket opponentSocket = null;
        synchronized (quickPlayPlayers){
            if(quickPlayPlayers.size()>1){ // There's someone other than you
                System.out.println(Thread.currentThread().getName() + "He encontrado rival");
                // Get and remove the opponent
                int i = 0;
                while(quickPlayPlayers.get(i).equals(client)){
                    i++;
                }
                opponentSocket = quickPlayPlayers.get(i);
                System.out.println(Thread.currentThread().getName() + "He cogido el rival");
                quickPlayPlayers.remove(i);
                // Remove the client
                i = 0;
                while(!quickPlayPlayers.get(i).equals(client)){
                    i++;
                }
                quickPlayPlayers.remove(i);
                System.out.println(Thread.currentThread().getName() + "Me eliminé de la lista");
            }
        }
        return opponentSocket;
    }

    private void quickModeNESSearch() {

    }

    private void hostGameSearch() {

    }

    private void joinGameSearch() {

    }
}
