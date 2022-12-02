package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.*;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.mapbox.turf.TurfJoins;


public class NoFlyZones {

    private final ArrayList<Line2D.Double> noFlyLines;
    private static NoFlyZones noFlyZones;

    private NoFlyZones() {
        NfzPoint[] nfzPoints = getNoFlyPoints("noFlyZones");
        this.noFlyLines = getNoFlyLines(nfzPoints);
    }

    public static NoFlyZones getInstance() {
        if (noFlyZones == null) {
            noFlyZones = new NoFlyZones();
        }
        return noFlyZones;
    }


    public NfzPoint[] getNoFlyPoints(String endPoint) {
        String url = "https://ilp-rest.azurewebsites.net/" + endPoint;
        try {
            return new ObjectMapper().readValue(new URL(url), NfzPoint[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Line2D.Double> getNoFlyLines(NfzPoint[] noFlyPoints) {
        ArrayList<Line2D.Double> noFlyLines = new ArrayList<>();
        for (NfzPoint noFlyZone : noFlyPoints) {
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

    public boolean isIntersecting(Node start, Node destination) {
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
