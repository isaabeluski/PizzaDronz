package uk.ac.ed.inf;

import org.apache.commons.validator.GenericValidator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Class where the code is executed.
 */
 public class App
{
    public static void main( String[] args ) {

        /*
        if (args.length != 3) {
            System.out.println("Three arguments are needed: date, base url and random seed.");
            System.exit(1);
        }
         */

        // GETS DATA FROM COMMAND LINE
        String date = "2023-03-01"; // args[0];
        String baseURL = "https://ilp-rest.azurewebsites.net/"; // args[1];
        String random = "random"; //args[2];

        Server server = Server.getInstance();

        // CHECKS IF DATE INTRODUCED IS VALID
        server.validDate(date);

        // CHECK IF BASE URL IS VALID
        server.validUrl(baseURL);

        // Setup server information from args.
        server.setBaseUrl(baseURL);
        server.setDate(date);

        ArrayList<Order> orders = Order.getOrders();
        Restaurant[] restaurants = Restaurant.getRestaurants();

        // ALL ORDERS OF A DAY
        Drone drone = new Drone(orders, restaurants);
        drone.makeDeliveries();

        // OUTPUT FILES
        drone.outputDroneGeoJson(drone.getCompleteTour());
        Flightpath.outputJsonFlightpath(drone.getFlightpath());
        Deliveries.outputJsonDeliveries(drone.getDeliveries());

    }
}
