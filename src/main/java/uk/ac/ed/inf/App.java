package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
 public class App
{
    public static void main( String[] args ) throws MalformedURLException {

        // GETS ALL DATA FROM REST SERVER
        Server server = new Server();
        ArrayList<Order> orders = server.orders;
        Restaurant[] restaurants = server.restaurants;
        DayOrder day = new DayOrder("2023-01-01", orders, restaurants);


        // ALL ORDERS OF A DAY
        Drone drone = new Drone(day.getFilteredOrders());
        ArrayList<LngLat> tour = drone.doTour(restaurants);
        System.out.println(tour);

        // ONE RESTAURANT
        //LngLat rest = new LngLat(restaurants[0].getLng(), restaurants[0].getLat());
        //ArrayList<LngLat> path = Path.getPathPoints(Drone.APPLETON_TOWER.toNode(), rest.toNode());
        //ArrayList<LngLat> path2 = Path.pathToStart(path);
        //ArrayList<LngLat> path = Path.totalPath(Drone.APPLETON_TOWER.toNode(), rest.toNode());
        GeoJson geoJson = new GeoJson();
        geoJson.outputGeoJson("01", "01", "2023", tour);
        // Flightpath.outputJson("01", "01", "2023", drone.getFlightpath());

    }
}
