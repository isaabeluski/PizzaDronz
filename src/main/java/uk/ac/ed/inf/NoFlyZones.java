package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Singleton used to retrieve NoFlyZones from REST server.
 */
public class NoFlyZones {

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
        String url = "https://ilp-rest.azurewebsites.net/noFlyZones";
        try {
            return new ObjectMapper().readValue(new URL(url), SingleNoFlyZone[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts the no-fly-zones points into Line2D objects.
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
        //build line
        var x = start.getPoint().toPoint();
        var y = destination.getPoint().toPoint();
        var line = new Line2D.Double(x.longitude(), x.latitude(),
                y.longitude(), y.latitude());

        for (var noFlyLine : this.noFlyLines) {
                if (noFlyLine.intersectsLine(line) || line.intersectsLine(noFlyLine)) {
                    return true;
                }
            }
        return false;
    }


}
