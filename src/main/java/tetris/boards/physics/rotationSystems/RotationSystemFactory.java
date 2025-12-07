package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;

public class RotationSystemFactory {
    public static RotationSystem createRotationSystem(RotationSystemType type, BoardGrid grid) {
        return switch (type) {
            case NES_ROTATION_SYSTEM -> new NESRotationSystem(grid);
            case SUPER_ROTATION_SYSTEM -> new SuperRotationSystem(grid);
            case SUPER_ROTATION_SYSTEM_PLUS -> new SuperRotationSystemPlus(grid);
        };
    }
}
