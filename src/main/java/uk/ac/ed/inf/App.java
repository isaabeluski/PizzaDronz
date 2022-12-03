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
        ArrayList<Order> orders = Order.getOrders("2023-01-01");
        Restaurant[] restaurants = Restaurant.getRestaurants();
        DayOrder day = new DayOrder("2023-01-01", orders, restaurants);


        // ALL ORDERS OF A DAY
        Drone drone = new Drone(day.getFilteredOrders());
        ArrayList<LngLat> tour = drone.makeDeliveries(restaurants);
        //System.out.println(tour);

        // ONE RESTAURANT
        //LngLat rest = new LngLat(restaurants[0].getLng(), restaurants[0].getLat());
        //ArrayList<LngLat> path = new Path(restaurants[0]).pathToLngLat();
        //ArrayList<LngLat> path2 = Path.pathToStart(path);
        //ArrayList<LngLat> path = Path.totalPath(Drone.APPLETON_TOWER.toNode(), rest.toNode());
        GeoJson geoJson = new GeoJson();
        geoJson.outputGeoJson("01", "01", "2023", tour);
        // Flightpath.outputJson("01", "01", "2023", drone.getFlightpath());

    }
}
