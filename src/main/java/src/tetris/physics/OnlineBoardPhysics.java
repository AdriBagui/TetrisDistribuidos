package src.tetris.physics;

import java.net.Socket;

public class OnlineBoardPhysics extends BoardPhysics {
    public OnlineBoardPhysics(boolean[][] grid) {
        super(grid);
        // TODO: Insertar input stream (igual hacen falta más cosas idk)
    }

    @Override
    public void update() {
        // TODO: Aquí va la lógica de leer del input stream
    }

    @Override
    public boolean isLocked() {
        // TODO: Tiene que ser capaz de detectar cuando está bloqueada la ficha (lo recibe en del input stream)
        // bloqueada significa (para mí) que esté en el fondo y ya no se pueda mover, es decir, en el siguiente
        // frame se va a generar una nueva ficha y la ficha va a pasar a ser parte del montón de fichas

        // ESTE RETURN FALSE HABRÁ QUE BORRARLO ES PROVISIONAL PARA QUE NO DE FALLO
        return false;
    }
}
