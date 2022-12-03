package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

    private static Server server;
    private final ArrayList<Line2D.Double> centralAreaLines;
    private final ArrayList<Line2D.Double> noFlyLines;
    private final Restaurant[] restaurants;


    private Server() {
        CentralAreaPoint[] centralAreaPoints = centralCoordinates();
        this.centralAreaLines = centralLines(centralAreaPoints);
        SingleNoFlyZone[] nfzPoints = getNoFlyPoints();
        this.noFlyLines = noFlyLines(nfzPoints);
        this.restaurants = getRestaurantsServer();
    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    private CentralAreaPoint[] centralCoordinates() {
        try {
            URL url = new URL("https://ilp-rest.azurewebsites.net/centralArea");

            return new ObjectMapper().readValue(url, CentralAreaPoint[].class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Line2D.Double> centralLines(CentralAreaPoint[] centralAreas) {
        CentralAreaPoint firstPoint = centralAreas[0];
        ArrayList<Line2D.Double> centralAreaLines = new ArrayList<>();
        var cap = new ArrayList<>(Arrays.stream(centralAreas).toList());
        cap.add(firstPoint);

        for (int i = 0; i < cap.size() - 1; i++) {
            Line2D.Double line = new Line2D.Double(
                    cap.get(i).getLng(),
                    cap.get(i).getLat(),
                    cap.get(i+1).getLng(),
                    cap.get(i+1).getLat());
            centralAreaLines.add(line);
        }
        return centralAreaLines;
    }

    private SingleNoFlyZone[] getNoFlyPoints() {
        String url = "https://ilp-rest.azurewebsites.net/noFlyZones";
        try {
            return new ObjectMapper().readValue(new URL(url), SingleNoFlyZone[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Line2D.Double> noFlyLines(SingleNoFlyZone[] noFlyPoints) {
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

    private Restaurant[] getRestaurantsServer() {
        String url = "https://ilp-rest.azurewebsites.net/restaurants";
        try {
            return new ObjectMapper().readValue(new URL(url), Restaurant[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Line2D.Double> getCentralAreaLines() {
        return centralAreaLines;
    }

    public ArrayList<Line2D.Double> getNoFlyLines() {
        return noFlyLines;
    }

    public Restaurant[] getRestaurants() {
        return restaurants;
    }
}
