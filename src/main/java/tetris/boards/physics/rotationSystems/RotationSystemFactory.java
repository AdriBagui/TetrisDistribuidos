package tetris.boards.physics.rotationSystems;

import tetris.boards.components.BoardGrid;

/**
 * Factory class for creating specific {@link RotationSystem} instances.
 * <p>
 * Decouples the creation logic from the board classes, allowing easy switching between
 * different physics rules (e.g., NES vs. Modern).
 * </p>
 */
public class RotationSystemFactory {
    /**
     * Creates a RotationSystem instance based on the provided type.
     *
     * @param type The type of rotation system to create.
     * @param grid The game grid the system will operate on.
     * @return A new instance of the requested {@link RotationSystem}.
     */
    public static RotationSystem createRotationSystem(RotationSystemType type, BoardGrid grid) {
        RotationSystem rotationSystem = null;

        switch (type) {
            case NES_ROTATION_SYSTEM:
                rotationSystem = new NESRotationSystem(grid);
                break;
            case SUPER_ROTATION_SYSTEM:
                new SuperRotationSystem(grid);
                break;
            case SUPER_ROTATION_SYSTEM_PLUS:
                new SuperRotationSystemPlus(grid);
                break;
        };

        return rotationSystem;
    }
}