package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;

public class RotationSystemFactory {
    public static final int NES_ROTATION_SYSTEM = 0;
    public static final int SUPER_ROTATION_SYSTEM = 1;
    public static final int SUPER_ROTATION_SYSTEM_PLUS = 2;

    public static RotationSystem createRotationSystem(int type, BoardGrid grid) {
        RotationSystem rotationSystem = null;

        switch (type) {
            case NES_ROTATION_SYSTEM:
                rotationSystem = new NESRotationSystem(grid);
                break;
            case SUPER_ROTATION_SYSTEM:
                rotationSystem = new SuperRotationSystem(grid);
                break;
            case SUPER_ROTATION_SYSTEM_PLUS:
                rotationSystem = new SuperRotationSystemPlus(grid);
                break;
        }

        return rotationSystem;
    }
}
