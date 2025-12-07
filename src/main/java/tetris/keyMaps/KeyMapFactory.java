package tetris.keyMaps;

import main.MainPanel;

public class KeyMapFactory {
    public static final int NES_MODE = 0;
    public static final int TETRIO_MODE = 1;

    public static SinglePlayerKeyMap createSinglePlayerKeyMap(int type, MainPanel mainPanel) {
        SinglePlayerKeyMap singlePlayerKeyMap = null;

        switch (type) {
            case NES_MODE:
                singlePlayerKeyMap = new SinglePlayerNESKeyMap(mainPanel);
                break;
            case TETRIO_MODE:
                singlePlayerKeyMap = new SinglePlayerTetrioKeyMap(mainPanel);
                break;
        }

        return singlePlayerKeyMap;
    }

    public static TwoPlayersKeyMap createTwoPlayersKeyMap(int type, MainPanel mainPanel) {
        TwoPlayersKeyMap twoPlayersKeyMap = null;

        switch (type) {
            case NES_MODE:
                twoPlayersKeyMap = new TwoPlayersNESKeyMap(mainPanel);
                break;
            case TETRIO_MODE:
                twoPlayersKeyMap = new TwoPlayersTetrioKeyMap(mainPanel);
                break;
        }

        return twoPlayersKeyMap;
    }
}
