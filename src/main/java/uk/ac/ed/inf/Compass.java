package uk.ac.ed.inf;

/**
 * Directions the drone can move towards.
 */
public enum Compass {

    /**
     * Drone moves North (90º angle).
     */
    NORTH(90.0),

    /**
     * Drone moves South (270º angle).
     */
    SOUTH(270.0),

    /**
     * Drone moves East (0º angle).
     */
    EAST(0.0),

    /**
     * Drone moves West (180º angle).
     */
    WEST(180.0),

    /**
     * Drone moves North East (45º angle).
     */
    NORTH_EAST(45.0),

    /**
     * Drone moves North West (135º angle).
     */
    NORTH_WEST(135.0),

    /**
     * Drone moves South East (315º angle).
     */
    SOUTH_EAST(315.0),

    /**
     * Drone moves South West (225º angle).
     */
    SOUTH_WEST(225.0),

    /**
     * Drone moves North North East (67.5º angle).
     */
    NORTH_NORTH_EAST(67.5),

    /**
     * Drone moves East North East (22.5º angle).
     */
    EAST_NORTH_EAST(22.5),

    /**
     * Drone moves East South East (337.5º angle).
     */
    EAST_SOUTH_EAST(337.5),

    /**
     * Drone moves South South East (292.5º angle).
     */
    SOUTH_SOUTH_EAST(292.5),

    /**
     * Drone moves South South West (247.5º angle).
     */
    SOUTH_SOUTH_WEST(247.5),

    /**
     * Drone moves West South West (202.5º angle).
     */
    WEST_SOUTH_WEST(202.5),

    /**
     * Drone moves West North West (157.5º angle).
     */
    WEST_NORTH_WEST(157.5),

    /**
     * Drone moves North North West (112.5º angle).
     */
    NORTH_NORTH_WEST(112.5);

    private final double angle;

    /**
     * Constructor.
     * @param angle Angle the drone will move towards.
     */
    Compass(double angle) {
        this.angle = angle;
    }

    /**
     * Gets the angle of a direction.
     * @return Angle in degrees.
     */
    public double getAngle() {
        return angle;
    }

}
