package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the points of the Central Area.
 */

public class CentralAreaPoint {

    @JsonProperty("name")
    private String name;

    @JsonProperty("longitude")
    private double lng;

    @JsonProperty("latitude")
    private double lat;

    CentralAreaPoint() {
    }

    /**
     * Gets the longitude of a point.
     * @return The longitude.
     */
    public double getLng() {
        return lng;
    }

    /**
     * Gets the latitude of a point.
     * @return The latitude.
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets the name of the point.
     * @return The name.
     */
    public String getName() {
        return name;
    }
}
