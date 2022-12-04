package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

    private static final String endPoint = "restaurants";

    /**
     * Default constructor.
     */
    Restaurant(String name, double lng, double lat, Menu[] menus) {
        this.name = name;
        this.lng = lng;
        this.lat = lat;
        this.menus = menus;
    }

    Restaurant() {
    }

    public static Restaurant[] getRestaurants() {
        String baseUrl = Server.getInstance().getBaseUrl();
        String url = baseUrl + endPoint;
        try {
            return new ObjectMapper().readValue(new URL(url), Restaurant[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the price of a menu item with its given name.
     * @return A HashMap of menu items and their prices.
     */
    public HashMap<String, Integer> menuCost() {
        HashMap<String, Integer> menu = new HashMap<>();
        for (Menu m : menus) {
            menu.put(m.getName(), m.getPriceInPence());
        }
        return menu;
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


    /**
     * Compares restaurants with respect to how far away they are from the starting position.
     * @param restaurant the object to be compared.
     * @return 0 if the restaurants are the same distance away,-1 if the restaurant is closer,
     * 1 if the restaurant is further away.
     */
    @Override
    public int compareTo(Restaurant restaurant) {
        LngLat coord1 = new LngLat(this.getLng(), this.getLat());
        LngLat coord2 = new LngLat(restaurant.getLng(), restaurant.getLat());
        double dist1 = Drone.STARTING_POINT.distanceTo(coord1);
        double dist2 = Drone.STARTING_POINT.distanceTo(coord2);
        return Double.compare(dist1, dist2);
    }

    /**
     * Sorts restaurants with respect to how far away they are from the starting position.
     * @param restaurants List of restaurants to be sorted.
     */
    public static void sortRestaurants(ArrayList<Restaurant> restaurants) {
        Collections.sort(restaurants);
    }

}
