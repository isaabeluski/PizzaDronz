package uk.ac.ed.inf;

public class CentralArea {
    public String name;
    public double lng;
    public double lat;

    public CentralArea(String name, double lng, double lat) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
