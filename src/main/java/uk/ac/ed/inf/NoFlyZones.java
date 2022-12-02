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

    private final ArrayList<Polygon> noFlyPolygons;

    private NoFlyZones() {
        NfzPoint[] nfzPoints = getNoFlyPoints("noFlyZones");
        this.noFlyLines = getNoFlyLines(nfzPoints);
        this.noFlyPolygons = getNoFlyPolygons(nfzPoints);
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

    public ArrayList<Polygon> getNoFlyPolygons(NfzPoint[] noFlyPoints) {
        ArrayList<Polygon> noFlyPolygons = new ArrayList<>();
        for (NfzPoint point : noFlyPoints) {
            Double[][] coordinates = point.getCoordinates();
            List<Point> points = new ArrayList<>();
            List<List<Point>> allPoints = new ArrayList<>();

            for (Double[] coordinate : coordinates) {
                points.add(Point.fromLngLat(coordinate[0], coordinate[1]));
            }
            allPoints.add(points);
            noFlyPolygons.add(Polygon.fromLngLats(allPoints));
        }
        return noFlyPolygons;
    }

    public boolean inFlyZones(Node node) {
        for (Polygon noFlyZone : noFlyPolygons) {
            if (TurfJoins.inside(Point.fromLngLat(node.getPoint().lng(), node.getPoint().lat()), noFlyZone)) {
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
