package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.util.List;

public class LngLat {

    public double lng;
    public double lat;
    public double distanceTolerance = 0.00015;

    public LngLat(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public boolean inCentralArea() throws MalformedURLException {

        List<LngLat> centralCoordinates = CentralAreaClient.getInstance().centralCoordinates();

        if (centralCoordinates.size() < 3) {
            return false;
        }

        int count = 0;
        for (int i = 0; i < centralCoordinates.size() - 1; i++) {
            if (this.lng < centralCoordinates.get(i).getLng()
                    && this.lng > centralCoordinates.get(i+1).getLng()) {
                if (this.lat < centralCoordinates.get(i).getLng()
                        || this.lat < centralCoordinates.get(i+1).getLng()) {
                    count++;
                }
            }
        }

        return (count % 2 != 0);
    }

    public double distanceTo(LngLat point) {
        double latDifference = Math.pow(this.lat - point.getLat(), 2);
        double lngDifference = Math.pow(this.lng - point.getLng(), 2);
        return Math.sqrt(latDifference + lngDifference);
    }

    public boolean closeTo(LngLat point) {
        return distanceTo(point) < distanceTolerance;
    }

    public LngLat nextPosition(Compass direction) {
        double angle = direction.getAngle();
        double newLat = distanceTolerance * Math.cos(angle);
        double newLng = distanceTolerance * Math.sin(angle);
        LngLat newPosition = new LngLat(newLng, newLat);
        return newPosition;
    }

    // Getters
    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

}
