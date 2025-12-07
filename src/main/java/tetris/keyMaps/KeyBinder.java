package tetris.keyMaps;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyBinder {
    Map<InputAction, KeyEvent> actionsKeyEventMap;

    public KeyBinder() {
        actionsKeyEventMap = new HashMap<>();
    }
}
