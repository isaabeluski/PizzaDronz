package uk.ac.ed.inf;

public enum Compass {

    NORTH(90.0),
    SOUTH(270.0),
    EAST(0.0),
    WEST(180.0),
    NORTH_EAST(45.0),
    NORTH_WEST(135.0),
    SOUTH_EAST(315.0),
    SOUTH_WEST(225.0),
    NORTH_NORTH_EAST(67.5),
    EAST_NORTH_EAST(22.5),
    EAST_SOUTH_EAST(337.5),
    SOUTH_SOUTH_EAST(292.5),
    SOUTH_SOUTH_WEST(247.5),
    WEST_SOUTH_WEST(202.5),
    WEST_NORTH_WEST(157.5),
    NORTH_NORTH_WEST(112.5);

    private final double angle;

    Compass(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
