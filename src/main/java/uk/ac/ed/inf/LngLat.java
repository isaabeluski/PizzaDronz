package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.util.List;

public record LngLat (double lng, double lat){

    private static final double distanceTolerance = 0.00015;

    /**
     * Checks if a point is within the Central Area.
     * @return true if point is in Central Area
     */
    public boolean inCentralArea() {

        // Gets coordinates from the REST server
        List<LngLat> centralCoordinates = CentralAreaClient.getInstance().centralCoordinates();

        if (centralCoordinates.size() < 3) {
            return false;
        }

        int count = 0;
        for (int i = 0; i < centralCoordinates.size() - 1; i++) {
            // if point is between the longitudes of two points, and its latitude is less that one of the points
            // then we add one to the count
            if ((this.lng < centralCoordinates.get(i).lng()
                    && this.lng > centralCoordinates.get(i+1).lng()) ||
                    (this.lng > centralCoordinates.get(i).lng()
                            && this.lng < centralCoordinates.get(i+1).lng())) {
                if (this.lat < centralCoordinates.get(i).lat()
                        || this.lat < centralCoordinates.get(i+1).lat()) {
                    count++;
                }
            }
        }

        // If the count is odd, then the point is within the Central Area
        return (count % 2 != 0);
    }

    /**
     * Calculates the distance between two points.
     * @param point
     * @return distance between points
     */
    public double distanceTo(LngLat point) {
        double latDifference = Math.pow(this.lat - point.lat(), 2);
        double lngDifference = Math.pow(this.lng - point.lng(), 2);
        return Math.sqrt(latDifference + lngDifference);
    }

    /**
     * Checks if a point is within the distance tolerance.
     * @param point
     * @return true if it is within that distance.
     */
    public boolean closeTo(LngLat point) {
        return distanceTo(point) < distanceTolerance;
    }

    /**
     * Calculates the position after the drone does one move towards a certain direction.
     * @param direction - a compass direction.
     * @return the position calculated.
     */
    public LngLat nextPosition(Compass direction) {
        double angle = Math.toRadians(direction.getAngle());
        double newLat = distanceTolerance * Math.cos(angle);
        double newLng = distanceTolerance * Math.sin(angle);
        return new LngLat(newLng, newLat);
    }


}
