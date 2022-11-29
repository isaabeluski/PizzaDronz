package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Represents a restaurant and retrieves its data from the REST server.
 */

public class Restaurant implements Comparable<Restaurant> {

    @JsonProperty("name")
    private String name;

    @JsonProperty("longitude")
    private double lng;

    @JsonProperty("latitude")
    private double lat;

    @JsonProperty("menu")
    private Menu[] menus;

    /**
     * Default constructor.
     */
    Restaurant() {
    }


    /**
     * Gets restaurants from the REST server
     * @param serverBaseAddress The URL
     * @return List of restaurants
     */
    public static Restaurant[] getRestaurantFromRestServer(URL serverBaseAddress) {
        String endpoint = "restaurants";
        String url = serverBaseAddress.toString() + endpoint;
        try {
            return new ObjectMapper().readValue(new URL(url), Restaurant[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the menu of a restaurant
     * @return The menu, which is a list of dishes with their respective prices.
     */
    public Menu[] getMenu() {
        return menus;
    }
    public double getLng(){
        return lng;
    }

    public double getLat(){
        return lat;
    }

    public String getName() {
        return name;
    }


    @Override
    public int compareTo(Restaurant restaurant) {
        LngLat coord1 = new LngLat(this.getLng(), this.getLat());
        LngLat coord2 = new LngLat(restaurant.getLng(), restaurant.getLat());
        double dist1 = Drone.APPLETON_TOWER.distanceTo(coord1);
        double dist2 = Drone.APPLETON_TOWER.distanceTo(coord2);
        return Double.compare(dist1, dist2);
    }

    public static void sortRestaurants(ArrayList<Restaurant> restaurants) {
        Collections.sort(restaurants);
    }

}
