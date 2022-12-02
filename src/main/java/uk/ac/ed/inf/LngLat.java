package uk.ac.ed.inf;


import com.mapbox.geojson.Point;

/**
 * Represents a point in the map.
 * @param lng Longitude of the point.
 * @param lat Latitude of the point.
 */
public record LngLat (Double lng, Double lat){

    private static final double DISTANCE_TOLERANCE = 0.00015;


    /**
     * Checks if a point is within the Central Area. It counts how many times a 'ray' coming from the
     * point being tested, intersects the edges of the polygon. If this number is even, the point
     * lies outside the polygon; if odd, it belongs inside.
     * @return True if the point is in the Central Area.
     */
    public boolean inCentralArea() {

        // Gets coordinates from the REST server.
        LngLat[] centralCoordinates = CentralAreaClient.getCentralCoordinates();

        // If there are less than 3 points, it is not a polygon.
        if (centralCoordinates.length < 3) {
            return false;
        }

        // Counts the intersections.
        int count = 0;
        for (int i = 0; i < centralCoordinates.length; i++) {

            // Longitudes and latitudes of the current point and next one.
            Double lngPoint = centralCoordinates[i].lng();
            Double latPoint = centralCoordinates[i].lat();
            Double lngNextPoint = centralCoordinates[(i+1)%centralCoordinates.length].lng();
            Double latNextPoint = centralCoordinates[(i+1)%(centralCoordinates.length)].lat();

            // Checks if testPoint is between the latitudes of two points.
            if ((this.lat <= latPoint && this.lat >= latNextPoint) ||
                    (this.lat >= latPoint && this.lat <= latNextPoint)) {

                // Checks if the line between the two points is vertical.
                if (lngPoint == lngNextPoint) {

                    // Checks that the testPoint is within the vertical edge.
                    if (this.lng == lngPoint) {
                        return true;
                    }

                    // If the longitude of testPoint is smaller, then it intersects.
                    if (this.lng < lngPoint) {
                        count++;
                        continue;

                    }
                    continue;
                }

                // Checks if the testPoint is within a non-vertical edge
                Double slopeOfLine = (latNextPoint - latPoint) / (lngNextPoint - lngPoint);
                Double intercept = latPoint - (slopeOfLine * lngPoint);

                if (this.lat == (slopeOfLine * this.lng) + intercept) {
                    // Checks for a horizontal edge.
                    if (slopeOfLine == 0 &&
                            ((this.lng < lngPoint && this.lng < lngNextPoint) ||
                            (this.lng > lngPoint && this.lng > lngNextPoint))) {
                        return false;
                    } else {
                        return true;
                    }
                }

                if (this.lat == latPoint) {
                    if (this.lng < lngPoint) {
                        continue;
                    }
                }

                // Checks for a peak in a polygon.
                Double latNextNextPoint = centralCoordinates[(i+2)%centralCoordinates.length].lng();
                Double lngNextNextPoint = centralCoordinates[(i+2)%(centralCoordinates.length)].lat();

                if (this.lng < lngNextPoint
                        && ((latNextPoint > latPoint && latNextPoint > latNextNextPoint)
                            || (latNextPoint < latPoint && latNextPoint < latNextNextPoint))
                        && this.lat == latNextPoint
                        && lngPoint != lngNextNextPoint) {
                    count++;
                }

                // If the edge is non-vertical, check if testPoint intersects.
                Double intersects = (((lngNextPoint - lngPoint) / (latNextPoint - latPoint)) * (this.lat - latPoint))
                        + lngPoint;

                if (this.lng < intersects) {
                    count++;
                }

            }
        }

        // If the count is odd, then the point is within the Central Area.
        return (count % 2 != 0);
    }

    public Double getAngleFromLine(LngLat point) {
        double angle = Math.toDegrees(Math.atan2(point.lat() - this.lat(),
                point.lng()) - this.lng());
        if(angle < 0){
            angle += 360;
        }
        if(angle > 360){
            angle -= 360;
        }
        return angle;
    }

    /**
     * Calculates the distance between two points.
     * @param point Calculates the distance to this point (LngLat object).
     * @return distance between points
     */
    public Double distanceTo(LngLat point) {
        Double latDifference = Math.pow(this.lat - point.lat(), 2);
        Double lngDifference = Math.pow(this.lng - point.lng(), 2);
        return Math.sqrt(latDifference + lngDifference);
    }

    /**
     * Checks if a point is within the distance tolerance.
     * @param point A LngLat object.
     * @return true if it is within that distance.
     */
    public boolean closeTo(LngLat point) {
        return distanceTo(point) < DISTANCE_TOLERANCE;
    }

    /**
     * Calculates the position of the drone after moving towards a certain direction.
     * @param direction A compass direction.
     * @return The position calculated.
     */
    public LngLat nextPosition(Compass direction) {

        // If the drone is hovering.
        if (direction == null) {
            return this;
        }

        Double angle = Math.toRadians(direction.getAngle());
        Double newLat = this.lat + (DISTANCE_TOLERANCE * Math.sin(angle));
        Double newLng = this.lng + (DISTANCE_TOLERANCE * Math.cos(angle));
        return new LngLat(newLng, newLat);
    }

    public Node toNode() {
        return new Node(this);
    }

    public Point toPoint() {
        return Point.fromLngLat(this.lng(), this.lat());
    }


}
