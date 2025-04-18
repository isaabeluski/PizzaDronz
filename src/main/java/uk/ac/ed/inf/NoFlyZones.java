package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton used to retrieve the no-fly-zones from REST server.
 */
public class NoFlyZones {

    // Nested class which represents a single no-fly-zone.
    private static class SingleNoFlyZone {

        @JsonProperty("name")
        private String name;

        @JsonProperty("coordinates")
        private Double[][] coordinates;

        public Double[][] getCoordinates() {
            return coordinates;
        }

    }

    private final ArrayList<Line2D.Double> noFlyLines;
    private static NoFlyZones noFlyZones;
    private static final String endPoint = "noFlyZones";

    private NoFlyZones() {
        SingleNoFlyZone[] nfzPoints = getNoFlyPoints();
        this.noFlyLines = getNoFlyLines(nfzPoints);
    }

    /**
     * Gets the singleton instance of NoFlyZones.
     * @return The singleton instance of NoFlyZones.
     */
    public static NoFlyZones getInstance() {
        if (noFlyZones == null) {
            noFlyZones = new NoFlyZones();
        }
        return noFlyZones;
    }

    /**
     * Gets the no-fly-zones from the REST server
     * @return A list of the no-fly-zones.
     */
    private SingleNoFlyZone[] getNoFlyPoints() {
        String baseUrl = Server.getInstance().getBaseUrl();

        String url = baseUrl + endPoint;

        try {
            return new ObjectMapper().readValue(new URL(url), SingleNoFlyZone[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts the points which form the different no-fly-zones into Line2D objects.
     * @param noFlyPoints The no-fly-zones points.
     * @return A list of the no-fly-zones lines.
     */
    private ArrayList<Line2D.Double> getNoFlyLines(SingleNoFlyZone[] noFlyPoints) {

        ArrayList<Line2D.Double> noFlyLines = new ArrayList<>();

        for (SingleNoFlyZone noFlyZone : noFlyPoints) {
            Double[][] coordinates = noFlyZone.getCoordinates();
            for (int i = 0; i < coordinates.length - 1; i++) {
                Line2D.Double line = new Line2D.Double(
                        coordinates[i][0],
                        coordinates[i][1],
                        coordinates[i + 1][0],
                        coordinates[i + 1][1]);
                noFlyLines.add(line);
            }
        }
        return noFlyLines;
    }

    /**
     * Checks if a line created by two points intersects with any of the no-fly-zones.
     * @param start The start point of the line.
     * @param destination The end point of the line.
     * @return True if the line intersects with any of the no-fly-zones, false otherwise.
     */
    public boolean intersectsNoFlyZone(Node start, Node destination) {

        // Creates a line from the start and destination points.
        var x = start.getPoint().toPoint();
        var y = destination.getPoint().toPoint();
        var line = new Line2D.Double(x.longitude(), x.latitude(),
                y.longitude(), y.latitude());

        // Checks if the line intersects with any of the no-fly-zones.
        for (var noFlyLine : this.noFlyLines) {
                if (noFlyLine.intersectsLine(line) || line.intersectsLine(noFlyLine)) {
                    return true;
                }
            }

        return false;
    }

}
