package uk.ac.ed.inf;

import org.apache.commons.validator.GenericValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Hello world!
 *
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

        // GETS ALL DATA FROM REST SERVER
        String date = "2023-01-01"; // args[0];
        String baseURL = "https://ilp-rest.azurewebsites.net/"; // args[1];
        String random = "random"; //args[2];


        // CHECKS IF DATE IS VALID
        SimpleDateFormat formatterOrderDate = new SimpleDateFormat("yyyy-MM-dd");
        if (!GenericValidator.isDate(date, formatterOrderDate.toPattern(), true))  {
            System.out.println("Invalid date format. Please use yyyy-MM-dd");
            System.exit(1);
        }
        try {
            Date orderDate = formatterOrderDate.parse(date);
            Date startDates = formatterOrderDate.parse("2023-01-01");
            Date endDates = formatterOrderDate.parse("2023-05-31");

            if (orderDate.before(startDates) || orderDate.after(endDates)) {
                System.out.println("The dates must be between 2023-01-01 and 2023-05-31.");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("The date format is not correct. Please use yyyy-MM-dd.");
            System.exit(1);
        }

        // CHECK IF BASE URL IS VALID
        if (!GenericValidator.isUrl(baseURL)) {
            System.out.println("Invalid base url.");
            System.exit(1);
        }

        // Setup server information from args.
        Server server = Server.getInstance();
        server.setBaseUrl(baseURL);
        server.setDate(date);

        ArrayList<Order> orders = Order.getOrders();
        Restaurant[] restaurants = Restaurant.getRestaurants();
        OrderAdministration day = new OrderAdministration(orders, restaurants);


        // ALL ORDERS OF A DAY
        Drone drone = new Drone(day);
        drone.makeDeliveries(restaurants);

    }
}
