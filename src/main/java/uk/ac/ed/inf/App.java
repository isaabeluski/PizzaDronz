package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Hello world!
 *
 */
 public class App
{
    public static void main( String[] args ) {

        // GETS ALL DATA FROM REST SERVER
        String date = "2023-01-01"; // args[0];
        String baseURL = "https://ilp-rest.azurewebsites.net"; // args[1];
        String random = "caca"; //args[2];

        // Setup server information from args
        Server server = Server.getInstance();
        server.setBaseUrl(baseURL);
        server.setDate(date);

        ArrayList<Order> orders = Order.getOrders();
        Restaurant[] restaurants = Restaurant.getRestaurants();
        DayOrder day = new DayOrder(date, orders, restaurants);


        // ALL ORDERS OF A DAY
        Drone drone = new Drone(day.getFilteredOrders());
        drone.makeDeliveries(restaurants);
        //System.out.println(tour);

    }
}
