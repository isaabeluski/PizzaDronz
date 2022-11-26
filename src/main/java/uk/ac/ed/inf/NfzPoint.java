package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NfzPoint {

    @JsonProperty("name")
    private String name;

    @JsonProperty("coordinates")
    private Double[][] coordinates;

    public Double[][] getCoordinates() {
        return coordinates;
    }

    public String getName() {
        return name;
    }
}
