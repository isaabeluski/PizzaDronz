package uk.ac.ed.inf;

import com.mapbox.geojson.Polygon;

import java.awt.geom.Point2D;
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
        LngLat[] centralCoordinates = server.centralCoordinates;
        ArrayList<ArrayList<LngLat>> noFlyZones =  server.noFlyZones;
        ArrayList<Order> orders = server.orders;
        Restaurant[] restaurants = server.restaurants;
        DayOrder day = new DayOrder("2023-01-01", orders, restaurants);


        // ALL ORDERS OF A DAY
        //Drone drone = new Drone(day.getFilteredOrders());
        //ArrayList<LngLat> tour = drone.doTour(restaurants, noFlyZones);
        //System.out.println(tour);

        // ONE RESTAURANT
        //LngLat rest = new LngLat(restaurants[0].getLng(), restaurants[0].getLat());
        //ArrayList<LngLat> path = Path.getPathPoints(Drone.APPLETON_TOWER.toNode(), rest.toNode(), noFlyZones);
        //GeoJson geoJson = new GeoJson();
        //geoJson.outputGeoJsonFolder("01", "01", "2023", path);

        Node start = new Node(new LngLat(-3.1890,55.9451));
        Node end = new Node(new LngLat(	-3.1891,55.9451));

        boolean doesIntersect = start.intersects(end, noFlyZones);
        System.out.println(doesIntersect);


    }
}
