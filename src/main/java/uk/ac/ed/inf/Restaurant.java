package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class Restaurant {

    @JsonProperty("name")
    private String name;

    @JsonProperty("longitude")
    private double lng;

    @JsonProperty("latitude")
    private double ltd;

    @JsonProperty("menu")
    private Menu[] menus;

    public Restaurant(String name, double lng, double ltd, Menu[] menus){
        this.name = name;
        this.lng = lng;
        this.ltd = ltd;
        this.menus = menus;
    }

    public Restaurant() {

    }

    /**
     * Gets restaurants from the REST server
     * @param serverBaseAddress The URL
     * @return List of restaurants
     */
    public static Restaurant[] getRestaurantFromRestServer(URL serverBaseAddress) {
        try {
            Restaurant[] restaurants =
                    new ObjectMapper().readValue(serverBaseAddress, Restaurant[].class);
            return restaurants;
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
