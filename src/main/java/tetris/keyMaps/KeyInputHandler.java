package tetris.keyMaps;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyInputHandler extends KeyAdapter {
    Map<KeyEvent, InputAction> actionsKeyEventMap;

    public KeyInputHandler() {
        actionsKeyEventMap = new HashMap<>();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        InputAction action = actionsKeyEventMap.get(e);

        switch (action) {
        }
    }
}
