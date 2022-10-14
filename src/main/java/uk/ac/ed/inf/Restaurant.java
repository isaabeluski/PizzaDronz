package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents a restaurant and retrieves its data from the REST server.
 */

public class Restaurant {

    @JsonProperty("name")
    private String name;

    @JsonProperty("longitude")
    private double lng;

    @JsonProperty("latitude")
    private double ltd;

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

}
