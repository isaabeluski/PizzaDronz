package uk.ac.ed.inf;

public class CentralArea {
    public String name;
    public double longitude;
    public double latitude;

    public CentralArea(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    CentralArea() {

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
