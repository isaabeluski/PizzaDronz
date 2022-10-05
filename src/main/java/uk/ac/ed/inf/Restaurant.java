package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    public String name;
    public double lng;
    public double ltd;
    public Menu[] menus;

    public Restaurant(String name, double lng, double ltd, Menu[] menus){
        this.name = name;
        this.lng = lng;
        this.ltd = ltd;
        this.menus = menus;
    }

    public static Restaurant[] getRestaurantFromRestServer(URL serverBaseAddress) {
        try {
            Restaurant[] restaurants =
                    new ObjectMapper().readValue(serverBaseAddress, Restaurant[].class);
            return restaurants;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Menu[] getMenu() {
        return menus;
    }
}
