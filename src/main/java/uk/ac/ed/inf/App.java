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
        String date = "2023-01-01"; // args[0];
        String baseURL = "https://ilp-rest.azurewebsites.net/"; // args[1];
        String random = "random"; //args[2];


        // CHECKS IF DATE INTRODUCED IS VALID
        SimpleDateFormat formatterOrderDate = new SimpleDateFormat("yyyy-MM-dd");
        if (!GenericValidator.isDate(date, formatterOrderDate.toPattern(), true))  {
            System.out.println("Invalid date format. Please use yyyy-MM-dd");
            System.exit(1);
        }
        try {
            Date orderDate = formatterOrderDate.parse(date);
            Date startDates = formatterOrderDate.parse("2023-01-01");
            Date endDates = formatterOrderDate.parse("2023-05-30");

            if (orderDate.before(startDates) || orderDate.after(endDates)) {
                System.out.println("The dates must be between 2023-01-01 and 2023-05-30.");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("The date format is not correct. Please use yyyy-MM-dd.");
            System.exit(1);
        }

        // CHECK IF BASE URL IS VALID
        Server.getInstance().validUrl(baseURL);

        // Setup server information from args.
        Server server = Server.getInstance();
        server.setBaseUrl(baseURL);
        server.setDate(date);

        ArrayList<Order> orders = Order.getOrders();
        Restaurant[] restaurants = Restaurant.getRestaurants();

        // ALL ORDERS OF A DAY
        Drone drone = new Drone(orders);
        drone.makeDeliveries(restaurants);

    }
}
