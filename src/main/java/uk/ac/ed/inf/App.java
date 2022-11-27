package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
 public class App
{
    public static void main( String[] args ) throws MalformedURLException {
        /**
        LngLat appleton = new LngLat(-3.1869,55.9445);
        boolean test = appleton.inCentralArea();
        System.out.println(test);

        System.out.println("-----------------------------------");

        LngLat corner1 = new LngLat(-3.192473, 55.946233);
        boolean testCorner1 = corner1.inCentralArea();
        System.out.println(testCorner1);

        System.out.println("-----------------------------------");

        LngLat inLIne = new LngLat(-3.184319, 55.944425);
        boolean testLine = inLIne.inCentralArea();
        System.out.println(testLine);

        System.out.println("-----------------------------------");

        LngLat point2 = new LngLat(-3.1869,55.9445);
        boolean test2 = point2.inCentralArea();
        System.out.println(test2);
        **/

        String hello = "hello";
        System.out.println(hello.length());


        String baseUrl = "https://ilp-rest.azurewebsites.net/";
        Order order = new Order();
        Order[] orders = order.getOrdersFromServer(new URL(baseUrl), "2023-01-01");
        Restaurant[] restaurants = Restaurant.getRestaurantFromRestServer(new URL(baseUrl));
        ArrayList<ArrayList<LngLat>> noFlyZones = NoFlyZones.getNoFlyZonesFromServer();
        dayOrder day = new dayOrder("2023-01-01", orders, restaurants);
        ArrayList<Order> filteredOrder = day.getFilteredOrders();
        Drone drone = new Drone(filteredOrder);
        drone.doTour(restaurants, noFlyZones);
        //System.out.println(tour);

        //GeoJson geoJson = new GeoJson();
        //geoJson.createJsonFile(geoJson.coordinates(tour));

        /*
        Node point = new LngLat(	-3.1839, 	55.9445).toNode();
        Node point2 = new LngLat(	-3.183873999999998,55.944494).toNode();

        System.out.println(point.getDirection(point2));
         */


    }
}
