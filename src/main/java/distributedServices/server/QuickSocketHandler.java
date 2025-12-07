package distributedServices.server;

import java.net.Socket;

public class QuickSocketHandler {
    private Socket quickPlayPlayer;

    public QuickSocketHandler() {
        quickPlayPlayer = null;
    }

    public synchronized Socket getQuickPlayPlayer() {
        return quickPlayPlayer;
    }

    /**
     * Gets the current quickPlayPlayer and sets it to null.
     * @return Returns the current quickPlayPlayer waiting.
     */
    public synchronized Socket getAndRemoveQuickPlayPlayer() {
        Socket aux = getQuickPlayPlayer();
        setQuickPlayPlayer(null);
        return aux;
    }

    public synchronized void setQuickPlayPlayer(Socket quickPlayPlayer) {
        this.quickPlayPlayer = quickPlayPlayer;
    }

    public synchronized boolean isThereAPlayingWaiting() { return quickPlayPlayer==null; }
}
