package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NoFlyZones extends Polygon {

    public static ArrayList<ArrayList<LngLat>> getNoFlyZonesFromServer() {
        String endpoint = "noFlyZones";
        String url = "https://ilp-rest.azurewebsites.net/noFlyZones";

        try {

            NfzPoint[] noFlyZones = new ObjectMapper().readValue(new URL(url), NfzPoint[].class);

            ArrayList<ArrayList<LngLat>> lala = new ArrayList<>();
            for (NfzPoint noFlyZone : noFlyZones) {
                ArrayList<LngLat> zones1 = new ArrayList<>();
                Double[][] coordinates = noFlyZone.getCoordinates();
                for (Double[] coordinate : coordinates) {
                    LngLat point = new LngLat(coordinate[0], coordinate[1]);
                    zones1.add(point);
                }
                lala.add(zones1);
            }

            return lala;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        //return null;
    }

}
