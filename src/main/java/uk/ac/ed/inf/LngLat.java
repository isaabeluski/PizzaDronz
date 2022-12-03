package uk.ac.ed.inf;


import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.turf.TurfJoins;

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
    /*
    public boolean inCentralArea() {

        // Gets Polygon from the REST server.
        Polygon centralAreaPolygon = CentralAreaClient.getCentralCoordinates();

        // Point to be tested
        Point point = this.toPoint();

        return TurfJoins.inside(point, centralAreaPolygon);
    }
     */



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
