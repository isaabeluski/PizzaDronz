package uk.ac.ed.inf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Represents a drone and makes delivery possible.
 */
public class Drone {

    private int battery;
    private final LngLat start;
    private final ArrayList<Order> orders;
    private final ArrayList<Flightpath> flightpath = new ArrayList<>();
    private static final int FULL_BATTERY = 2000;

    private final ArrayList<LngLat> completeTour = new ArrayList<>();
    private final ArrayList<Deliveries> deliveries = new ArrayList<>();

    /**
     * Represents where the drone takes off from - in this case Appleton Tower.
     */
    public static final LngLat STARTING_POINT = new LngLat(-3.186874, 55.944494);

    /**
     * Used to keep track of the time a movement calculation takes.
     */
    public static long startTime;
    Restaurant[] restaurants;


    /**
     * Constructor.
     * @param orders The orders of a day.
     */
    public Drone(ArrayList<Order> orders, Restaurant[] restaurants) {
        this.battery = FULL_BATTERY;
        this.start = STARTING_POINT;
        this.restaurants = restaurants;
        this.orders = new DayOrders().getDayOrders(orders, restaurants);
    }

    /**
     * Calculates the battery used when going to a restaurant and coming back.
     * @param path1 The path to the restaurant.
     * @param path2 The path back to the start.
     * @return The battery used.
     */
    public int batteryCost(ArrayList<LngLat> path1, ArrayList<LngLat> path2) {
        return path1.size() + path2.size();
    }

    public Flightpath addHover(LngLat point, Order order) {
        return new Flightpath(
                order.getOrderNo(),
                point.lng(),
                point.lat(),
                null,
                point.lng(),
                point.lat(),
                System.nanoTime() - startTime);
    }

    /**
     * Main method that makes the drone deliver the orders of a specific day. It goes through all orders of a day
     * and calculates the best path to the restaurant and back to the start. It takes into account the battery of the
     * drone, since it will not take off if there is not enough battery to go to the restaurant and back.
     */
    public void makeDeliveries() {

        startTime = System.nanoTime();

        // Goes through the sorted order list, based on the restaurant with the shortest path.
        for (Order ord : orders) {

            Restaurant correspondingRestaurant = ord.restaurantOrdered(restaurants);
            LngLat restaurantPos = new LngLat(correspondingRestaurant.getLng(), correspondingRestaurant.getLat());

            if (ord.getOrderOutcome() == OrderOutcome.ValidButNotDelivered) {

                // Calculates the path to the restaurant and a hover move.
                var pathToRestaurant = new Path(start, restaurantPos);
                var pathToRestaurantPoints = pathToRestaurant.getPathInLngLat();
                var hoverInRestaurant = addHover(restaurantPos, ord);

                // Calculates the path back to the start and a hover move.
                var returnPath = new Path(restaurantPos, start);
                var returnPathPoints = returnPath.getPathInLngLat();
                var hoverAtStart = addHover(start, ord);

                // Checks if the battery is enough to make the delivery.
                if (batteryCost(pathToRestaurantPoints, returnPathPoints) > battery) {
                    break;
                }

                // Adds path to the restaurant.
                completeTour.addAll(pathToRestaurantPoints);
                flightpath.addAll(pathToRestaurant.getMoves(ord));
                flightpath.add(hoverInRestaurant);

                // Adds path back to the start.
                completeTour.addAll(returnPathPoints);
                flightpath.addAll(returnPath.getMoves(ord));
                flightpath.add(hoverAtStart);

                ord.setOrderOutcome(OrderOutcome.Delivered);
                battery -= batteryCost(pathToRestaurantPoints, returnPathPoints);

            }
        }

        // Creates a Deliveries object for every order of a day.
        for (Order ord : orders) {
            Deliveries delivery = new Deliveries(ord.getOrderNo(), ord.getOrderOutcome().name(), ord.getPriceTotalInPence());
            deliveries.add(delivery);
        }

        // TODO: Borrar esto después.
        System.out.println("Number of moves: " + flightpath.size());
        System.out.println("Battery remaining: " + battery);
        int count = 0;
        for (Order ord : orders) {
            if (ord.getOrderOutcome() == OrderOutcome.Delivered) {
                count++;
            }
        }
        System.out.println("Number of orders delivered: " + count);
        System.out.println("Number of total orders: " + orders.size());
    }


    /**
     * Converts a list of LngLat to a FeatureCollection, so it can then be converted to a GeoJson file.
     * @param path The path the drone takes.
     * @return The path as a FeatureCollection.
     */
    private FeatureCollection lnglatToFC(ArrayList<LngLat> path){
        ArrayList<Point> points = new ArrayList<>();

        for(LngLat move: path){
            points.add(Point.fromLngLat(move.lng(), move.lat()));
        }

        Feature featureLineString =  Feature.fromGeometry(LineString.fromLngLats(points));
        return FeatureCollection.fromFeature(featureLineString);
    }

    /**
     * Creates a GeoJson file from a list of points.
     * @param path  The path the drone takes.
     */
    public void outputDroneGeoJson(ArrayList<LngLat> path) {
        try {
            String date = Server.getInstance().getDate();
            FileWriter file = new FileWriter("resultfiles/drone-" + date + ".geojson");
            file.write(lnglatToFC(path).toJson());
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Flightpath> getFlightpath() {
        return flightpath;
    }

    public ArrayList<Deliveries> getDeliveries() {
        return deliveries;
    }

    public ArrayList<LngLat> getCompleteTour() {
        return completeTour;
    }
}

