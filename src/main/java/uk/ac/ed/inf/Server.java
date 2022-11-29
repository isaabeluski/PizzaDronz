package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to generate one instance of every output from REST server.
 */
public class Server {

    public Restaurant[] restaurants;
    public ArrayList<ArrayList<LngLat>> noFlyZones;
    public ArrayList<Order> orders;
    public LngLat[] centralCoordinates;
    private final String BASE_ADDRESS = "https://ilp-rest.azurewebsites.net/";

    public Server() {
        this.restaurants = getRestaurants("restaurants");
        this.noFlyZones = getNoFlyZones("noFlyZones");
        this.orders = getOrders("orders", "2023-01-01");
        this.centralCoordinates = getCentralCoordinates("centralArea");
    }

    public ArrayList<ArrayList<LngLat>> getNoFlyZones(String endPoint) {

        String url = BASE_ADDRESS + endPoint;

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

    public LngLat[] getCentralCoordinates(String endPoint) {

        String url = BASE_ADDRESS + endPoint;

        try {

            CentralAreaPoint[] centralAreas = new ObjectMapper().readValue(new URL(url), CentralAreaPoint[].class);

            // Converts the Central Areas points into LngLat objects.
            LngLat[] coordinates = new LngLat[centralAreas.length];
            int i = 0;
            for (CentralAreaPoint point : centralAreas) {
                coordinates[i] = new LngLat(point.getLng(), point.getLat());
                i++;
            }

            return coordinates;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Order> getOrders(String endPoint, String date) {
        String url = BASE_ADDRESS + endPoint + '/' + date;
        try {
            Order[] orders = new ObjectMapper().readValue(new URL(url), Order[].class);
            return new ArrayList<>(List.of(orders));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Restaurant[] getRestaurants(String endPoint) {
        String url = BASE_ADDRESS + endPoint;
        try {
            return new ObjectMapper().readValue(new URL(url), Restaurant[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
