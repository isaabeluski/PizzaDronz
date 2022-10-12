package uk.ac.ed.inf;


/**
 * Class used for representing a point
 * @param lng
 * @param lat
 */
public record LngLat (double lng, double lat){

    private static final double distanceTolerance = 0.00015;

    /**
     * Checks if a point is within the Central Area. It counts how many times a 'ray' coming from the
     * point being tested, intersects the edges of the polygon. If this number is even, the point
     * lies outside the polygon; if odd, it belongs inside.
     * @return True if the point is in the Central Area.
     */
    public boolean inCentralArea() {

        // Gets coordinates from the REST server
        LngLat[] centralCoordinates = CentralAreaClient.getInstance().centralCoordinates();

        // If there are less than 3 points, it is not a polygon
        if (centralCoordinates.length < 3) {
            return false;
        }

        int count = 0;
        for (int i = 0; i < centralCoordinates.length; i++) {

            // Longitudes and latitudes of the current point and next one.
            double lngPoint = centralCoordinates[i].lng();
            double latPoint = centralCoordinates[i].lat();
            double lngNextPoint = centralCoordinates[(i+1)%centralCoordinates.length].lng();
            double latNextPoint = centralCoordinates[(i+1)%(centralCoordinates.length)].lat();

            // Checks if testPoint is between the latitudes of two points
            if ((this.lat <= latPoint && this.lat >= latNextPoint) ||
                    (this.lat >= latPoint && this.lat <= latNextPoint)) {

                // Checks if the line between the two points is vertical
                if (lngPoint == lngNextPoint) {

                    // Checks that the testPoint is within the vertical edge
                    if (this.lng == lngPoint) {
                        return true;
                    }

                    // If the longitude of testPoint is smaller, then it intersects
                    if (this.lng < lngPoint) {
                        count++;
                        continue;

                    }
                    continue;
                }

                // Checks if the testPoint is within a non-vertical edge
                double slopeOfLine = (latNextPoint - latPoint) / (lngNextPoint - lngPoint);
                double intercept = latPoint - (slopeOfLine * lngPoint);

                if (this.lat == (slopeOfLine * this.lng) + intercept) {
                    return true;
                }

                // If the edge is non-vertical, check if testPoint intersects
                double slope = (lngNextPoint - lngPoint) / (latNextPoint - latPoint);
                double p = (slope * (this.lat - latPoint)) + lngPoint;

                if (this.lng <= p) {
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
     * Calculates the position of the drone after moving towards a certain direction.
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
