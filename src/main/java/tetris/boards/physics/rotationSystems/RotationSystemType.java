package tetris.boards.physics.rotationSystems;

/**
 * Enumeration of the supported rotation algorithms.
 * <p>
 * Determines which ruleset is used for wall kicks and rotation states in a {@link tetris.boards.BoardWithPhysics}.
 * </p>
 */
public enum RotationSystemType {
    /**
     * Nintendo Rotation System (used in classic NES Tetris).
     * No wall kicks, strict rotation rules.
     */
    NES_ROTATION_SYSTEM,

    /**
     * Super Rotation System (SRS).
     * The standard for modern Tetris, featuring extensive wall kicks.
     */
    SUPER_ROTATION_SYSTEM,

    /**
     * Super Rotation System Plus (SRS+).
     * A variation of SRS with modified wall kicks for the I-piece (often used in competitive web clients).
     */
    SUPER_ROTATION_SYSTEM_PLUS
}