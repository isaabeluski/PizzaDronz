package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CentralArea {
    public String name;
    public double lng;
    public double lat;

    CentralArea() {
    }

    @JsonProperty("longitude")
    public double getLng() {
        return lng;
    }

    @JsonProperty("latitude")
    public double getLat() {
        return lat;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
}
