package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.*;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.mapbox.turf.TurfJoins;


public class NoFlyZones {

    private final ArrayList<Line2D.Double> noFlyLines;
    private static NoFlyZones noFlyZones;

    private NoFlyZones() {
        this.noFlyLines = getNoFlyLines("noFlyZones");
    }

    public static NoFlyZones getInstance() {
        if (noFlyZones == null) {
            noFlyZones = new NoFlyZones();
        }
        return noFlyZones;
    }


    public ArrayList<Line2D.Double> getNoFlyLines(String endPoint) {
        String url = "https://ilp-rest.azurewebsites.net/" + endPoint;
        ArrayList<Line2D.Double> noFlyLines = new ArrayList<>();
        try {
            NfzPoint[] noFlyZones = new ObjectMapper().readValue(new URL(url), NfzPoint[].class);
            for (NfzPoint noFlyZone : noFlyZones) {
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
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean inFlyZones(List<Polygon> noFlyZones) {
        for (Polygon noFlyZone : noFlyZones) {
            if (TurfJoins.inside(Point.fromLngLat(55.9442, -3.1883), noFlyZone)) {
                return true;
            }
        }
        return false;
    }

    public boolean intersecting(Line2D.Double line) {
        for (Line2D.Double noFlyLine : noFlyLines) {
            if (line.intersectsLine(noFlyLine) || noFlyLine.contains(line.getP2()) || noFlyLine.contains(line.getP1())) {
                return true;
            }
        }
        return false;
    }


}
