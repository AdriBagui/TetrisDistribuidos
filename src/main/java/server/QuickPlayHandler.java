package server;

import java.net.Socket;

public class QuickPlayHandler {
    private Socket waitingPlayer;

    public QuickPlayHandler() {
        waitingPlayer = null;
    }

    public synchronized Socket getWaitingPlayer() {
        return waitingPlayer;
    }

    /**
     * Gets the current waitingPlayer and sets it to null.
     * @return Returns the current waitingPlayer.
     */
    public synchronized Socket getAndRemoveWaitingPlayer() {
        Socket aux = getWaitingPlayer();
        setWaitingPlayer(null);
        return aux;
    }

    public synchronized void setWaitingPlayer(Socket waitingPlayer) {
        this.waitingPlayer = waitingPlayer;
    }

    public synchronized boolean noPlayerWaiting() { return waitingPlayer == null; }
}
